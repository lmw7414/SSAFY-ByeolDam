import axios from 'axios';

const failedRequestQueue = [];
let isRefreshing = false;

const client = axios.create({
  baseURL: '/api',
  withCredentials: true,
});

export const setToken = (token) => {
  sessionStorage.setItem('access_token', token);
  client.defaults.headers.Authorization = token;
};

export const deleteToken = () => {
  sessionStorage.clear('access_token');
  client.defaults.headers.Authorization = null;
};

export default client;
