import { useState, useEffect } from 'react';

import ConstellationRow from './ConstellationRow';

import axios from '../../../apis/axios.js';
import parseJwt from '../../../utils/parseJwt.js';

export default function ConstellationModal() {
  const readConstellations = async () => {
    const JWTtoken = sessionStorage.getItem('token');
    // const userInfo = sessionStorage.getItem('userInfo');
    // console.log(JWTtoken);
    // console.log(userInfo);
    const parsed = parseJwt(JWTtoken);

    console.log(parsed);
    const profile = await axios.get(`/constellations/user/${parsed.email}`);
  };

  useEffect(() => {
    readConstellations();
  }, []);
  return (
    <div className="constellation-modal-container">
      <div className="">
        {/* map으로 별자리 목록 전부 출력 예정 */}
        <ConstellationRow />
        <ConstellationRow />
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
