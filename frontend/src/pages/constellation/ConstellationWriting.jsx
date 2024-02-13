import { useEffect, useState } from 'react';

import douglasPeucker from '../../utils/douglasPeucker';

import ContoursEditor from '../../components/editor/ContoursEditor';
import MaskPreview from '../../components/editor/MaskPreview';
import ContoursPreview from '../../components/editor/ContoursPreview';

import { getAIcontours } from '../../apis/constellation';
import { getConstellationThumbnail } from '../../utils/getConstellationThumbnail';

export default function ConstellationWriting() {
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
        setImage(img);
      };
    });
  };

  const setContoursData = (data) => {
    console.log(
      JSON.stringify(
        data
          .map((dt) => {
            const height = dt.image_height;
            const width = dt.image_width;

            const zipped = douglasPeucker(
              dt.contours.map((point) => {
                const [x, y] = point;
                return { x, y };
              }),
              5,
            );

            return zipped.map(({ x, y }) => [(x * editorSize) / width, (y * editorSize) / height]);
          })
          .filter((dt) => dt.length > 1),
      ),
    );

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
            5,
          );

          return zipped.map(({ x, y }) => [(x * editorSize) / width, (y * editorSize) / height]);
        })
        .filter((dt) => dt.length > 1),
    );
  };

  const getAIcontoursByImage = async () => {
    const data = await getAIcontours(originalFile);
    setContoursData(data);
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
    const { thumb, cthumb } = getConstellationThumbnail({
      imageConfig,
      width: editorSize / 2 - 15,
      height: editorSize / 2 - 15,
      points: convertedPoints,
      img: image,
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
    <div className="constellation-editor-container" tabIndex={1} onKeyDown={keyInputHandler}>
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
            />
          </div>
        </div>
        <div className="constellation-editor-button-box">
          <div className="constellation-editor-button-wrapper">
            <button>사진변경</button>
            <button>AI 윤곽선 추출</button>
          </div>
          <div className="constellation-editor-input-wrapper">
            <label>별자리 이름 :</label>
            <input className="constellation-editor-input" type="text" />
          </div>
        </div>
        <button className="constellation-writing-btn">별자리 생성</button>
        <div id={'save-image'} style={{ display: 'none' }} />
      </div>
      <div id={'save-image'} style={{ display: 'none' }} />
    </div>
  );
}
