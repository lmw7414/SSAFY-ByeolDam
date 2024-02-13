import React from 'react';
import ChildrenComment from './ChildrenComment';

export default function Comment({ id, nickName, content, createAt, parentId, childrenComments }) {
  return (
    <div className='comment-box'>
      <div id="comment">
        <p className="nickname comment-nickname">{nickName}</p>
        <div id="comment-describe">
          <p className="nickname">{content}</p>
          {/* <button>수정</button> */}
          {/* <button>삭제</button> */}
        </div>
        <div>
          {childrenComments.map((comment) => (
            <ChildrenComment
            id={comment.id}
            nickName={comment.nickName}
            content={comment.content}
            createAt={comment.createAt}
            parentId={id}
            />
            ))}
        </div>
      </div>
      <hr className="settings-detail-divide"/>
    </div>
  );
}
