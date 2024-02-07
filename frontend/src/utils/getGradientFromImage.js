import { Image } from 'image-js';
import { getColor } from 'color-thief-node';

const getGradientFromImage = async (img) => {
  const image = await Image.load(img);

  const topImage = image.crop({ x: 0, y: 0, width: image.width, height: image.height / 3 });
  const bottomImage = image.crop({
    x: 0,
    y: (image.height * 2) / 3,
    width: image.width,
    height: image.height / 3,
  });

  const topColor = getColor(topImage.getCanvas());
  const bottomColor = getColor(bottomImage.getCanvas());
  return { topColor, bottomColor };
};

export default getGradientFromImage;
