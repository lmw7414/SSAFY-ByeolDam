import React from 'react';

export default function ConstellationSelectBox({ thumbnail, name, id, article, setSelected }) {
  return (
    <div
      className={`constellation-select-box ${article.constellationId === id ? 'constellation-select-box-selected' : ''}`}
      onClick={setSelected}
    >
      <img src={thumbnail} />
      <div>{name}</div>
    </div>
  );
}
