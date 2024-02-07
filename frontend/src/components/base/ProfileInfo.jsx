export default function ProfileInfo({ text, num, onClick }) {
  return (
    <div className="profile-info" onClick={onClick}>
      <div className="profile-info-text button-light">
        <p>{text}</p>
      </div>
      <div className="profile-info-num button-bold">
        <p>{num}</p>
      </div>
    </div>
  );
}
