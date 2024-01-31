import { Route, useLocation } from 'wouter';

import Member from './pages/member/Member';
import Universe from './pages/Universe';
import NavBar from './components/NavBar';

import './assets/styles/scss/main.scss';
import ModalContainer from './components/modal/ModalContainer';

export default function App() {
  const [isNotNav] = useLocation();

  return (
    <div className="App">
      {isNotNav === '/' ? null : <NavBar />}
      <Route path="/" component={Member} />
      <Route path="/home" component={Universe} />
      <ModalContainer />
    </div>
  );
}
