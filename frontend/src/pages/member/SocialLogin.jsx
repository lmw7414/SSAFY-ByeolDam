import { useEffect } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import axios from '../../apis/axios';
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

    const profile = parseJwt(token);
    sessionStorage.setItem('access_token', token);
    sessionStorage.setItem('profile', JSON.stringify(profile));
    axios.get(`/users/${profile.nickname}`).then((data) => {
      console.log(data);
    });
    navigate('/home');
  }, []);

  return <div></div>;
}
