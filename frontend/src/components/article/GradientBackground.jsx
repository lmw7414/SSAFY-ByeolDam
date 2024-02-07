import { Rect } from 'react-konva';

export default function GradientBackground({ color, width, height, onClick }) {
  return (
    <>
      {color && (
        <Rect
          width={width}
          height={height}
          x={0}
          y={0}
          fillLinearGradientColorStops={[
            0,
            `rgb(${color.topColor.join(',')})`,
            1,
            `rgb(${color.bottomColor.join(',')})`,
          ]} // '#ffffff'
          fillLinearGradientStartPoint={{ x: width / 2, y: 0 }}
          fillLinearGradientEndPoint={{
            x: width / 2,
            y: height,
          }}
          onClick={onClick}
        />
      )}
    </>
  );
}
