export default function UserBox({
  followSubject,
  profileImg,
  nickname,
  constellationCounts,
  articleCounts,
}) {
  return (
    <div className="user-box">
      <div className="user-box-left">
        <div className="follow-modal-profile-img-container">
          <img className="follow-modal-profile-image" src={profileImg} alt="" />
        </div>
        <p className="real-nickname">{nickname}</p>
      </div>
      <div className="user-box-right">
        <div className="icon-num-box">
          <img
            src="/images/search-bar-filter-icons/constellation_activated.png"
            alt="constellation-icon"
            className="user-box-icon"
          />
          <p className="user-box-num">{constellationCounts}</p>
        </div>
        <div className="icon-num-box">
          <img
            src="/images/search-bar-filter-icons/star_activated.png"
            alt="star-icon"
            className="user-box-icon"
          />
          <p className="user-box-num">{articleCounts}</p>
        </div>
        <button className="follow-modal-button">
          {followSubject === 'following' ? '팔로잉' : '삭제'}
        </button>
      </div>
    </div>
  );
}
