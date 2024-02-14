import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

import useModal from '../../../hooks/useModal';

import ConstellationRow from './ConstellationRow';

import axios from '../../../apis/axios.js';

export default function ConstellationModal() {
  const [constellationArray, setConstellationArray] = useState([]);
  const [currentModalState, setModalState] = useModal();

  const closeModal = () => {
    setModalState({
      isOpen: false,
      title: '',
      children: null,
    });
  };

  const navigate = useNavigate();

  const openConstellaionWriting = () => {
    navigate('/constellation');
    closeModal();
  };

  const readConstellations = async () => {
    const profileStr = sessionStorage.getItem('profile');
    const profile = JSON.parse(profileStr);
    const data = await axios.get(`/constellations/user/${profile.nickname}`);
    const arr = data.data.result;
    console.log(arr[0].constellationUserEntities[0].constellationId);
    setConstellationArray(arr);
  };

  // console.log(constellationArray);

  useEffect(() => {
    readConstellations();
  }, []);

  const array = [
    {
      id: 0,
      name: '갱얼쥐',
      contourResponse: { thumbUrl: '/public/images/search-test/13.jpg' },
      constellationUserEntities: [{ constellationId: 0 }],
    },
    {
      id: 1,
      name: '고먐미',
      contourResponse: { thumbUrl: '/public/images/search-test/14.jpg' },
      constellationUserEntities: [{ constellationId: 1 }],
    },
    {
      id: 2,
      name: '몰라',
      contourResponse: { thumbUrl: '/public/images/search-test/16.jpg' },
      constellationUserEntities: [{ constellationId: 2 }],
    },
  ];
  return (
    <div className="constellation-modal-container">
      <div className="constellation-row-container">
        {array.map((constellation) => (
          <ConstellationRow
            key={constellation.id}
            title={constellation.name}
            constellationThumb={constellation.contourResponse.thumbUrl}
            constellationId={constellation.constellationUserEntities[0].constellationId}
            // constellThumb={constellation.contourResponse.thumbUrl}
          />
        ))}
      </div>
      <div className="constellation_main_buttons_box">
        <img
          src="/images/constellation-modal/constellation_create_button.png"
          alt="post_create_button"
          className="constellation-main-button"
          onClick={openConstellaionWriting}
        />
        <img
          src="/images/constellation-modal/post_create_button.png"
          alt="post_create_button"
          className="constellation-main-button"
          // onClick={openConstellationModal}
        />
      </div>
    </div>
  );
}
