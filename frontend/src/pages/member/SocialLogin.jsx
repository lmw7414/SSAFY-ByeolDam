import { useEffect } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { setToken, refreshToken } from '../../apis/axios';

export default function SocialLogin() {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();

  useEffect(() => {
    const token = searchParams.get('token');
    setToken(token);
    refreshToken().then(() => {
      navigate('/home');
    });
  }, []);

  return <div></div>;
}
