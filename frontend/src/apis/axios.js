import axios from 'axios';

let failedRequestQueue = [];
let isRefreshing = false;

const client = axios.create({
  baseURL: import.meta.env.VITE_BASE_URL,
  withCredentials: true,
});

client.interceptors.request.use(
  (config) => {
    const accessToken = sessionStorage['access_token'];
    if (accessToken) config.headers['Authorization'] = `Bearer ${accessToken}`;

    return config;
  },
  (e) => {
    return Promise.reject(e);
  },
);

export const setToken = (token) => {
  sessionStorage.setItem('access_token', token);
  client.defaults.headers.Authorization = `Bearer ${token}`;
};

export const setProfile = (profile) => {
  const prevProfile = sessionStorage.profile;
  sessionStorage.setItem('profile', { ...prevProfile, profile });
};

export const deleteToken = () => {
  sessionStorage.clear('access_token');
  client.defaults.headers.Authorization = null;
};

const addFailedRequest = (request) => {
  failedRequestQueue.push(request);
};

export const refreshToken = async () => {
  const { data } = await client.get('/users/refresh');
  sessionStorage.setItem('profile', JSON.stringify(data.result.user));
  return data.result.token;
};

const refreshTokenAndResolveRequests = async (error) => {
  try {
    const { response: errorResponse } = error;

    const failedRequest = new Promise((resolve, reject) => {
      addFailedRequest(async (accessToken) => {
        try {
          errorResponse.config.headers['Authorization'] = `Bearer ${accessToken}`;
          resolve(client(errorResponse.config));
        } catch (e) {
          reject(e);
        }
      });
    });

    if (!isRefreshing) {
      isRefreshing = true;

      const token = await refreshToken();
      setToken(token);
      failedRequestQueue.forEach((request) => request(token));
      failedRequestQueue = [];
    }

    return failedRequest;
  } catch (e) {
    // if (sessionStorage['access_token']) sessionStorage.clear('access_token');
    // if (sessionStorage['profile']) sessionStorage.clear('profile');
    return Promise.reject(e);
  }
};

client.interceptors.response.use(
  function (response) {
    return response;
  },
  async function (error) {
    const { response: errorResponse } = error;
    if (errorResponse.status === 401) {
      return await refreshTokenAndResolveRequests(error);
    }

    return Promise.reject(error);
  },
);

export default client;
