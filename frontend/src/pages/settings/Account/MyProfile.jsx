export default function MyProfile({ profileData, changePage }) {
  // if (!profileData) {
  //   return null; // 또는 로딩 상태를 나타내는 다른 JSX를 반환할 수 있습니다.
  // }
  const toEdit = () => {
    changePage(1);
  };
  return (
    <div>
      <h2>내 프로필</h2>
      <div className="content-box">
        <div className="profile-image-container">
          <img className="profile-image" src="/images/space.png" alt="profile_image" />
        </div>
        <div>
          <label>
            닉네임
            <p className="nickname">{profileData.nickname}</p>
          </label>
        </div>
        <div>
          <label>
            이메일
            <p className="nickname">{profileData.email}</p>
          </label>
        </div>
        <div>
          <label>
            이름
            <p className="nickname">{profileData.name}</p>
          </label>
        </div>
        <div>
          <label>
            생일
            <p className="nickname">{profileData.birthday}</p>
          </label>
        </div>
        <div>
          <label>
            메모
            <p className="nickname">{profileData.memo}</p>
          </label>
        </div>
        <div>
          <label>
            공개범위
            <p className="nickname">{profileData.disclosureType}</p>
          </label>
        </div>

        <button className="profile-image-edit" onClick={toEdit}>
          내 정보 수정
        </button>
      </div>
    </div>
  );
}
