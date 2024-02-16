import { useEffect, useState, useRef } from 'react';
import Comment from '../../article/Comment';
import { addComments, getComments } from '../../../apis/comments';
import { getUserUniverse } from '../../../apis/constellation';
import { changeConstellationId, getLikeCount, postLike } from '../../../apis/articles';

export default function ArticleModal({
  articleId,
  description,
  hits,
  createdAt,
  imgUrl,
  owner,
  tags,
  commentList,
  constellationId,
}) {
  const [comments, setComments] = useState(commentList || []);
  const [constellationName, setConstellationName] = useState('');
  const [liked, setLiked] = useState(false);
  const [likeCount, setLikeCount] = useState(0);
  const [content, setContent] = useState('');
  const [constellationList, setConstellationList] = useState([]);
  const [newConstellationId, setNewConstellationId] = useState(constellationId);
  const isFirstCall = useRef();

  const nickname = JSON.parse(sessionStorage.profile).nickname;

  useEffect(() => {
    if (owner === nickname) {
      getUserUniverse(nickname).then(({ result }) => {
        setConstellationList(result);
      });
    }

    getComments(articleId).then(({ data }) => {
      setComments(data);
    });

    getLikeCount(articleId).then(({ data }) => {
      setLikeCount(data);
    });
  }, []);

  const createComment = (e) => {
    e.preventDefault();
    setContent(content.trim());
    addComments({ articleId, content }).then((result) => {
      setTimeout(() => {
        getComments(articleId).then(({ data }) => {
          setComments(data);
        });
      }, 200);
    });
    setContent('');
  };

  const selectConstellationId = (e) => {
    setNewConstellationId(e.target.value ? e.target.value : null);
  };

  const changeConsteallationId = () => {
    changeConstellationId({ articleId, constellationId: newConstellationId }).then(() => {
      alert('별자리를 옮겼습니다');
      constellationId = newConstellationId;
    });
  };

  useEffect(() => {
    if (isFirstCall.current) {
      postLike(articleId).then(() => {
        setTimeout(() => {
          getLikeCount(articleId).then(({ data }) => {
            setLikeCount(data);
          });
        }, 200);
      });
    } else {
      isFirstCall.current = true;
    }
  }, [liked]);

  useEffect(() => {
    constellationList.forEach(({ id, name }) => {
      if (id === constellationId) setConstellationName(name);
    });
  }, [constellationList]);

  return (
    <div>
      <div className="article-modal-top">
        <p className="article-description">{description}</p>
        <p className="article-owner">{owner}</p>
      </div>
      <div className="modal-box">
        <div className="article-img-box">
          <img src={imgUrl} alt="사용자 게시물 이미지" />
        </div>

        <div className="modal-right-box">
          <div className="modal-describe-box">
            <div id="tags">{tags.join(' ')}</div>
            <p id="like-area">
              좋아요 {likeCount}개
              <img
                src={liked ? '/images/colored_heart.png' : '/images/empty_heart.png'}
                alt="좋아요"
                onClick={() => {
                  setLiked(!liked);
                }}
              />
            </p>
          </div>

          <hr className="settings-detail-divide" />
          <div id="comment-area">
            {comments.length === 0 ? (
              <p className="article-modal-comment">게시물의 첫 번째 댓글을 달아주세요.</p>
            ) : null}
            {comments.map((comment) => (
              <Comment
                key={comment.id}
                id={comment.id}
                nickName={comment.nickName}
                content={comment.content}
                createAt={comment.createAt}
                parentId={comment.parentId}
                childrenComments={!!comment.childrenComments && comment.childrenComments}
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
            <img
              src="/images/comment_create_btn.png"
              alt="댓글 생성 버튼"
              onClick={createComment}
            />
          </div>
        </div>
      </div>
      {owner === nickname && (
        <div className="select-constellation-box">
          <div>현재 속한 별자리:</div>
          <select onChange={selectConstellationId}>
            <option value={''}>미분류</option>
            {constellationList.map(({ id, name }) => {
              return (
                <option key={id} value={id} selected={id === constellationId}>
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
  );
}
