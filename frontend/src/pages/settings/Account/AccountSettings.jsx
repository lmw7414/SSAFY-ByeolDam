import React, { useState, useEffect } from 'react';

import axios from '../../../apis/axios';

import MyProfile from './MyProfile';

export default function AccountSettings() {
  const [accountSettingsId, setAccountSettingsId] = useState(0);
  const [profileUpdate, setProfileUpdate] = useState(0);
  const [profileData, setProfileData] = useState({
    imageUrl: '',
    nickname: '',
    email: '',
    name: '',
    birthday: '',
    memo: '',
    disclosureType: '',
  });

  const changePage = (newId) => {
    setAccountSettingsId(newId);
  };

  const readProfile = async () => {
    const profileStr = sessionStorage.getItem('profile');
    const profile = JSON.parse(profileStr);
    const data = await axios.get(`/users/${profile.nickname}`);
    setProfileData(data.data.result);
    setProfileUpdate(0);
  };

  useEffect(() => {
    readProfile();
  }, [profileUpdate]);

  return (
    <div className="account-settings-container">
      <div className="content-container">
        <MyProfile
          profileData={profileData}
          accountSettingsId={accountSettingsId}
          changePage={changePage}
          setProfileUpdate={setProfileUpdate}
          setProfileData={setProfileData}
        />
      </div>
    </div>
  );
}
