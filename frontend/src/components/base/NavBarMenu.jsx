export default function NavBarMenu({ text, src, alt, onClick, barId }) {
  return (
    <div className="nav-bar-menu" onClick={onClick}>
      <div className="nav-bar-menu-icon-frame">
        <img className="nav-bar-menu-icon" src={src} alt={alt} />
      </div>
      <p
        className={
          barId === 4 ? 'nav-bar-menu-text-selected button-light' : 'nav-bar-menu-text button-light'
        }
      >
        {text}
      </p>
    </div>
  );
}
