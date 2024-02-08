import { useEffect } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';

export default function SocialLogin() {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();

  useEffect(() => {
    const token = searchParams.get('token');
    console.log(token);
    navigate('/home');
  }, []);

  return <div></div>;
}
