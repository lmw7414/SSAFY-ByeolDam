const getLoginUrl = (providerId) =>
  `${import.meta.env.VITE_OUATH_URL}/${providerId}?redirect_uri=${import.meta.env.VITE_GOOGLE_REDIRECT_URI}`;

export const googleLogin = () => {
  window.location.href = getLoginUrl('google');
};

export const naverLogin = () => {
  window.location.href = getLoginUrl('naver');
};

export const kakaoLogin = () => {
  window.location.href = getLoginUrl('kakao');
};
