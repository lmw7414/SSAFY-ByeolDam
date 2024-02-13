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
