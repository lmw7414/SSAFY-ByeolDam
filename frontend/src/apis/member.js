import axios from './axios';

export const login = async ({ email, password }) => {
  try {
    const result = await axios.post('/users/login', {
      email,
      password,
    });

    sessionStorage.setItem('token', result?.token);

    axios.interceptors.request.use(
      (config) => {
        config.headers['Authorization'] = `Bearer ${result.token}`;
        return config;
      },
      (e) => {
        return Promise.reject(e);
      },
    );

    console.log(result);

    return result;
  } catch (e) {
    console.error(e);
  }
};

export const join = async ({ email, password, name, nickname }) => {
  try {
    const result = await axios.post('/users/join', {
      email,
      password,
      name,
      nickname,
    });

    console.log(result);

    sessionStorage.setItem('userInfo', result);
  } catch (e) {
    console.error(e);
  }
};
