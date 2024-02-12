import { useEffect, useState } from 'react';
import Tag from './Tag';

export default function TagEditor({ setTags }) {
  const [tagList, setTagList] = useState([]);
  const [selected, setSelected] = useState('');
  const [isWriting, setIsWriting] = useState();
  const [width, setWidth] = useState(3);

  const setInputWidth = (e) => {
    let sum = 0;
    const tagName = e.target.value;
    for (let i = 0; i < tagName.length; i++) {
      sum += encodeURI(tagName.charAt(i)).length > 1 ? 1.6 : 0.9;
    }
    setWidth(Math.max(3, sum));
  };

  const deleteTag = (tagName) => {
    setTagList(tagList.filter((tag) => tagName !== tag));
  };

  const addTage = (tagName) => {
    if (tagList.includes(tagName)) return;
    setTagList([...tagList, tagName]);
  };

  const addTagEnter = (e) => {
    if (e.code !== 'Enter') return;
    setIsWriting(false);
    let tagName = e.target.value.trim().replace('#', '');
    if (!tagName) return;
    addTage(`#${tagName}`);
    setIsWriting(true);
    e.target.value = '';
  };

  const addTagBlur = (e) => {
    setIsWriting(false);
    let tagName = e.target.value.trim().replace('#', '');
    if (!tagName) return;
    addTage(`#${tagName}`);
  };

  useEffect(() => {
    setTags(tagList);
  }, [tagList]);

  return (
    <div className="article-tag-list">
      {tagList.map((tagName) => (
        <Tag
          key={tagName}
          tagName={tagName}
          selected={selected}
          setSelected={setSelected}
          deleteTag={() => {
            deleteTag(tagName);
          }}
        />
      ))}
      {isWriting ? (
        <input
          autoFocus
          style={{ width: `min(calc(${width}ch + 1rem), 100%)` }}
          className="article-tag-insert-input"
          onChange={setInputWidth}
          onKeyUp={addTagEnter}
          onBlur={addTagBlur}
        />
      ) : (
        <div
          className="article-tag-insert-btn"
          onClick={() => {
            setIsWriting(true);
          }}
        >
          +
        </div>
      )}
    </div>
  );
}
