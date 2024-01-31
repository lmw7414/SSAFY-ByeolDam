export default function ProfileInfo({ text, num }) {
  return (
    <div className="profile-info">
      <div className="profile-info-text button-light">
        <p>{text}</p>
      </div>
      <div className="profile-info-num button-bold">
        <p>{num}</p>
      </div>
    </div>
  );
}
