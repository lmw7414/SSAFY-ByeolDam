import axios from '../../apis/axios';
import React, { useState } from 'react';

export default function SignupTest() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [name, setName] = useState('');
  const [nickname, setNickname] = useState('');

  const signUp = async (event) => {
    event.preventDefault();
    try {
      const data = await axios.post('/users/join', {
        email,
        password,
        name,
        nickname,
      });
      console.log(data);
    } catch (e) {
      console.error(e);
    }
  };

  return (
    <div>
      <h1 className="signupText">회원가입</h1>
      <form className="signupForm" onSubmit={signUp}>
        <input
          type="text"
          placeholder="이메일"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        />
        <input
          type="password"
          placeholder="비밀번호"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />
        <input
          type="text"
          placeholder="이름"
          value={name}
          onChange={(e) => setName(e.target.value)}
        />
        <input
          type="text"
          placeholder="닉네임"
          value={nickname}
          onChange={(e) => setNickname(e.target.value)}
        />
        <button type="submit">회원가입</button>
      </form>
    </div>
  );
}
