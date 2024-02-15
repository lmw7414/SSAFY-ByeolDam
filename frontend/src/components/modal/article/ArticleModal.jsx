import { useEffect, useState } from 'react';
import Comment from '../../article/Comment';
import { addComments } from '../../../apis/comments';
import { getUserUniverse } from '../../../apis/constellation';
import { changeConstellationId } from '../../../apis/articles';

export default function ArticleModal({
  articleId,
  description,
  hits,
  createdAt,
  imgUrl,
  owner,
  tags,
  constellationName,
  commentList,
  constellationId,
}) {
  const [comments, setComments] = useState(commentList || []);
  const [constellation, setConstellation] = useState(constellationName);
  const [liked, setLiked] = useState(false);
  const [content, setContent] = useState('');
  const [constellationList, setConstellationList] = useState([]);
  const [newConstellationId, setNewConstellationId] = useState(constellationId);

  const nickname = JSON.parse(sessionStorage.profile).nickname;

  useEffect(() => {
    if (owner === nickname) {
      getUserUniverse(nickname).then(({ result }) => {
        setConstellationList(result);
      });
    }
  }, []);

  const createComment = (e) => {
    e.preventDefault();
    setContent(content.trim());
    addComments({ articleId, content }).then((result) => {});
    setContent('');
  };

  const selectConstellationId = (e) => {
    setNewConstellationId(e.target.value ? e.target.value : null);
  };

  const changeConsteallationId = () => {
    changeConstellationId({ articleId, constellationId: newConstellationId }).then(() => {
      constellationId = newConstellationId;
    });
  };

  const likeCount = 1;

  return (
    <div className="modal-box">
      <div className="article-img-box">
        <img src={imgUrl} alt="사용자 게시물 이미지" />
        {owner === nickname && (
          <div className="select-constellation-box">
            <div>현재 속한 별자리:</div>
            <select onChange={selectConstellationId} defaultValue={constellationId || ''}>
              <option value={''}>미분류</option>
              {constellationList.map(({ id, name }) => {
                return (
                  <option key={id} value={id}>
                    {name}
                  </option>
                );
              })}
            </select>
            {constellationId != newConstellationId && (
              <button type="button" onClick={changeConsteallationId}>
                적용하기
              </button>
            )}
          </div>
        )}
      </div>

      <div className="modal-right-box">
        <div className="modal-describe-box">
          <p className="nickname article-owner">{owner}</p>
          <p className="nickname article-description">{description}</p>
          <div id="tags">
            {tags.map((tag) => (
              <p key={tag} className="nickname">
                {tag}
              </p>
            ))}
          </div>
          <div id="like-area">
            <p className="nickname">좋아요 {likeCount}개</p>
            <img
              src={liked ? '/images/heart_activated.png' : '/images/heart.png'}
              alt="좋아요"
              onClick={() => {
                setLiked(!liked);
              }}
            />
          </div>
        </div>

        <hr className="settings-detail-divide" />
        <div id="comment-area">
          {comments.map((comment) => (
            <Comment
              id={comment.id}
              nickName={comment.nickName}
              content={comment.content}
              createAt={comment.createAt}
              parentId={comment.parentId}
              childrenComments={comment.childrenComments}
            />
          ))}
        </div>

        <div className="create-comment-area">
          <textarea
            type="text"
            value={content}
            onChange={(e) => setContent(e.target.value)}
            name="content"
            id="content"
            placeholder="댓글 입력"
            onKeyUp={(e) => {
              if (e.key === 'Enter') {
                createComment(e);
              }
            }}
          />
          <img src="/images/comment_create_btn.png" alt="댓글 생성 버튼" onClick={createComment} />
        </div>
      </div>
    </div>
  );
}
