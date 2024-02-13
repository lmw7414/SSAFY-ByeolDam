import axios from '../../apis/axios';
import { useState, useEffect } from 'react';

import { checkEmail, checkNickname } from '../../apis/member';

export default function SignupTest() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [name, setName] = useState('');
  const [nickname, setNickname] = useState('');
  const [isNickNameDuplicated, setIsNickNameDuplicated] = useState(false);
  const [isEmailDuplicated, setIsEmailDuplicated] = useState(false);

  const signUp = async (event) => {
    event.preventDefault();
    try {
      const data = await axios.post('/users/join', {
        email,
        password,
        name,
        nickname,
      });
    } catch (e) {
      console.error(e);
    }
  };

  useEffect(() => {
    if (!nickname?.trim()) return;
    checkNickname(nickname).then(({ data }) => {
      setIsNickNameDuplicated(!data.result);
    });
  }, [nickname]);

  useEffect(() => {
    if (!email?.trim()) return;
    checkEmail(email).then(({ data }) => {
      setIsEmailDuplicated(!data.result);
    });
  }, [email]);

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
        {isEmailDuplicated && <div>이미 가입한 이메일입니다.</div>}
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
        {isNickNameDuplicated && <div>중복된 닉네임입니다</div>}
        <button type="submit">회원가입</button>
      </form>
    </div>
  );
}
