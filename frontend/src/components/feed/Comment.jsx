import {useState} from 'react'
import ChildrenComment from './ChildrenComment'

export default function Comment({ id, nickName, content, createdAt, parentId, childrenComments }) {
  const [openChildComment, setOpenChildComment] = useState(false);

  console.log("comment id: ", id);
  console.log("comment nickName: ", nickName);
  console.log("comment content: ", content);
  console.log("comment createdAt: ", createdAt);

  return (
    <div style={{ display: 'flex', flexDirection: 'column'}}>
      <div className='comment-header'>
        <div className='comment-nickname'>{nickName}</div>
        <div className='comment-content'>{content}</div>
      </div>
      <div>
  {openChildComment ? (
    childrenComments && childrenComments.length > 0 ? (
      childrenComments.map(({ id, nickName, content, childrenComments }) => (
        <ChildrenComment
          key={id}
          nickName={nickName}
          content={content}
          childrenComments={childrenComments}
        />
      ))
    ) : null
  ) : (
    childrenComments && childrenComments.length === 0 ? (
      null
    ) : (
      <div onClick={() => setOpenChildComment(true)}>
        답글 더 보기
      </div>
    )
  )}
</div>
    </div>
  )
}
