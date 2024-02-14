const getResizedImage = ({ width, height, img }) => {
  const stage = new Konva.Stage({
    container: 'save-image',
    width: width,
    height: height,
  });

  const layer = new Konva.Layer();
  stage.add(layer);

  const image = new Konva.Image({
    image: img,
    x: (width - (width * width) / Math.max(width, height)) / 2,
    y: (height - (height * height) / Math.max(width, height)) / 2,
    width: (width * width) / Math.max(width, height),
    height: (height * height) / Math.max(width, height),
  });

  layer.add(image);
  stage.draw();
  const resizedImage = new Image();
  resizedImage.src = stage.toDataURL({ pixelRatio: 1024 / Math.max(width, height) });

  return resizedImage;
};

export default getResizedImage;
