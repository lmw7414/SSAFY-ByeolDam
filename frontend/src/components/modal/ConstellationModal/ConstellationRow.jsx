import { useState, useEffect } from 'react';

import axios from '../../../apis/axios';

export default function ConstellationRow({ title, constellationId, constellationThumb }) {
  const [starArray, setStarArray] = useState([]);

  const readStars = async () => {
    const data = await axios.get(`articles/constellation/${constellationId}`);
    // console.log(data);
    setStarArray(data.data.result);
  };

  useEffect(() => {
    readStars();
  }, []);
  const dummyArray = [
    {
      id: 0,
      title: '',
      imageResponse: { imageName: '', imageUrl: '/public/images/search-test/1.jpg' },
    },
    {
      id: 1,
      title: '',
      imageResponse: { imageName: '', imageUrl: '/public/images/search-test/2.jpg' },
    },
    {
      id: 2,
      title: '',
      imageResponse: { imageName: '', imageUrl: '/public/images/search-test/3.jpg' },
    },
    {
      id: 3,
      title: '',
      imageResponse: { imageName: '', imageUrl: '/public/images/search-test/4.jpg' },
    },
    {
      id: 4,
      title: '',
      imageResponse: { imageName: '', imageUrl: '/public/images/search-test/5.jpg' },
    },
    {
      id: 5,
      title: '',
      imageResponse: { imageName: '', imageUrl: '/public/images/search-test/6.jpg' },
    },
    {
      id: 6,
      title: '',
      imageResponse: { imageName: '', imageUrl: '/public/images/search-test/7.jpg' },
    },
    {
      id: 7,
      title: '',
      imageResponse: { imageName: '', imageUrl: '/public/images/search-test/8.jpg' },
    },
    {
      id: 8,
      title: '',
      imageResponse: { imageName: '', imageUrl: '/public/images/search-test/9.jpg' },
    },
    {
      id: 9,
      title: '',
      imageResponse: { imageName: '', imageUrl: '/public/images/search-test/10.jpg' },
    },
    {
      id: 10,
      title: '',
      imageResponse: { imageName: '', imageUrl: '/public/images/search-test/11.jpg' },
    },
    {
      id: 11,
      title: '',
      imageResponse: { imageName: '', imageUrl: '/public/images/search-test/12.jpg' },
    },
    {
      id: 12,
      title: '',
      imageResponse: { imageName: '', imageUrl: '/public/images/search-test/13.jpg' },
    },
  ];
  return (
    <div className="constellation-row">
      <div className="constellation-thumbnail-container">
        <img src={constellationThumb} alt="" className="constellation-thumbnail" />
        <h3>{title}</h3>
      </div>
      <div className="star-list-container">
        {dummyArray.map((star) => (
          <div key={star.id} className="star-image-container">
            <img src={star.imageResponse.imageUrl} alt="" className="star-image" />
          </div>
        ))}
      </div>
    </div>
  );
}
