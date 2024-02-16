import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

import douglasPeucker from '../../utils/douglasPeucker';

import ContoursEditor from '../../components/editor/ContoursEditor';
import MaskPreview from '../../components/editor/MaskPreview';
import ContoursPreview from '../../components/editor/ContoursPreview';

import { addConstellation, getAIcontours } from '../../apis/constellation';
import { getConstellationThumbnail } from '../../utils/getConstellationThumbnail';
import getResizedImage from '../../utils/getResizedImage';
import dataURLtoBlob from '../../utils/dataURLtoBlob';

export default function ConstellationWriting({ isNavBarVisible }) {
  const [points, setPoints] = useState([]);
  const [image, setImage] = useState(null);
  const [pointList, setPointList] = useState([]);
  const [selected, setSelected] = useState(0);
  const [hovered, setHovered] = useState(-1);
  const [history, setHistory] = useState([]);
  const [originalFile, setOriginalFile] = useState(null);
  const [originalData, setOriginalData] = useState(null);
  const [convertedPoints, setConvertedPoints] = useState([]);
  const [imageConfig, setImageConfig] = useState(null);
  const [name, setName] = useState('');
  const [isLoading, setIsLoading] = useState(false);

  const navigate = useNavigate();

  const editorSize = 550;

  const loadImage = () => {
    const input = document.createElement('input');
    input.setAttribute('type', 'file');
    input.setAttribute('accept', 'image/*');

    input.click();

    input.addEventListener('change', () => {
      const file = input.files[0];
      setOriginalFile(file);
      const img = new Image();
      img.src = URL.createObjectURL(file);
      img.onload = async () => {
        setPointList([]);
        setPoints([]);
        setImage(getResizedImage({ img, width: img.naturalWidth, height: img.naturalHeight }));
      };
    });
  };

  const setContoursData = (data) => {
    setOriginalData(data);
    setPointList(
      data
        .map((dt) => {
          const height = dt.image_height;
          const width = dt.image_width;

          const zipped = douglasPeucker(
            dt.contours.map((point) => {
              const [x, y] = point;
              return { x, y };
            }),
            4,
          );

          return zipped.map(({ x, y }) => [(x * editorSize) / width, (y * editorSize) / height]);
        })
        .filter((dt) => dt.length > 1),
    );
  };

  const getAIcontoursByImage = async () => {
    if (isLoading) return;
    const resizedImageBlob = dataURLtoBlob(image.src);

    const resizedImageFile = new File(
      [resizedImageBlob],
      originalFile.name.split('.')[0] + '.png',
      { type: 'image/png' },
    );

    setIsLoading(true);

    try {
      const data = await getAIcontours(resizedImageFile);
      setContoursData(data);
    } catch (e) {
      console.error(e);
    }

    setIsLoading(false);
  };

  const undo = () => {
    if (history.length == 0) return;
    setPoints(history[history.length - 1]);
    setHistory([...history.slice(0, history.length - 1)]);
  };

  const keyInputHandler = (e) => {
    if (e.key === 'z' && (e.ctrlKey || e.metaKey)) undo();
  };

  const writeConstellation = () => {
    if (!name.trim() && !alert('별자리의 이름을 입력해주세요')) return;
    if (isLoading) return;

    const { thumb, cthumb } = getConstellationThumbnail({
      imageConfig,
      originalFile,
      width: editorSize / 2 - 15,
      height: editorSize / 2 - 15,
      points: convertedPoints,
      img: image,
    });

    const resizedImageBlob = dataURLtoBlob(image.src);
    const resizedImageFile = new File(
      [resizedImageBlob],
      originalFile.name.split('.')[0] + '.png',
      { type: 'image/png' },
    );

    addConstellation({
      name,
      thumb,
      cthumb,
      contoursList: pointList,
      ultimate: convertedPoints,
      description: '',
      origin: resizedImageFile,
    }).then((result) => {
      navigate('/home');
      alert('별자리가 생성되었습니다.');
    });
  };

  useEffect(() => {
    if (pointList.length > 0) {
      setHistory([]);
      setPoints(pointList[selected]);
    }
  }, [selected]);

  useEffect(() => {
    if (pointList.length > 0) setPoints(pointList[0]);
  }, [pointList]);

  return (
    <div
      className={`constellation-editor-container ${isNavBarVisible ? 'constellation-editor-container-extend' : ''}`}
      tabIndex={1}
      onKeyDown={keyInputHandler}
    >
      <div className="constellation-editor-box">
        <div className="constellation-editor-wrapper">
          <ContoursEditor
            width={editorSize}
            height={editorSize}
            image={image}
            points={points}
            setPoints={setPoints}
            setHistory={setHistory}
            history={history}
          />
          <div className="constellation-editor-preview-container">
            <div className="constellation-editor-preview-label">AI 디텍션</div>
            <MaskPreview
              width={editorSize / 2 - 15}
              height={editorSize / 2 - 15}
              pointList={pointList}
              setHovered={setHovered}
              setSelected={setSelected}
              image={image}
              editorSize={editorSize}
            />
            <div className="constellation-editor-preview-label">별자리 미리보기</div>
            <ContoursPreview
              width={editorSize / 2 - 15}
              height={editorSize / 2 - 15}
              selected={selected}
              hovered={hovered}
              pointList={pointList}
              points={points}
              image={image}
              editorSize={editorSize}
              convertedPoints={convertedPoints}
              setConvertedPoints={setConvertedPoints}
              imageConfig={imageConfig}
              setImageConfig={setImageConfig}
            />
          </div>
        </div>
        <div className="constellation-editor-button-box">
          <div className="constellation-editor-button-wrapper">
            <button type="button" onClick={loadImage} disabled={isLoading}>
              사진변경
            </button>
            <button type="button" onClick={getAIcontoursByImage} disabled={isLoading}>
              AI 윤곽선 추출
            </button>
            {isLoading && <img src="/images/loading.gif" />}
          </div>
          <div className="constellation-editor-input-wrapper">
            <label>별자리 이름 :</label>
            <input
              className="constellation-editor-input"
              type="text"
              onChange={(e) => {
                setName(e.target.value);
              }}
            />
          </div>
        </div>
        <button
          type="button"
          className={`constellation-writing-btn ${isLoading ? 'btn-loading' : ''}`}
          onClick={writeConstellation}
          disabled={isLoading}
        >
          별자리 생성
        </button>
        <div id={'save-image'} style={{ display: 'none' }} />
      </div>
    </div>
  );
}
