import axios from '../../../apis/axios';
import React, { useState, useEffect } from 'react';

import MyProfile from './MyProfile';
import EditMyProfile from './EditMyProfile';

export default function AccountSettings() {
  const [accountSettingsId, setAccountSettingsId] = useState(0);
  const [profileData, setProfileData] = useState({
    nickname: '',
    email: '',
    name: '',
    birthday: '',
    memo: '',
    disclosureType: '',
    password: '',
  });

  const readProfile = async () => {
    const { data } = await axios.get('/users/immigrant_co');
    return data;
  };

  const changePage = (newId) => {
    setAccountSettingsId(newId);
    console.log(accountSettingsId);
  };

  // 테스트용 객체
  // const profileData = {
  //   password: '1234',
  //   name: '뽱정민',
  //   nickname: '상느사',
  //   memo: '나는 자연인이다.',
  //   disclosureType: 'VISIBLE',
  //   birthday: '2024-02-05',
  // };

  useEffect(() => {
    readProfile().then(({ result }) => {
      // console.log(result);
      setProfileData(result);
    });
  }, []);

  return (
    <div className="account-settings-container">
      <h1 className="nickname">계정</h1>
      <div className="content-container">
        {accountSettingsId ? (
          <EditMyProfile profileData={profileData} changePage={changePage} />
        ) : (
          <MyProfile profileData={profileData} changePage={changePage} />
        )}
        <hr className="settings-detail-divide" />
      </div>
    </div>
  );
}
