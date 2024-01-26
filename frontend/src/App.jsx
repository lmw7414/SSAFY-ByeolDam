import { Route } from 'wouter';

import Login from './pages/Login';
import Universe from './pages/Universe';

import './assets/styles/main.scss';

export default function App() {
  return (
    <div className="App">
      <Route path="/" component={Login} />
      <Route path="/home" component={Universe} />
    </div>
  );
}
