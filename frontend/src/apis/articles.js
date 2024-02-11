import axios from './axios';

export const getArticles = async (articleId) => {
  const { data } = await axios.get(`articles/${articleId}`);
  return { resultCode: data.resultCode, data: data.articles };
};

export const addArticle = async (
  { description, articleHashtagSet, disclosureType, imageType },
  file,
) => {
  const formData = new FormData();

  const request = new Blob(
    [
      JSON.stringify({
        description,
        articleHashtagSet,
        disclosureType,
        imageType,
        title: description,
      }),
    ],
    { type: 'application/json' },
  );

  formData.append('request', request);
  formData.append('imageFile', file);

  const { data } = await axios.post('articles', formData, {
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  });

  return { resultCode: data.resultCode };
};

export const deleteArticle = async (articleId) => {
  const { data } = await axios.delete(`articles/${articleId}`);
  return { resultCode: data.resultCode };
};

export const editArticle = async ({ articleId, title, disclosure, tag, description }) => {
  const { data } = await axios.put(`articles/${articleId}`, {
    title,
    disclosure,
    tag,
    description,
  });

  return { resultCode: data.resultCode };
};
