export default function Member() {
  return (
    <div className="member-box">
      <div className="member-logo-box">
        <img src="/src/assets/images/temporary-logo.png" className="member-box-logo" alt="logo" />
      </div>
      <div className="member-socials-box">
        <p className="body-1">소셜 계정으로 로그인</p>
        <div className="social-logos">
          <img className="social-logo" src="/src/assets/images/socials/naver.png" alt="" />
          <img className="social-logo" src="/src/assets/images/socials/kakao.png" alt="" />
          <img className="social-logo" src="/src/assets/images/socials/google.png" alt="" />
        </div>
      </div>
      <div className="member-button-box">
        <p>계정이 없으신가요?</p>
        <button type="button" className="member-button">
          소셜 회원가입
        </button>
      </div>
    </div>
  );
}
