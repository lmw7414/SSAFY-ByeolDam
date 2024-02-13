import React from 'react';

export default function ChildrenComment({ id, parentId, nickName, content, createAt }) {
  return (
    <div className="children-comment-box">
      <div id="comment">
        <p className="nickname comment-nickname">{nickName}</p>
        <div id="comment-describe">
          <p className="nickname">{content}</p>
          {/* <button>수정</button>
      <button>삭제</button> */}
        </div>
      </div>
      {/* <hr className="children-comment-hr" /> */}
    </div>
  );
}
