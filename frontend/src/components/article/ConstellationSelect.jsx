import { useState } from 'react';
import ConstellationSelectList from './ConstellationSelectList';

export default function ConstellationSelect({
  writeArticle,
  article,
  setArticle,
  setStep,
  isLoading,
}) {
  const setDisclosureType = (e) => {
    setArticle({
      ...article,
      disclosureType: e.target.value,
    });
  };

  return (
    <div className="constellation-select-container">
      <label className="constellation-select-label">별자리 선택</label>
      <ConstellationSelectList setArticle={setArticle} article={article} />
      <label className="constellation-select-label" htmlFor="disclosure">
        공개범위 선택
      </label>
      <div className="disclosure-select-box">
        <label className="disclosure-input-label" htmlFor="constellation-visible">
          <input
            id="constellation-visible"
            name="disclosure"
            type="radio"
            value="VISIBLE"
            onChange={setDisclosureType}
            checked={article.disclosureType === 'VISIBLE'}
          />
          <div>공개</div>
        </label>
        <label className="disclosure-input-label" htmlFor="constellation-invisible">
          <input
            id="constellation-invisible"
            name="disclosure"
            type="radio"
            value="INVISIBLE"
            onChange={setDisclosureType}
            checked={article.disclosureType === 'INVISIBLE'}
          />
          <div>비공개</div>
        </label>
      </div>
      <div className="article-button-wrapper">
        <div
          type="button"
          className="article-prev-btn"
          onClick={() => {
            setStep(1);
          }}
        >
          <img src="/images/nav-bar-toggle-button/close.png" />
        </div>
        <button
          className={`article-write-btn ${isLoading ? 'article-write-btn-loading' : ''}`}
          type="button"
          onClick={writeArticle}
        >
          별 생성하기
        </button>
      </div>
    </div>
  );
}
