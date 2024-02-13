import { useState, useEffect } from 'react';
import axios from '/src/apis/axios';

export default function EditMyProfile({
  profileData,
  changePage,
  setProfileupdate,
  setProfileData,
}) {
  const [editedProfile, setEditedProfile] = useState({ ...profileData });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setEditedProfile((prevProfile) => ({ ...prevProfile, [name]: value }));
  };

  const editProfile = async (e) => {
    e.preventDefault();

    try {
      const response = await axios.put('/users', editedProfile);
      setProfileupdate(1);
      console.log('내 정보 수정 성공');
      changePage(0);
    } catch (error) {
      console.error('Error updating profile:', error);
    }
  };
  return (
    <form className="account-profile-image-box" onSubmit={editProfile}>
      <div className="flex-col-box">
        <div className="my-profile-box">
          <p className="profile-info-text body-1-1">닉네임</p>
          <input
            type="text"
            name="nickname"
            value={editedProfile.nickname}
            onChange={handleChange}
            className="edit-profile-input edit-profile-input-text"
          />
        </div>
        <div className="my-profile-box">
          <p className="profile-info-text body-1-1">이메일</p>
          <input
            type="text"
            name="email"
            value={editedProfile.email}
            onChange={handleChange}
            className="edit-profile-input edit-profile-input-text"
          />
        </div>
        <div className="my-profile-box">
          <p className="profile-info-text body-1-1">이름</p>
          <input
            type="text"
            name="name"
            value={editedProfile.name}
            onChange={handleChange}
            className="edit-profile-input edit-profile-input-text"
          />
        </div>
        <div className="my-profile-box">
          <p className="profile-info-text body-1-1">생일</p>
          <input
            type="date"
            name="birthday"
            value={editedProfile.birthday}
            onChange={handleChange}
            className="edit-profile-input edit-profile-input-text"
          />
        </div>
        <div className="my-profile-box">
          <p className="profile-info-text body-1-1">소개글</p>
          <input
            type="text"
            name="memo"
            value={editedProfile.memo}
            onChange={handleChange}
            className="edit-profile-input edit-profile-input-text"
          />
        </div>
        <div className="my-profile-box">
          <p className="profile-info-text body-1-1">공개범위</p>
          <select
            name="disclosureType"
            value={editedProfile.disclosureType}
            onChange={handleChange}
            className="edit-profile-input edit-profile-input-text"
          >
            <option value="VISIBLE">공개</option>
            <option value="INVISIBLE">비공개</option>
          </select>
        </div>
      </div>
      <div>
        <button type="submit" className="profile-image-edit">
          적용
        </button>
        <button type="button" onClick={() => changePage(0)}>
          [임시]프로필로가기
        </button>
      </div>
    </form>
  );
}
