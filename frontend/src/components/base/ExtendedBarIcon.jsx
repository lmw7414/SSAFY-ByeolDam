export default function ExtendedBarIcon({ src, alt, onClick }) {
  return (
    <div className="extended-bar-icon-frame">
      <img className="extended-bar-icon" src={src} alt={alt} onClick={onClick} />
    </div>
  );
}
