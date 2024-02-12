import React from 'react';

export default function ConstellationSelectBox({
  thumbnailUrl,
  name,
  selected,
  setSelected,
  index,
}) {
  return (
    <div
      className={`constellation-select-box ${selected === index ? 'constellation-select-box-selected' : ''}`}
      onClick={setSelected}
    >
      <img src={thumbnailUrl} />
      <div>{name}</div>
    </div>
  );
}
