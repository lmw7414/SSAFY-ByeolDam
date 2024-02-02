import { Route, useLocation } from 'wouter';

import Member from './pages/member/Member';
import Universe from './pages/Universe';
import NavBar from './components/NavBar';
import Settings from './pages/settings/Settings';

import './assets/styles/scss/main.scss';
import ModalContainer from './components/modal/ModalContainer';
import { ModalContext } from './hooks/useModal';
import { useEffect, useState } from 'react';

export default function App() {
  const [location] = useLocation();
  const [modalState, setModalState] = useState({
    isOpen: false,
    children: null,
    title: '',
  });

  return (
    <div className="App">
      <ModalContext.Provider value={{ modalState, setModalState }} className="provider">
        {location === '/' ? null : <NavBar />}
        <ModalContainer modalState={modalState} />
        <Route path="/" component={Member} />
        <Route path="/home" component={Universe} />
        <Route path="/settings" component={Settings} />
      </ModalContext.Provider>
    </div>
  );
}
