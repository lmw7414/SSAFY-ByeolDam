import React, { useState, useEffect } from 'react';

import axios from '../../../apis/axios';
import parseJwt from '../../../utils/parseJwt.js';

import MyProfile from './MyProfile';

export default function AccountSettings() {
  const [accountSettingsId, setAccountSettingsId] = useState(0);
  const [profileUpdate, setProfileupdate] = useState(0);
  const [profileData, setProfileData] = useState({
    nickname: '',
    email: '',
    name: '',
    birthday: '',
    memo: '',
    disclosureType: '',
  });

  const changePage = (newId) => {
    setAccountSettingsId(newId);
    // console.log(accountSettingsId);
  };

  // const readProfile = async () => {
  //   const JWTtoken = sessionStorage.getItem('access_token');
  //   const parsed = parseJwt(JWTtoken);
  //   const { data } = await axios.get(`/users/${parsed.nickname}`);
  //   console.log(nickname);
  //   sessionStorage.setItem('userInfo', data);
  //   return data;
  // };
  const readProfile = async () => {
    const profileStr = sessionStorage.getItem('profile');
    const profile = JSON.parse(profileStr);
    setProfileData(profile);
  };

  useEffect(() => {
    readProfile();
  }, [profileUpdate]);

  return (
    <div className="account-settings-container">
      <div className="content-container">
        <MyProfile
          profileData={profileData}
          changePage={changePage}
          accountSettingsId={accountSettingsId}
        />
      </div>
    </div>
  );
}
