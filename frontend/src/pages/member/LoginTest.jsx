import axios from '../../apis/axios';
import React, { useEffect, useState } from 'react';

import { login } from '../../apis/member';

export default function LoginTest() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [isLogin, setIsLogin] = useState(false);

  const userLogin = (event) => {
    event.preventDefault();
    login({ email, password })
      .then(() => {
        setIsLogin(true);
        sessionStorage.setItem('userInfo', JSON.stringify({ nickname: 'test123' }));
      })
      .catch((e) => {
        alert('아이디 또는 비밀번호가 일치하지 않거나 존재하지 않는 아이디입니다.');
      });
  };

  useEffect(() => {
    setIsLogin(!!sessionStorage.token);
  }, []);

  return (
    <>
      {isLogin && <div>로그인된 상태입니다.</div>}
      {!isLogin && (
        <div>
          <h1 className="loginText">로그인</h1>
          <form className="loginForm" onSubmit={userLogin}>
            <input
              type="text"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              placeholder="아이디"
            />
            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="비밀번호"
            />
            <button type="submit">로그인</button>
          </form>
        </div>
      )}
    </>
  );
}
