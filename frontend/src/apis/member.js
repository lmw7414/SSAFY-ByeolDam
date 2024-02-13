import axios from './axios';

export const login = async ({ email, password }) => {
  const { data } = await axios.post('/users/login', {
    email,
    password,
  });

  const token = data?.result?.token;
  sessionStorage.setItem('access_token', token);
  sessionStorage.setItem('profile', JSON.stringify(data.result.user));

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

export const signup = async ({ email, password, name, nickname }) => {
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

export const verificateEmail = async (email) => {
  const result = await axios.post('/email/verification-request', { email });
  return result;
};

export const verificateCode = async ({ email, code }) => {
  const result = await axios.get(`/email/verification?email=${email}&code=${code}`);
  return result;
};

export const getMyFollwings = async () => {
  if (!sessionStorage.token) return;
  const result = await axios.get('/me/followings');
};

export const logout = async () => {
  // cosnt result = await axios.post('/users/logout');
  if (sessionStorage['access_token']) sessionStorage.clear('access_token');
  if (sessionStorage['profile']) sessionStorage.clear('profile');
};
