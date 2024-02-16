import { useState, useEffect } from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';

import { logout } from '../apis/member';
import useModal from '../hooks/useModal';

import ExtendedBar from './ExtendedBar';
import ProfileInfo from './base/ProfileInfo';
import NavBarMenu from './base/NavBarMenu';
import FollowModal from './modal/Follow/FollowModal';

import axios from '../apis/axios';

export default function NavBar({ isNavBarVisible, setIsNavBarVisible, nickname, setNickname }) {
  const [barId, setBarId] = useState(0);
  const [navEx, setNavEx] = useState(0);
  const [modalState, setModalState] = useModal();
  const [profile, setProfile] = useState({});

  // 네비게이션 바 작동 관련
  const navigate = useNavigate();
  const location = useLocation();

  const goHome = () => {
    const myNickName = JSON.parse(sessionStorage.profile).nickname;

    navigate('/home');
    setNickname(myNickName);
    setBarId(0);
    setNavEx(0);
  };

  const goFeed = () => {
    navigate('/feed');
    setBarId(2);
  };

  const closeNavBar = () => {
    setIsNavBarVisible(!isNavBarVisible);
  };
  const openSearchBar = () => {
    setBarId(1);
    setNavEx(1);
  };
  const openNotificationBar = () => {
    setBarId(3);
    setNavEx(1);
  };
  const changeBar = (newId) => {
    setBarId(newId);
  };
  const changeExNav = (newId) => {
    setNavEx(newId);
  };

  const openSettingsPage = () => {
    setBarId(4);
  };

  const readProfile = async (nickname) => {
    const data = await axios.get(`/users/${nickname}`);
    setProfile(data.data.result);
  };

  useEffect(() => {
    readProfile(nickname);
  }, [nickname]);

  // 팔로우 모달
  const openFollowerModal = () => {
    setModalState({
      isOpen: true,
      title: '팔로워',
      children: <FollowModal followSubject={'follower'} modalState={modalState} />,
    });
  };

  const openFollowingModal = () => {
    setModalState({
      isOpen: true,
      title: '팔로잉',
      children: <FollowModal followSubject={'following'} modalState={modalState} />,
    });
  };

  return (
    <div>
      {navEx ? (
        <ExtendedBar
          changeBar={changeBar}
          barId={barId}
          changeExNav={changeExNav}
          navEx={navEx}
          goHome={goHome}
        />
      ) : (
        <div className={`nav-bar-big-container ${isNavBarVisible ? '' : 'nav-bar-hidden'}`}>
          <div className="nav-bar-logo" onClick={goHome}>
            <img src="/images/temporary-logo.png" alt="logo" />
          </div>
          <div className="nav-bar-container">
            <div className="nav-bar-contents">
              <div className="nav-bar-profile-box">
                <div className="profile-image-box">
                  <div className="profile-image-container">
                    <img className="profile-image" src={profile.imageUrl} alt="profile_image" />
                  </div>
                </div>
                <div className="nickname-memo-container">
                  <p className="real-nickname">{profile.nickname}</p>
                  <p className="memo">{profile.memo}</p>
                </div>

                <div className="profile-info-box">
                  <ProfileInfo text={'별'} num={profile.articleCounts} />

                  <ProfileInfo text={'별자리'} num={profile.constellationCounts} />

                  <ProfileInfo
                    text={'팔로워'}
                    num={profile.followers}
                    onClick={openFollowerModal}
                    isFollowInfo={true}
                  />

                  <ProfileInfo
                    text={'팔로잉'}
                    num={profile.followings}
                    onClick={openFollowingModal}
                    isFollowInfo={true}
                  />
                </div>
              </div>

              <div className="nav-bar-divide-line-box">
                <hr className="nav-bar-divide-line" />
              </div>

              <div className="nav-bar-menu-box">
                <NavBarMenu
                  text={'검색'}
                  src={'/images/nav-bar-menu-icons/search.png'}
                  alt={'search'}
                  onClick={openSearchBar}
                />
                <NavBarMenu
                  text={'피드'}
                  src={'/images/nav-bar-menu-icons/feed.png'}
                  alt={'feed'}
                  onClick={goFeed}
                />
                <NavBarMenu
                  text={'알림'}
                  src={'/images/nav-bar-menu-icons/notifications.png'}
                  alt={'notifications'}
                  onClick={openNotificationBar}
                />
                <Link to={'/settings'} className="link-setting">
                  <NavBarMenu
                    text={'설정'}
                    src={
                      barId === 4
                        ? '/images/nav-bar-menu-icons/settings_activated.png'
                        : '/images/nav-bar-menu-icons/settings.png'
                    }
                    alt={'settings'}
                    onClick={openSettingsPage}
                    barId={barId}
                  />
                </Link>
                <NavBarMenu
                  text={'로그아웃'}
                  src={'/images/nav-bar-menu-icons/logout.png'}
                  alt={'logout'}
                  onClick={() => {
                    logout();
                    navigate('/');
                  }}
                />
              </div>
            </div>
          </div>

          <div
            className="nav-bar-toggle-container"
            onClick={location.pathname === '/settings' ? null : closeNavBar}
          >
            <div className="nav-bar-menu-icon-box">
              <img
                src={
                  isNavBarVisible
                    ? '/images/nav-bar-toggle-button/close.png'
                    : '/images/nav-bar-toggle-button/open.png'
                }
                alt="toggle_button"
                className="toggle-button"
              />
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
