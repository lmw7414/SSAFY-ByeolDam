import React from 'react';

export default function ConstellationSelectBox({ thumbnail, name, id, article, setSelected }) {
  return (
    <div
      className={`constellation-select-box ${article.constellationId === id ? 'constellation-select-box-selected' : ''}`}
      onClick={setSelected}
    >
      <div className="constellation-select-box-thumbnail-container">
        <img src={thumbnail} className="constellation-select-box-thumbnail" />
      </div>
      <div>{name}</div>
    </div>
  );
}
