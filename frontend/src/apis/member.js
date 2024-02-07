import axios from './axios';

export const login = async ({ email, password }) => {
  const { data } = await axios.post('/users/login', {
    email,
    password,
  });

  sessionStorage.setItem('token', data?.result?.token);

  axios.interceptors.request.use(
    (config) => {
      config.headers['Authorization'] = `Bearer ${data?.result?.token}`;
      return config;
    },
    (e) => {
      return Promise.reject(e);
    },
  );
};

export const join = async ({ email, password, name, nickname }) => {
  const result = await axios.post('/users/join', {
    email,
    password,
    name,
    nickname,
  });

  sessionStorage.setItem('userInfo', result);
  return result;
};

export const checkNickname = async (nickname) => {
  const result = await axios.post('/users/check-nickname', { nickname });
  return result;
};

export const checkEmail = async (email) => {
  const result = await axios.post('/users/check-email', { email });
  return result;
};

export const getMyFollwings = async () => {
  if (!sessionStorage.token) return;
  const result = await axios.get('/me/followings');
  console.log(result);
};
