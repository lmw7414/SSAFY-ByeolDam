import axios from './axios';

export const getAIcontours = async (imageFile) => {
  const formData = new FormData();
  formData.append('imageFile', imageFile);
  const { data } = await axios.post('/ai', formData, {
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  });

  return data.result.result;
};

export const addConstellation = async ({
  name,
  description,
  origin,
  thumb,
  cthumb,
  contoursList,
  ultimate,
}) => {
  const formData = new FormData();

  const request = new Blob(
    [
      JSON.stringify({
        name,
        description,
      }),
    ],
    { type: 'application/json' },
  );

  const contoursListBlob = new Blob([JSON.stringify(contoursList)], { type: 'application/json' });
  const ultimateBlob = new Blob([JSON.stringify(ultimate)], { type: 'application/json' });

  formData.append('request', request);
  formData.append('origin', origin);
  formData.append('thumb', thumb);
  formData.append('cthumb', cthumb);
  formData.append('contoursList', contoursListBlob);
  formData.append('ultimate', ultimateBlob);

  const result = await axios.post('/constellations', formData, {
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  });

  return result;
};

export const getMyConstellations = async (nickname) => {
  const { data: unLabeledList } = await axios.get('/articles/constellation');
  const { data: labeledList } = await axios.get(`/constellations/user/${nickname}`);

  return { unLabeledList: unLabeledList.result, labeledList: labeledList.result };
};

export const getConstellationContour = async (constellationId) => {
  const { data } = await axios.post(`/constellations/${constellationId}/request-contour`);

  return data;
};

export const getUserUniverse = async (nickname) => {
  const { data } = await axios.get(`/constellations/user/${nickname}`);

  return data;
};
