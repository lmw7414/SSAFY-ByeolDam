import React from 'react';

export default function ConstellationSelectBox({ thumbnail, name, selected, setSelected, index }) {
  return (
    <div
      className={`constellation-select-box ${selected === index ? 'constellation-select-box-selected' : ''}`}
      onClick={setSelected}
    >
      <img src={thumbnail} />
      <div>{name}</div>
    </div>
  );
}
