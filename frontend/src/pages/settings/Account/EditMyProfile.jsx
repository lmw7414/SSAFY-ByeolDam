import { useState, useEffect } from 'react';
import axios from '/src/apis/axios';

export default function EditMyProfile({ profileData, changePage, setProfileupdate }) {
  const [editedProfile, setEditedProfile] = useState({ ...profileData });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setEditedProfile((prevProfile) => ({ ...prevProfile, [name]: value }));
  };

  const handleSubmit = async (e) => {
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
  console.log(editedProfile);
  return (
    <div>
      <h2>내 프로필 수정</h2>
      <div className="content-box">
        <form onSubmit={handleSubmit}>
          <div className="profile-image-container">
            <img className="profile-image" src="/src/assets/images/space.png" alt="profile_image" />
          </div>
          <button>프로필 사진 변경</button>
          <div>
            <label>
              닉네임
              <input
                type="text"
                name="nickname"
                value={editedProfile.nickname}
                onChange={handleChange}
              />
            </label>
          </div>
          <div>
            <label>
              이메일
              <input type="text" name="email" value={editedProfile.email} disabled />
            </label>
          </div>
          <div>
            <label>
              비밀번호 수정
              <input
                type="text"
                name="password"
                // value={editedProfile.password}
                onChange={handleChange}
              />
            </label>
          </div>
          <div>
            <label>
              이름
              <input type="text" name="name" value={editedProfile.name} onChange={handleChange} />
            </label>
          </div>
          <div>
            <label>
              생일
              <input
                type="date"
                name="birthday"
                value={editedProfile.birthday}
                onChange={handleChange}
              />
            </label>
          </div>

          <div>
            <label>
              메모
              <input type="text" name="memo" value={editedProfile.memo} onChange={handleChange} />
            </label>
          </div>
          <div>
            <label>
              공개 범위
              <select
                name="disclosureType"
                value={editedProfile.disclosureType}
                onChange={handleChange}
              >
                <option value="VISIBLE">공개</option>
                <option value="INVISIBLE">비공개</option>
              </select>
            </label>
          </div>
          <div>
            <button type="submit" className="profile-image-edit">
              적용
            </button>
          </div>
          <div>
            <button type="button" onClick={() => changePage(0)}>
              [임시]프로필로가기
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
