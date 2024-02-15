import { useState } from 'react';
import EditMyProfile from './EditMyProfile';

import axios from '/src/apis/axios';

export default function MyProfile({
  profileData,
  changePage,
  accountSettingsId,
  setProfileUpdate,
  setProfileData,
}) {
  // if (!profileData) {
  //   return null;
  // }
  const [uploadedImage, setUploadedImage] = useState(null);

  const toEdit = () => {
    changePage(1);
  };

  const onChangeImage = async (e) => {
    const file = e.target.files[0];
    const imageUrl = URL.createObjectURL(file);

    try {
      const formData = new FormData();
      formData.append('imageFile', file);

      await axios.put('users/profile-image', formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      });
      setProfileUpdate(1);
      window.location.reload();
      // setProfileData({ ...profileData, imageUrl: imageUrl });
    } catch (error) {
      console.error('Error updating profile image:', error);
    }
  };

  return (
    <div className="my-profile-container">
      <h1 className="nickname settings-title">계정</h1>
      <div className="notification-settings-menu">
        <h2 className="nickname">프로필 사진</h2>
        <div className="account-profile-image-box">
          <div className="account-profile-image-container">
            <img className="account-profile-image" src={profileData.imageUrl} alt="profile_image" />
          </div>
          <div>
            <label htmlFor="file">
              <div className="update-profile-button">사진 변경</div>
            </label>
            <input
              type="file"
              name="file"
              id="file"
              onChange={onChangeImage}
              className="display-none"
            ></input>
          </div>
        </div>
      </div>

      <hr className="settings-detail-divide" />

      <div className="notification-settings-menu">
        <h2 className="nickname">내 정보</h2>

        {accountSettingsId ? (
          <EditMyProfile
            changePage={changePage}
            profileData={profileData}
            setProfileUpdate={setProfileUpdate}
            setProfileData={setProfileData}
          />
        ) : (
          <div className="account-profile-image-box">
            <div className="flex-col-box">
              <div className="my-profile-box">
                <p className="profile-info-field body-1-1">닉네임</p>
                <div>
                  <p className="profile-info-value body-1-1">{profileData.nickname}</p>
                </div>
              </div>
              <div className="my-profile-box">
                <p className="profile-info-field body-1-1">이메일</p>
                <div>
                  <p className="profile-info-value body-1-1">{profileData.email}</p>
                </div>
              </div>
              <div className="my-profile-box">
                <p className="profile-info-field body-1-1">이름</p>
                <div>
                  <p className="profile-info-value body-1-1">{profileData.name}</p>
                </div>
              </div>
              <div className="my-profile-box">
                <p className="profile-info-field body-1-1">생일</p>
                <div>
                  <p className="profile-info-value body-1-1">{profileData.birthday}</p>
                </div>
              </div>
              <div className="my-profile-box">
                <p className="profile-info-field body-1-1">소개글</p>
                <div>
                  <p className="profile-info-value body-1-1">{profileData.memo}</p>
                </div>
              </div>
              <div className="my-profile-box">
                <p className="profile-info-field body-1-1">공개범위</p>
                <div>
                  <p className="profile-info-value body-1-1">
                    {profileData.disclosureType === 'VISIBLE' ? '공개' : '비공개'}
                  </p>
                </div>
              </div>
            </div>
            <button className="update-profile-button" onClick={toEdit}>
              내 정보 수정
            </button>
          </div>
        )}
      </div>

      <hr className="settings-detail-divide" />
    </div>
  );
}
