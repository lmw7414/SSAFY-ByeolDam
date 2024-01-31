import { useState } from 'react';

export default function NavBar() {
  const [isNavBarVisible, setIsNavBarVisible] = useState(true);

  const ToggleClick = () => {
    setIsNavBarVisible(!isNavBarVisible);
  };

  return (
    <div className={`nav-bar-big-container ${isNavBarVisible ? '' : 'hidden'}`}>
      <div className="nav-bar-container">
        <div className="nav-bar-logo">
          <img src="/src/assets/images/temporary-logo.png" alt="logo" />
        </div>

        <div className="nav-bar-profile-box">
          <div className="profile-image-box">
            <div className="profile-image-container">
              <img
                className="profile-image"
                src="/src/assets/images/space.png"
                alt="profile_image"
              />
            </div>
          </div>
          <div className="nickname title">
            <p>상당히느긋한사람</p>
          </div>
          <div className="profile-info-box">
            <div className="profile-info">
              <div className="profile-info-text button-light">
                <p>별</p>
              </div>
              <div className="profile-info-num button-bold">
                <p>24</p>
              </div>
            </div>
            <div className="profile-info">
              <div className="profile-info-text button-light">
                <p>별자리</p>
              </div>
              <div className="profile-info-num button-bold">
                <p>24</p>
              </div>
            </div>
            <div className="profile-info">
              <div className="profile-info-text button-light">
                <p>팔로워</p>
              </div>
              <div className="profile-info-num button-bold">
                <p>24</p>
              </div>
            </div>
            <div className="profile-info">
              <div className="profile-info-text button-light">
                <p>팔로잉</p>
              </div>
              <div className="profile-info-num button-bold">
                <p>24</p>
              </div>
            </div>
          </div>
        </div>

        <hr className="nav-bar-divide-line" />

        <div className="nav-bar-menu-box">
          <div className="nav-bar-menu">
            <div className="nav-bar-menu-icon-box">
              <img
                className="nav-bar-menu-icon"
                src="/src/assets/images/nav-bar-menu-icons/search.png"
                alt="search_icon"
              />
            </div>
            <p className="nav-bar-menu-text button-light">검색</p>
          </div>
          <div className="nav-bar-menu">
            <div className="nav-bar-menu-icon-box">
              <img
                className="nav-bar-menu-icon"
                src="/src/assets/images/nav-bar-menu-icons/feed.png"
                alt="feed_icon"
              />
            </div>
            <p className="nav-bar-menu-text button-light">피드</p>
          </div>
          <div className="nav-bar-menu">
            <div className="nav-bar-menu-icon-box">
              <img
                className="nav-bar-menu-icon"
                src="/src/assets/images/nav-bar-menu-icons/notification.png"
                alt="notification_icon"
              />
            </div>
            <p className="nav-bar-menu-text button-light">알림</p>
          </div>
          <div className="nav-bar-menu">
            <div className="nav-bar-menu-icon-box">
              <img
                className="nav-bar-menu-icon"
                src="/src/assets/images/nav-bar-menu-icons/settings.png"
                alt="settings_icon"
              />
            </div>
            <p className="nav-bar-menu-text button-light">설정</p>
          </div>
          <div className="nav-bar-menu">
            <div className="nav-bar-menu-icon-box">
              <img
                className="nav-bar-menu-icon"
                src="/src/assets/images/nav-bar-menu-icons/logout.png"
                alt="logout_icon"
              />
            </div>
            <p className="nav-bar-menu-text button-light">로그아웃</p>
          </div>
        </div>

        <div className="nav-bar-toggle-container" onClick={ToggleClick}>
          <div className="nav-bar-menu-icon-box">
            <img
              src={
                isNavBarVisible
                  ? '/src/assets/images/nav-bar-toggle-button/close.png'
                  : '/src/assets/images/nav-bar-toggle-button/open.png'
              }
              alt="toggle_button"
              className="toggle-button"
            />
          </div>
        </div>
      </div>
    </div>
  );
}
