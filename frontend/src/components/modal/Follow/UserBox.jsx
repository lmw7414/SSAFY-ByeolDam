export default function UserBox({ followSubject, profileImg, nickname }) {
  return (
    <div>
      <div className="follow-modal-profile-img-container">
        <img className="follow-modal-profile-image" src={profileImg} alt="" />
      </div>
      <p>{nickname}</p>
      <div>
        <img src="" alt="" />

        <img src="" alt="" />

        <button>{followSubject === 'following' ? '팔로잉' : '삭제'}</button>
      </div>
    </div>
  );
}
