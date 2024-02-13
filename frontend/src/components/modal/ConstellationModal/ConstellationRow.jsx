export default function ConstellationRow({ title }) {
  return (
    <div className="constellation-row">
      <div className="constellation-thumbnail-container">
        <h3>{title}</h3>
      </div>
      <div className="star-list-container">사진들</div>
    </div>
  );
}
