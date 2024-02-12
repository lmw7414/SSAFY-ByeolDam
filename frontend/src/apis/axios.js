import axios from 'axios';

const failedRequestQueue = [];
let isRefreshing = false;

const client = axios.create({
  baseURL: '/api',
  withCredentials: true,
});

client.interceptors.request.use(
  (config) => {
    const accessToken = sessionStorage['access_token'];
    config.headers['Authorization'] = `Bearer ${accessToken}`;

    return config;
  },
  (error) => {
    console.log(error);
    return Promise.reject(error);
  },
);

export const setToken = (token) => {
  sessionStorage.setItem('access_token', token);
  client.defaults.headers.Authorization = token;
};

export const deleteToken = () => {
  sessionStorage.clear('access_token');
  client.defaults.headers.Authorization = null;
};

export default client;
