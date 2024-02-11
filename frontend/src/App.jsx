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
import LoginTest from './pages/member/LoginTest';
import SignupTest from './pages/member/SignupTest';
import SocialLogin from './pages/member/SocialLogin';
import SearchStar from './pages/search/SearchStar';
import SearchConstellation from './pages/search/SearchConstellation';

export default function App() {
  return (
    <div className="App">
      <Router>
        <Routes>
          <Route path="/" element={<Member />} />
          <Route path="/login" element={<LoginTest />} />
          <Route path="/signup" element={<SignupTest />} />
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

  const [modalState, setModalState] = useState({
    isOpen: false,
    children: null,
    title: '',
  });

  useEffect(() => {
    if (sessionStorage['profile'] && sessionStorage['access_token']) setIsLogin(true);
    else {
      alert('로그인이 필요한 페이지입니다');
      navigate('/');
    }
  }, [isLogin]);

  return (
    <>
      {isLogin ? (
        <ModalContext.Provider value={{ modalState, setModalState }} className="provider">
          <NavBar />
          <Routes>
            <Route path="/home" element={<Universe />} />
            <Route path="/settings" element={<Settings />} />
            <Route path="/search/star" element={<SearchStar />} />
            <Route path="/search/constellation" element={<SearchConstellation />} />
            <Route path="/constellation" element={<ConstellationWriting />} />
          </Routes>
          <ModalContainer modalState={modalState} />
        </ModalContext.Provider>
      ) : (
        <div></div>
      )}
    </>
  );
}
