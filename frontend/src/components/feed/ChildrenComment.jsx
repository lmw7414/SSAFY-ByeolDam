import React from 'react'

export default function ChildrenComment({ id, parentId, nickName, content, createAt }) {
  return (
    <div style={{ display: 'flex', flexDirection: 'row', paddingLeft: '10px' }}>
      <div className='comment-header'>
        <div className='comment-nickname'>{nickName}</div>
        <div className='comment-content'>{content}</div>
      </div>
    </div>
  )
}
