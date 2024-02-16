import { BrowserRouter as Router, Routes, Route, useNavigate } from 'react-router-dom';

import Member from './pages/member/Member';
import Universe from './pages/Universe';
import NavBar from './components/NavBar';
import Settings from './pages/settings/Settings';

import './assets/styles/scss/main.scss';
import ModalContainer from './components/modal/ModalContainer';
import { ModalContext } from './hooks/useModal';
import { useEffect, useState } from 'react';
import ConstellationWriting from './pages/constellation/ConstellationWriting';
import SignUp from './pages/member/SignUp';
import SocialLogin from './pages/member/SocialLogin';
import SearchStar from './pages/search/SearchStar';
import SearchConstellation from './pages/search/SearchConstellation';
import FeedList from './pages/feed/FeedList';
import OtherUniverse from './pages/OtherUniverse';

export default function App() {
  return (
    <div className="App">
      <Router>
        <Routes>
          <Route path="/" element={<Member />} />
          <Route path="/login" element={<Member />} />
          <Route path="/signup" element={<SignUp />} />
          <Route path="/login/oauth2/code/:providerId" element={<SocialLogin />} />
          <Route path="/*" element={<NavApp />} />
        </Routes>
      </Router>
    </div>
  );
}

function NavApp() {
  const navigate = useNavigate();
  const [isLogin, setIsLogin] = useState(false);
  const [isNavBarVisible, setIsNavBarVisible] = useState(true);
  const [modalState, setModalState] = useState({
    isOpen: false,
    children: null,
    title: '',
  });
  const [nickname, setNickname] = useState(null);

  useEffect(() => {
    if (isLogin) return;
    if (sessionStorage['profile'] && sessionStorage['access_token']) {
      setIsLogin(true);
      setNickname(JSON.parse(sessionStorage['profile']).nickname);
    } else {
      alert('로그인이 필요한 페이지입니다');
      navigate('/');
    }
  }, [isLogin]);

  return (
    <>
      {isLogin ? (
        <ModalContext.Provider value={{ modalState, setModalState }} className="provider">
          <NavBar
            isNavBarVisible={isNavBarVisible}
            setIsNavBarVisible={setIsNavBarVisible}
            nickname={nickname}
            setNickname={setNickname}
          />
          <Routes>
            <Route
              path="/home/*"
              element={
                <Universe
                  isNavBarVisible={isNavBarVisible}
                  setIsNavBarVisible={setIsNavBarVisible}
                />
              }
            />
            <Route
              path="/universe/:nickname"
              element={<OtherUniverse setNickname={setNickname} />}
            />
            <Route path="/settings" element={<Settings />} />
            <Route path="/search/star" element={<SearchStar />} />
            <Route path="/search/constellation" element={<SearchConstellation />} />
            <Route
              path="/constellation"
              element={<ConstellationWriting isNavBarVisible={isNavBarVisible} />}
            />
            <Route path="/feed" element={<FeedList />}></Route>
          </Routes>
          <ModalContainer modalState={modalState} isNavBarVisible={isNavBarVisible} />
        </ModalContext.Provider>
      ) : (
        <div></div>
      )}
    </>
  );
}
