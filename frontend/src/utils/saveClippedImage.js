import Konva from 'konva';

function downloadURI(uri, name) {
  var link = document.createElement('a');
  link.download = name;
  link.href = uri;
  link.click();
}

const saveClippedImage = (image, points, width, height) => {
  const Stage = new Konva.Stage({
    container: 'save-image',
    width: width,
    height: height,
  });

  const Layer = new Konva.Layer();
  Stage.add(Layer);

  const Group = new Konva.Group({
    clipFunc: (ctx) => {
      ctx.beginPath();
      ctx.moveTo(points[0][0], points[0][1]);
      for (let i = 1; i < points.length; i++) {
        ctx.lineTo(points[i][0], points[i][1]);
      }
      ctx.closePath();
    },
  });

  const Image = new Konva.Image({
    width,
    height,
    image,
    x: 0,
    y: 0,
  });

  Group.add(Image);
  Layer.add(Group);
  Stage.draw();

  const dataURL = Stage.toDataURL({ pixelRatio: 3 });
  downloadURI(dataURL, 'cat');
};

export default saveClippedImage;
