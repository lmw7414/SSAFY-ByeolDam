import React from 'react'

export default function ChildrenComment({ id, parentId, nickName, content, createAt }) {
  return (
    <div style={{ display: 'flex', flexDirection: 'row', paddingLeft: '20px', marginTop:'2px'}}>
      <div className='child-comment-header'>
        <div className='child-comment-nickname'>{nickName}</div>
        <div className='child-comment-content'>{content}</div>
      </div>
    </div>
  )
}
