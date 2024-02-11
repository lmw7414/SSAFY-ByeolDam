import React from 'react';

export default function Tag({ tagName, selected, setSelected, deleteTag }) {
  return (
    <div
      className={`article-tag-btn ${selected === tagName ? 'article-tag-selected' : ''}`}
      onMouseEnter={() => {
        setSelected(tagName);
      }}
      onMouseLeave={() => {
        setSelected('');
      }}
    >
      <div>{tagName}</div>
      {selected === tagName && (
        <div className="articel-tag-delete-btn" onClick={deleteTag}>
          x
        </div>
      )}
    </div>
  );
}
