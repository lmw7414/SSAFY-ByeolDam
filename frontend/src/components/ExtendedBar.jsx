// import { useState } from 'react';
export default function ExtendedBar({ changeBar, barId }) {
  const ToggleClick = () => {
    changeBar(0);
  };

  const openSearchBar = () => {
    changeBar(1);
  };
  const openNotificationBar = () => {
    changeBar(2);
  };

  return (
    <div className="extended-bar-big-container">
      <div className="extended-bar-container">
        <div className="extended-bar-logo">
          <img src="/src/assets/images/temporary-logo.png" alt="logo" />
        </div>

        {barId === 1 ? <input type="text" placeholder="검색" className="search-bar-input" /> : null}

        <hr className="extended-bar-divide-line" />

        <div className="extended-bar-menu-box">
          <div className="extended-bar-menu">
            <div className="extended-bar-menu-icon-box">
              <img
                className="extended-bar-menu-icon"
                src="/src/assets/images/nav-bar-menu-icons/search.png"
                alt="search_icon"
                onClick={openSearchBar}
              />
            </div>
          </div>
          <div className="extended-bar-menu">
            <div className="extended-bar-menu-icon-box">
              <img
                className="extended-bar-menu-icon"
                src="/src/assets/images/nav-bar-menu-icons/feed.png"
                alt="feed_icon"
              />
            </div>
          </div>
          <div className="extended-bar-menu">
            <div className="extended-bar-menu-icon-box">
              <img
                className="extended-bar-menu-icon"
                src="/src/assets/images/nav-bar-menu-icons/notification.png"
                alt="notification_icon"
                onClick={openNotificationBar}
              />
            </div>
          </div>
          <div className="extended-bar-menu">
            <div className="extended-bar-menu-icon-box">
              <img
                className="extended-bar-menu-icon"
                src="/src/assets/images/nav-bar-menu-icons/settings.png"
                alt="settings_icon"
              />
            </div>
          </div>
          <div className="extended-bar-menu">
            <div className="extended-bar-menu-icon-box">
              <img
                className="extended-bar-menu-icon"
                src="/src/assets/images/nav-bar-menu-icons/logout.png"
                alt="logout_icon"
              />
            </div>
          </div>
        </div>

        <div className="extended-bar-toggle-container" onClick={ToggleClick}>
          <div className="extended-bar-menu-icon-box">
            <img
              src="/src/assets/images/nav-bar-toggle-button/close.png"
              alt="toggle_button"
              className="toggle-button"
            />
          </div>
        </div>
      </div>
    </div>
  );
}
