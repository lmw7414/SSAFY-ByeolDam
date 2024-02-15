import {useState} from 'react'
import ChildrenComment from './ChildrenComment'
import { addComments } from '../../apis/comments.js';

export default function Comment({ id, nickName, content, createdAt, parentId, childrenComments, articleId }) {
  const [openChildComment, setOpenChildComment] = useState(false);
  const [openCommentBox, setOpenCommentBox] = useState(false);
  const [comment, setComment] = useState('');

  const createChildComment = (parentId, e) => {
    e.preventDefault();
    if(!comment.trim()) return;
    addComments({parentId, content:comment, articleId}).then((result) => {
      setComment('');
    });
    
  };

  return (
    <div style={{ display: 'flex', flexDirection: 'column', marginBottom: '10px'}}>
      {childrenComments && childrenComments.length > 0 ? (
        <div>
          <div className='feed-comment-header'>
          <div className='feed-comment-nickname'>{nickName}</div>
          <div className='feed-comment-content'>{content}</div>
        </div>

        {!openChildComment && (
          <div onClick={() => setOpenChildComment(true)} style={{paddingLeft:'20px', color: '#c7bed6'}}>답글 더 보기</div>
        )}

        {openChildComment &&
          (childrenComments.map(({ id, nickName, content, childrenComments }) => (
          <ChildrenComment
            key={id}
            nickName={nickName}
            content={content}
            childrenComments={childrenComments}
          />
          ))
        )}
        {openChildComment && !openCommentBox && <div onClick={()=>setOpenCommentBox(true)} style={{paddingLeft:'20px', color:'#c7bed6'}}>답글 작성하기</div>}
        {openCommentBox && <div style={{paddingLeft: '20px'}}><div className='create-feed-comment-area'>
            <textarea 
              type="text"
              name="content" 
              id="content"
              value={comment}
              onChange={(e) => setComment(e.target.value)}
              placeholder='댓글 입력'
              onKeyUp={(e) => {
                if (e.key === 'Enter') {
                  createChildComment(id, e);
                }
              }}
            />
            <img src="images/comment_create_btn.png" alt="댓글 생성 버튼" onClick={(e)=>{createChildComment(id, e)}} />
          </div></div>}
    </div>
    ) : (
    <div>
      <div className='feed-comment-header'>
        <div className='feed-comment-nickname'>{nickName}</div>
        <div className='feed-comment-content'>{content}</div>
      </div>
      {!openChildComment && !openCommentBox && (
        <div onClick={()=>setOpenCommentBox(true)} style={{paddingLeft:'20px', color:'#c7bed6'}}>답글 작성하기</div>
      )}
      {!openChildComment && openCommentBox && <div style={{paddingLeft: '20px'}}><div className='create-feed-comment-area'>
            <textarea 
              type="text"
              name="content" 
              id="content"
              value={comment}
              onChange={(e) => setComment(e.target.value)}
              placeholder='댓글 입력'
              onKeyUp={(e) => {
                if (e.key === 'Enter') {
                  createChildComment(id, e);
                }
              }}
            />
            <img src="images/comment_create_btn.png" alt="댓글 생성 버튼" onClick={(e)=>{createChildComment(id, e)}} />
          </div></div>}
    </div>
    )}
  </div>
  )
}
