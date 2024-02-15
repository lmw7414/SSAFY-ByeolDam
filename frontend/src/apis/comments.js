import axios from './axios';

export const getComments = async (articleId) => {
  const { data } = await axios.get(`/articles/${articleId}/comments`);
  return { resultCode: data.resultCode, data: data.result.content };
};

export const addComments = async ({ parentId = null, articleId, content }) => {
  const { data } = axios.post(`articles/${articleId}/comments`, {
    parentId,
    content,
  });
  return data;
};

export const editComments = async ({ articleId, commentId, content }) => {
  const { data } = axios.put(`articles/${articleId}/comments/${commentId}`, {
    content,
  });

  return { resultCode: data.resultCode };
};

export const deleteComments = async ({ articleId, commentId }) => {
  const { data } = axios.delete(`articles/${articleId}/comments/${commentId}`);

  return { resultCode: data.resultCode };
};
