import { useRef, useState, useEffect } from 'react';
import { Stage, Layer, Image as KonvaImage, Transformer } from 'react-konva';

import GradientBackground from './GradientBackground';
import getGradientFromImage from '../../utils/getGradientFromImage';
import TagEditor from './TagEditor';
import dataURLtoBlob from '../../utils/dataURLtoBlob';

export default function ImageUpload({ setStep, setFile, setArticle, article }) {
  const [editor, setEditor] = useState({
    image: null,
    color: null,
    x: 0,
    y: 0,
    width: 0,
    height: 0,
  });
  const [history, setHistory] = useState([]);
  const [selected, setSelected] = useState(false);
  const [originalFile, setOriginalFile] = useState(null);

  const stage = useRef();
  const imageRef = useRef();
  const transformer = useRef();
  const width = 512;
  const height = 512;

  const keyInputHandler = (e) => {
    if (e.key === 'z' && (e.ctrlKey || e.metaKey)) undo();
  };

  const setImageState = ({ x, y, scaleX, scaleY, rotation }) => {
    imageRef.current.x(x);
    imageRef.current.y(y);
    imageRef.current.scaleX(scaleX);
    imageRef.current.scaleY(scaleY);
    imageRef.current.rotation(rotation);
  };

  const undo = () => {
    if (history.length == 0) return;
    setImageState(history[history.length - 1]);
    setHistory([...history.slice(0, history.length - 1)]);
  };

  const clickImage = () => {
    setSelected(transformer.current.nodes().indexOf(imageRef.current) < 0);
  };

  const setDescription = (e) => {
    setArticle({
      ...article,
      description: e.target.value,
    });
  };

  const setTags = (articleHashtagSet) => {
    setArticle({
      ...article,
      articleHashtagSet,
    });
  };

  const saveFile = () => {
    transformer.current.nodes([]);
    const blob = dataURLtoBlob(stage.current.toDataURL({ pixelRatio: 2 }));
    const file = new File([blob], originalFile.name.split('.')[0] + '.png', { type: 'image/png' });
    setFile(file);
  };

  const nextStep = () => {
    if (!article.description.trim() && !alert('설명을 입력해주세요')) return;
    if (!editor.image && !alert('사진을 추가해주세요')) return;
    saveFile();
    setStep(2);
  };

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
        const color = await getGradientFromImage(img.src);
        setEditor({
          color,
          image: img,
          x: (width - (width * img.width) / Math.max(img.width, img.height)) / 2,
          y: (height - (height * img.height) / Math.max(img.width, img.height)) / 2,
          width: (width * img.width) / Math.max(img.width, img.height),
          height: (height * img.height) / Math.max(img.width, img.height),
        });
      };
    });
  };

  useEffect(() => {
    if (!editor.image) return;
    const initialState = { x: editor.x, y: editor.y, scaleX: 1, scaleY: 1, rotation: 0 };

    setImageState(initialState);
    setHistory([]);

    transformer.current.getLayer().batchDraw();
  }, [editor]);

  useEffect(() => {
    if (selected) transformer.current.nodes([imageRef.current]);
    else transformer.current.nodes([]);
  }, [selected]);

  return (
    <div className="article-editor-box">
      <div className="image-upload-box">
        <div className="image-editor-wrapper" tabIndex={1} onKeyDown={keyInputHandler}>
          <Stage ref={stage} width={width} height={height}>
            <Layer>
              {editor.color && (
                <GradientBackground
                  color={editor.color}
                  width={width}
                  height={height}
                  onClick={() => {
                    setSelected(false);
                  }}
                />
              )}
              {editor.image && (
                <KonvaImage
                  ref={imageRef}
                  x={editor.x}
                  y={editor.y}
                  image={editor.image}
                  width={editor.width}
                  height={editor.height}
                  onClick={clickImage}
                  onDragStart={() => {
                    setHistory([...history, { ...imageRef.current.attrs }]);
                  }}
                  draggable={selected}
                />
              )}
              <Transformer
                ref={transformer}
                onTransformStart={() => {
                  setHistory([...history, { ...imageRef.current.attrs }]);
                }}
              />
            </Layer>
          </Stage>
        </div>
        <div
          className="image-change-btn"
          onClick={(e) => {
            e.stopPropagation();
            loadImage();
          }}
        >
          {editor.image ? '사진 변경' : '사진 추가'}
        </div>
      </div>
      <div className="article-content-box">
        <label className="article-content-label" htmlFor="article-description-label">
          설명
        </label>
        <input
          className="article-content-input"
          type="text"
          onChange={setDescription}
          name="description"
        />
        <label className="article-content-label" htmlFor="">
          태그
        </label>
        <TagEditor setTags={setTags} />
        <button className="article-upload-btn" type="button" onClick={nextStep}>
          다음
        </button>
      </div>
    </div>
  );
}
