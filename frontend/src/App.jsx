import { Route, useLocation } from 'wouter';

import Member from './pages/member/Member';
import Universe from './pages/Universe';
import NavBar from './components/NavBar';

import './assets/styles/scss/main.scss';
import ModalContainer from './components/modal/ModalContainer';
import { ModalContext } from './hooks/useModal';
import { useState } from 'react';
import ConstellationListModal from './components/modal/ConstellationListModal';

export default function App() {
  const [location] = useLocation();
  const [modalState, setModalState] = useState({
    isOpen: false,
    children: null,
    title: '',
  });

  setModalState({
    isOpen: true,
    title: '별자리 리스트',
    children: <ConstellationListModal />,
  });

  return (
    <div className="App">
      <ModalContext.Provider value={{ modalState, setModalState }}>
        {location === '/' ? null : <NavBar />}
        <Route path="/" component={Member} />
        <Route path="/home" component={Universe} />
        <ModalContainer modalState={modalState} />
      </ModalContext.Provider>
    </div>
  );
}
