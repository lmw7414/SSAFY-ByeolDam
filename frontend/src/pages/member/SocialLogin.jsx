import { useEffect } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import axios, { setToken } from '../../apis/axios';
import parseJwt from '../../utils/parseJwt';

export default function SocialLogin() {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();

  useEffect(() => {
    const token = searchParams.get('token');

    axios.interceptors.request.use(
      (config) => {
        config.headers['Authorization'] = `Bearer ${token}`;
        return config;
      },
      (e) => {
        return Promise.reject(e);
      },
    );

    setToken(token);
    sessionStorage.setItem('profile', 'nickname:immigrant_co');
    navigate('/home');
  }, []);

  return <div></div>;
}
