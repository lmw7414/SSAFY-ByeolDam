import axios from './axios';

export const getArticles = async (articleId) => {
  const { data } = await axios.get(`articles/${articleId}`);
  return data;
};
export const addArticle = async (
  { description, articleHashtagSet, disclosureType, imageType, constellationId },
  file,
) => {
  const formData = new FormData();

  const requestObject =
    constellationId === -1
      ? { description, articleHashtagSet, disclosureType, imageType, title: description }
      : {
          description,
          articleHashtagSet,
          disclosureType,
          imageType,
          constellationId,
          title: description,
        };

  const request = new Blob([JSON.stringify(requestObject)], { type: 'application/json' });

  formData.append('request', request);
  formData.append('imageFile', file);

  const result = await axios.post(
    constellationId === -1 ? '/articles/no-constellation' : '/articles',
    formData,
    {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    },
  );

  return result;
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

export const getFeeds = async () => {
  const { data } = await axios.get(`articles/follow`);
  return { resultCode: data.resultCode, data: data.result.content };
};

export const getProfileImage = async (nickName) => {
  const { data } = await axios.get(`${nickName}/request-profile`);
  return { resultCode: data.resultCode, data: data.result };
};

export const changeConstellationId = async ({ constellationId, articleId }) => {
  const { data } = await axios.post(`/articles/constellation-select/${constellationId}`, {
    articleIdSet: [articleId],
  });

  return data;
};

export const getLikeCount = async (articleId) => {
  const { data } = await axios.get(`articles/${articleId}/likeCount`);
  return { resultCode: data.resultCode, data: data.result };
};

export const postLike = async (articleId) => {
  const data = await axios.post(`articles/${articleId}/likes`);
  return data;
};

export const getLike = async (articleId) => {
  const { data } = await axios.get(`articles/${articleId}/likes`);
  return { resultCode: data.resultCode, data: data.result };
};
