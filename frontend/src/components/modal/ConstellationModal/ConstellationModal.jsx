import { useState, useEffect } from 'react';

import ConstellationRow from './ConstellationRow';

import axios from '../../../apis/axios.js';
import parseJwt from '../../../utils/parseJwt.js';

export default function ConstellationModal() {
  const [consteallationInfos, setConstellationInfos] = useState([]);

  const readConstellations = async () => {
    const JWTtoken = sessionStorage.getItem('token');
    const parsed = parseJwt(JWTtoken);
    // console.log(parsed.email);
    const data = await axios.get(`/constellations/user/${parsed.email}`);
    const array = data.data.result.content;
    console.log(array.length);
    setConstellationInfos(array);
  };

  useEffect(() => {
    readConstellations();
  }, []);

  const array = [
    {
      id: 0,
      title: '갱얼쥐',
      tag: 'string',
      disclosure: 'VISIBLE',
      hits: 45,
      description: '멍멍멍뭉이',
      createdAt: '2024-02-09T08:17:22.569Z',
      modifiedAt: '2024-02-09T08:17:22.569Z',
      deletedAt: '2024-02-09T08:17:22.569Z',
    },
    {
      id: 1,
      title: '고먐미',
      tag: 'string',
      disclosure: 'VISIBLE',
      hits: 241,
      description: '냐옹냐옹냐옹이',
      createdAt: '2024-02-09T08:17:22.569Z',
      modifiedAt: '2024-02-09T08:17:22.569Z',
      deletedAt: '2024-02-09T08:17:22.569Z',
    },
  ];
  return (
    <div className="constellation-modal-container">
      <div className="">
        {array.map((constellation) => (
          <ConstellationRow key={constellation.id} title={constellation.title} />
        ))}
      </div>
      <div className="constellation_main_buttons_box">
        <img
          src="/src/assets/images/constellation-modal/constellation_create_button.png"
          alt="post_create_button"
          className="constellation-main-button"
          // onClick={openConstellationModal}
        />
        <img
          src="/src/assets/images/constellation-modal/post_create_button.png"
          alt="post_create_button"
          className="constellation-main-button"
          // onClick={openConstellationModal}
        />
      </div>
    </div>
  );
}
