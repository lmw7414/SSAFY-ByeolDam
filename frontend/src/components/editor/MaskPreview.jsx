import React, { useRef } from 'react';
import { Stage, Layer, Image, Line } from 'react-konva';

import generateColor from '../../utils/generateColor';

export default function MaskPreview({
  width,
  height,
  pointList,
  setHovered,
  setSelected,
  image,
  editorSize,
  convertedPoints,
  setConvertedPoints,
  imageConfig,
  setImageConfig,
}) {
  const preview = useRef();

  return (
    <Stage ref={preview} width={width} height={height} className="border-radius">
      <Layer>
        <Image image={image} opacity={0.8} x={0} y={0} width={width} height={height} />
        {pointList.map((data, i) => {
          const color = generateColor(i, pointList.length);
          return (
            <Line
              key={i}
              index={i}
              points={data.reduce(
                (prev, [x, y]) => [...prev, (x * width) / editorSize, (y * height) / editorSize],
                [],
              )}
              stroke={color}
              fill={color}
              closed={true}
              opacity={0.5}
              lineCap={'round'}
              onMouseEnter={() => {
                preview.current.container().style.cursor = 'pointer';
                setHovered(i);
              }}
              onMouseLeave={() => {
                preview.current.container().style.cursor = 'auto';
                setHovered(-1);
              }}
              onClick={() => {
                setSelected(i);
              }}
            />
          );
        })}
      </Layer>
    </Stage>
  );
}
