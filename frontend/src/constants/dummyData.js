// const constellationList = [
//   { id: 0, url: './images/sample/cat.png', position: [0, 20, 50] },
//   { id: 1, url: './images/sample/cat.png', position: [0, 20, -50] },
//   { id: 2, url: './images/sample/cat.png', position: [-50, 20, 0] },
//   { id: 3, url: './images/sample/cat.png', position: [-35.3, 20, 35.3] },
//   { id: 4, url: './images/sample/cat.png', position: [35.3, 20, 35.3] },
//   { id: 5, url: './images/sample/cat.png', position: [-35.3, 20, -35.3] },
//   { id: 6, url: './images/sample/cat.png', position: [35.3, 20, -35.3] },
//   { id: 7, url: './images/sample/cat.png', position: [50, 20, 0] },
// ];

const R = 40;
const size = 15;
const data = [
  {
    id: 'bread',
    url: './data/춤2.json',
    thumbnail: './images/sample/춤2.jpg',
  },
  { id: 'car', url: './data/car.json', thumbnail: './images/sample/car.jpg' },
  { id: 'cat', url: './data/cat.json', thumbnail: './images/sample/cat.jpg' },
  {
    id: 'chicken',
    url: './data/싸피.json',
    thumbnail: './images/sample/싸피.png',
  },
  {
    id: 'coffee',
    url: './data/삼성.json',
    thumbnail: './images/sample/삼성.png',
  },
  {
    id: 'couple',
    url: './data/couple.json',
    thumbnail: './images/sample/couple.jpg',
  },
  {
    id: 'diary',
    url: './data/diary.json',
    thumbnail: './images/sample/diary.jpg',
  },
  {
    id: 'family',
    url: './data/family.json',
    thumbnail: './images/sample/family.jpg',
  },
  {
    id: 'food',
    url: './data/food.json',
    thumbnail: './images/sample/food.jpg',
  },
  {
    id: 'redpanda',
    url: './data/redpanda.json',
    thumbnail: './images/sample/redpanda.jpg',
  },
  {
    id: 'running',
    url: './data/running.json',
    thumbnail: './images/sample/running.jpg',
  },
  {
    id: 'scenery',
    url: './data/여행2.json',
    thumbnail: './images/sample/여행2.jpg',
  },
  {
    id: 'selfie',
    url: './data/강아지3.json',
    thumbnail: './images/sample/강아지3.jpg',
  },
  {
    id: 'sports',
    url: './data/sports.json',
    thumbnail: './images/sample/sports.jpg',
  },
  {
    id: 'friend',
    url: './data/friend.json',
    thumbnail: './images/sample/friend.jpg',
  },
];

const constellationList = [
  ...Array.from({ length: size }, (x, i) => {
    const rand = Math.random() * 10;
    const angle = (Math.PI / 180) * (i % 2 ? 42 + rand : 67 + rand);
    const pos =
      (Math.PI / 180) * (((2 * 360) / size) * parseInt(i / 2) + rand * 4 + (i % 2 ? 0 : 10));

    return {
      id: data[i].id,
      position: [
        Math.cos(pos) * R * Math.sin(angle),
        R * Math.cos(angle),
        Math.sin(pos) * R * Math.sin(angle),
      ],
      url: data[i].url,
      thumbnail: data[i].thumbnail,
    };
  }),
];

export default constellationList;
