import { useState, useEffect } from 'react';
import { Stage, Layer, Line, Circle, Rect, Group, Image } from 'react-konva';

import { convertPoints, getImageConfig } from '../../utils/saveClippedImage';

export default function ContoursPreview({ width, height, selected, pointList, points, image }) {
  const [convertedPoints, setConvertedPoints] = useState([]);
  const [imageConfig, setImageConfig] = useState(null);

  useEffect(() => {
    if (!image || !points.length) return;
    setConvertedPoints(convertPoints(points, 1200, 1200, width, height));
    setImageConfig(getImageConfig(image, points, 1200, 1200, width, height));
  }, [points]);

  return (
    <Stage width={width} height={height}>
      <Layer>
        <Rect width={width} height={height} fill="#200B41" opacity={0.8} />
        {image && imageConfig && convertPoints.length && (
          <Group
            clipFunc={(ctx) => {
              ctx.beginPath();
              ctx.moveTo(convertedPoints[0][0], convertedPoints[0][1]);
              for (let i = 1; i < convertedPoints.length; i++) {
                ctx.lineTo(convertedPoints[i][0], convertedPoints[i][1]);
              }
              ctx.closePath();
            }}
          >
            <Image
              image={image}
              x={imageConfig.x}
              y={imageConfig.y}
              width={imageConfig.width}
              height={imageConfig.height}
              crop={imageConfig.crop}
              opacity={0.3}
            />
          </Group>
        )}
        {selected < pointList.length && (
          <Line
            points={convertedPoints.reduce((prev, [x, y]) => [...prev, x, y], [])}
            stroke={'#8E7CAC'}
            strokeWidth={6}
            closed={true}
            tension={0.1}
          />
        )}
        {selected < pointList.length &&
          convertedPoints.map(([x, y], i) => (
            <Circle index={i} key={i} x={x} y={y} radius={6} fill={'white'} />
          ))}
      </Layer>
    </Stage>
  );
}
