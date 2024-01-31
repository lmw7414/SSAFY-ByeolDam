import { Route } from 'wouter';

import Member from './pages/member/Member';
import Universe from './pages/Universe';
import NavBar from './components/NavBar';

import './assets/styles/scss/main.scss';
import ModalContainer from './components/modal/ModalContainer';

export default function App() {
  return (
    <div className="App">
      <NavBar />
      <Route path="/" component={Member} />
      <Route path="/home" component={Universe} />
      <ModalContainer />
    </div>
  );
}
