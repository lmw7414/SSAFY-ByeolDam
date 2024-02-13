export const downloadURI = (uri, name) => {
  var link = document.createElement('a');
  link.download = name;
  link.href = uri;
  link.click();
};

export const convertPoints = (points, width, height, w, h) => {
  const [lx, rx] = points.reduce(
    (prev, now) => [Math.min(now[0], prev[0]), Math.max(now[0], prev[1])],
    [width, 0],
  );

  const [dy, uy] = points.reduce(
    (prev, now) => [Math.min(now[1], prev[0]), Math.max(now[1], prev[1])],
    [height, 0],
  );

  const wd = rx - lx;
  const ht = uy - dy;
  const size = Math.max(wd, ht);

  return points.map(([x, y]) => {
    const X = (x - lx + (size - wd) / 2) / size;
    const Y = (y - dy + (size - ht) / 2) / size;

    return [X * w * 0.9 + w * 0.05, Y * h * 0.9 + h * 0.05];
  });
};

export const getImageConfig = (image, points, width, height, w, h) => {
  const [lx, rx] = points.reduce(
    (prev, now) => [Math.min(now[0], prev[0]), Math.max(now[0], prev[1])],
    [width, 0],
  );

  const [dy, uy] = points.reduce(
    (prev, now) => [Math.min(now[1], prev[0]), Math.max(now[1], prev[1])],
    [height, 0],
  );

  const wd = rx - lx;
  const ht = uy - dy;
  const size = Math.max(wd, ht);

  points = points.map(([x, y]) => {
    const X = (x - lx + (size - wd) / 2) / size;
    const Y = (y - dy + (size - ht) / 2) / size;

    return [X * w * 0.9 + w * 0.05, Y * h * 0.9 + h * 0.05];
  });

  const crop = {
    x: (lx * image.width) / width,
    y: (dy * image.height) / height,
    width: (wd * image.width) / width,
    height: (ht * image.height) / height,
  };

  return {
    crop,
    x: ((w - w * (wd / size)) * 0.9) / 2 + w * 0.05,
    y: ((h - h * (ht / size)) * 0.9) / 2 + h * 0.05,
    width: w * (wd / size) * 0.9,
    height: h * (ht / size) * 0.9,
  };
  // const dataURL = Stage.toDataURL({ pixelRatio: 2 });
  // downloadURI(dataURL, 'clipped');
};
