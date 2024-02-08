import axios from '../../../apis/axios';
import React, { useState, useEffect } from 'react';

import MyProfile from './MyProfile';
import EditMyProfile from './EditMyProfile';

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
    password: '',
  });

  const changePage = (newId) => {
    setAccountSettingsId(newId);
    console.log(accountSettingsId);
  };

  const readProfile = async () => {
    const { nickname } = JSON.parse(sessionStorage.getItem('userInfo'));
    const { data } = await axios.get(`/users/${nickname}`);
    console.log(nickname);
    sessionStorage.setItem('userInfo', data);
    return data;
  };

  useEffect(() => {
    readProfile().then(({ result }) => {
      setProfileData(result);
      setProfileupdate(0);
    });
  }, [profileUpdate]);

  return (
    <div className="account-settings-container">
      <h1 className="nickname">계정</h1>
      <div className="content-container">
        {accountSettingsId ? (
          <EditMyProfile
            profileData={profileData}
            changePage={changePage}
            setProfileupdate={setProfileupdate}
          />
        ) : (
          <MyProfile profileData={profileData} changePage={changePage} />
        )}
        <hr className="settings-detail-divide" />
      </div>
    </div>
  );
}
