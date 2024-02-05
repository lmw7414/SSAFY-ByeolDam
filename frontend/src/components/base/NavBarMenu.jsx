export default function NavBarMenu({ text, src, alt, onClick }) {
  return (
    <div className="nav-bar-menu" onClick={onClick}>
      <div className="nav-bar-menu-icon-frame">
        <img className="nav-bar-menu-icon" src={src} alt={alt} />
      </div>
      <p className="nav-bar-menu-text button-light">{text}</p>
    </div>
  );
}
