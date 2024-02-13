export default function NotificationSettings() {
  return (
    <div className="settings-page-container">
      <h1 className="nickname settings-title">알림</h1>
      <div className="notification-settings-menu">
        <h2 className="nickname">푸시 알림</h2>
        <div className="notification-menu-box">
          <div className="toggle-slide-box">
            <h3 className="nickname">모두 일시 중단</h3>
            <div className="toggle">
              <input type="checkbox" id="toggle-slide" />
              <label for="toggle-slide">on/off</label>
            </div>
          </div>
        </div>
      </div>
      <hr className="settings-detail-divide" />

      <div className="notification-settings-menu">
        <h2 className="nickname">좋아요</h2>
        <div className="notification-menu-box">
          <div className="radio-box">
            <input type="radio" name="like-alarm" />
            <p className="nickname">해제</p>
          </div>
          <div className="radio-box">
            <input type="radio" name="like-alarm" />
            <p className="nickname">내가 팔로우 하는 사용자</p>
          </div>
          <div className="radio-box">
            <input type="radio" name="like-alarm" />
            <p className="nickname">모든 사람</p>
          </div>
        </div>
      </div>
      <hr className="settings-detail-divide" />

      <div className="notification-settings-menu">
        <h2 className="nickname">댓글</h2>
        <div className="notification-menu-box">
          <div className="radio-box">
            <input type="radio" name="comment-alarm" />
            <p className="nickname">해제</p>
          </div>
        </div>
      </div>
    </div>
  );
}
