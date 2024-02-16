const R = 45;

const getPositionList = (size) => [
  ...Array.from({ length: size }, (x, i) => {
    const rand = i == 0 ? 2 : Math.random() * 5;
    // const rand = 0;
    const angle = (Math.PI / 180) * (i % 2 ? 57 + rand : 33 + rand);
    const pos =
      (Math.PI / 180) * (((2 * 360) / size) * parseInt(i / 2) + rand * 2 + (i % 2 ? 0 : 10)) +
      29.75;
    6;
    return [
      Math.cos(pos) * R * Math.sin(angle),
      R * Math.cos(angle),
      Math.sin(pos) * R * Math.sin(angle),
    ];
  }),
];

export default getPositionList;
