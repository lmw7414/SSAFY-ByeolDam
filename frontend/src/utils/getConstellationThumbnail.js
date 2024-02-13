import Konva from 'konva';
import dataURLtoBlob from './dataURLtoBlob';

const downloadURI = (uri, name) => {
  var link = document.createElement('a');
  link.download = name;
  link.href = uri;
  link.click();
};

export const getConstellationThumbnail = ({ width, height, points, img, imageConfig }) => {
  const stage = new Konva.Stage({
    container: 'save-image',
    width: width,
    height: height,
  });

  const layer = new Konva.Layer();
  stage.add(layer);

  const group = new Konva.Group({
    clipFunc: (ctx) => {
      ctx.beginPath();
      ctx.moveTo(points[0][0], points[0][1]);
      for (let i = 1; i < points.length; i++) {
        ctx.lineTo(points[i][0], points[i][1]);
      }
      ctx.closePath();
    },
  });

  layer.add(group);

  const image = new Konva.Image({
    image: img,
    x: imageConfig.x,
    y: imageConfig.y,
    width: imageConfig.width,
    height: imageConfig.height,
    crop: imageConfig.crop,
    opacity: 0.3,
  });

  group.add(image);
  stage.draw();
  downloadURI(stage.toDataURL({ pixelRatio: 256 / width }), 'thumb');
  const thumb = dataURLtoBlob(stage.toDataURL({ pixelRatio: 256 / width }));

  const line = new Konva.Line({
    points: points.reduce((prev, [x, y]) => [...prev, x, y], []),
    stroke: '#8E7CAC',
    strokeWidth: 3,
    closed: true,
    tension: 0.05,
  });

  layer.add(line);

  points.map(([x, y], i) => {
    const circle = new Konva.Circle({
      x: x,
      y: y,
      radius: 2,
      fill: 'white',
    });
    layer.add(circle);
  });

  stage.draw();
  downloadURI(stage.toDataURL({ pixelRatio: 256 / width }), 'cthumb');
  const cthumb = dataURLtoBlob(stage.toDataURL({ pixelRatio: 256 / width }));

  console.log(JSON.stringify({ width, height, points }));

  return { thumb, cthumb };
};
