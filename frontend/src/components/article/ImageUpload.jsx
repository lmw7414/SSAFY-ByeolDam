import { useRef, useState, useEffect } from 'react';
import { Stage, Layer, Image as KonvaImage, Transformer } from 'react-konva';

import GradientBackground from './GradientBackground';
import getGradientFromImage from '../../utils/getGradientFromImage';

export default function ImageUpload({ setStep, setFile }) {
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
  const imageRef = useRef();
  const transformer = useRef();
  const width = 600;
  const height = 600;

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

  const loadImage = () => {
    const input = document.createElement('input');
    input.setAttribute('type', 'file');
    input.setAttribute('accept', 'image/*');

    input.click();

    input.addEventListener('change', () => {
      const file = input.files[0];

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

  return (
    <div tabIndex={1} onKeyDown={keyInputHandler}>
      <Stage width={width} height={height}>
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
      <div
        onClick={(e) => {
          e.stopPropagation();
          loadImage();
        }}
      >
        사진 추가
      </div>
      <button
        type="button"
        onClick={() => {
          setStep(2);
        }}
      >
        다음
      </button>
    </div>
  );
}
