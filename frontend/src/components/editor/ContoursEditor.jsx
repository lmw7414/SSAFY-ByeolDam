import { useEffect, useRef } from 'react';
import { Layer, Stage, Image as KonvaImage, Line, Circle } from 'react-konva';
import getUuidByString from 'uuid-by-string';
import getSegmentDistance from '../../utils/getSegmentDistance';

export default function ContoursEditor({
  image,
  width,
  height,
  points,
  setPoints,
  history,
  setHistory,
}) {
  const editor = useRef();
  const imageRef = useRef();

  const getUuidByPosition = (x, y) => {
    const string = `${x},${y}`;
    return getUuidByString(string);
  };

  const deletePoint = (index) => {
    setHistory([...history, points]);
    setPoints([...points.slice(0, index), ...points.slice(index + 1)]);
  };

  const movePoint = (x, y, index) => {
    setHistory([...history, points]);
    setPoints([...points.slice(0, index), [x, y], ...points.slice(index + 1)]);
  };

  const addPoint = (x, y, index) => {
    setHistory([...history, points]);
    setPoints([...points.slice(0, index + 1), [x, y], ...points.slice(index + 1)]);
  };

  const globalClickHandler = (e) => {
    if (e.evt.button === 0) {
      const x = e.evt.offsetX;
      const y = e.evt.offsetY;
      let nearestLineIdx = 0;
      let minDist = Infinity;

      for (let i = 0; i < points.length; i++) {
        const dist = getSegmentDistance(points[i], points[(i + 1) % points.length], [x, y]);
        if (minDist > dist) {
          minDist = dist;
          nearestLineIdx = i;
        }
      }

      if (nearestLineIdx <= points.length) addPoint(x, y, nearestLineIdx);
    }
  };

  return (
    <Stage
      ref={editor}
      width={width}
      height={height}
      onContextMenu={(e) => {
        e.evt.preventDefault();
      }}
      onClick={globalClickHandler}
      onMouseEnter={() => {
        editor.current.container().style.cursor = 'pointer';
      }}
      onMouseLeave={() => {
        editor.current.container().style.cursor = 'auto';
      }}
      className="border-radius"
    >
      <Layer>
        <KonvaImage
          ref={imageRef}
          x={0}
          y={0}
          width={width}
          height={height}
          image={image}
          opacity={0.6}
        />
        {points.map(([x, y], i) => (
          <Line
            key={getUuidByPosition(x, y)}
            index={i}
            points={[x, y, ...points[(i + 1) % points.length]]}
            stroke={'red'}
            strokeWidth={3}
          />
        ))}

        {points.map(([x, y], i) => (
          <Circle
            index={i}
            key={getUuidByPosition(x, y)}
            x={x}
            y={y}
            radius={3}
            fill={'white'}
            stroke={'red'}
            strokeWidth={1}
            onDragEnd={({ target }) => {
              movePoint(target.attrs.x, target.attrs.y, target.attrs.index);
            }}
            onClick={(e) => {
              if (e.evt.button === 2) deletePoint(e.target.attrs.index);
            }}
            draggable
          />
        ))}
      </Layer>
    </Stage>
  );
}
