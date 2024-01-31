export default function NavBar() {
  return (
    <div className="nav-bar-container">
      <div className="nav-bar-logo">
        <img src="/src/assets/images/temporary-logo.png" alt="logo" />
      </div>
      <div className="nav-bar-profile-box">
        <div className="profile-image-box">
          <div className="profile-image-container">
            <img className="profile-image" src="/src/assets/images/space.png" alt="profile_image" />
          </div>
        </div>
        <div className="nickname">
          <p>상당히느긋한사람</p>
        </div>
        <div className="profile-info-box">
          <div className="profile-info">
            <div class="profile-info-text button-light">
              <p>별</p>
            </div>
            <div class="profile-info-num button-bold">
              <p>24</p>
            </div>
          </div>
          <div className="profile-info">
            <div class="profile-info-text button-light">
              <p>별자리</p>
            </div>
            <div class="profile-info-num button-bold">
              <p>24</p>
            </div>
          </div>
          <div className="profile-info">
            <div class="profile-info-text button-light">
              <p>팔로워</p>
            </div>
            <div class="profile-info-num button-bold">
              <p>24</p>
            </div>
          </div>
          <div className="profile-info">
            <div class="profile-info-text button-light">
              <p>팔로잉</p>
            </div>
            <div class="profile-info-num button-bold">
              <p>24</p>
            </div>
          </div>
        </div>
      </div>

      <hr />

      <div className="nav-bar-menu-box">
        <div className="nav-bar-menu">
          <img
            className="nav-bar-menu-icon"
            src="/src/assets/images/nav-bar-menu-icons/search.png"
            alt="search_icon"
          />
          <p className="nav-bar-menu-text button-light">검색</p>
        </div>
        <div className="nav-bar-menu">
          <img
            className="nav-bar-menu-icon"
            src="/src/assets/images/nav-bar-menu-icons/feed.png"
            alt="feed_icon"
          />
          <p className="nav-bar-menu-text button-light">피드</p>
        </div>
        <div className="nav-bar-menu">
          <img
            className="nav-bar-menu-icon"
            src="/src/assets/images/nav-bar-menu-icons/notification.png"
            alt="notification_icon"
          />
          <p className="nav-bar-menu-text button-light">알림</p>
        </div>
        <div className="nav-bar-menu">
          <img
            className="nav-bar-menu-icon"
            src="/src/assets/images/nav-bar-menu-icons/settings.png"
            alt="settings_icon"
          />
          <p className="nav-bar-menu-text button-light">설정</p>
        </div>
        <div className="nav-bar-menu">
          <img
            className="nav-bar-menu-icon"
            src="/src/assets/images/nav-bar-menu-icons/logout.png"
            alt="logout_icon"
          />
          <p className="nav-bar-menu-text button-light">로그아웃</p>
        </div>
      </div>
    </div>
  );
}
