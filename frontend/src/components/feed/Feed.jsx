import React, { useEffect, useState, useRef } from 'react';
import UserBox from '../feed/UserBox.jsx';
import Comment from '../feed/Comment.jsx';
import { addComments, getComments } from '../../apis/comments.js';
import { getLikeCount, postLike, getLike } from '../../apis/articles.js';
import { useThrottle } from '@uidotdev/usehooks';

export default function Feed({ feedData }) {
  const [like, setLike] = useState(false);
  const [initLike, setInitLike] = useState(false);
  const [comments, setComments] = useState([]);
  const [content, setContent] = useState('');
  const [likeCount, setLikeCount] = useState(0);
  const [showAllComments, setShowAllComments] = useState(false);
  const throttleValue = useThrottle(like);
  const isMounted = useRef(false);

  // 좋아요 버튼을 누르면 좋아요 post 요청
  // 댓글 열고 닫기
  useEffect(() => {
    getFeedComments();
    getLikeCnt();
    getLikeStatus(true);
  }, []);

  useEffect(() => {
    if (isMounted.current) {
      if (initLike === throttleValue) return;
      postLike(feedData.id).then(() => {
        getLikeStatus();
        getLikeCnt();
      });
    } else {
      isMounted.current = true;
    }
  }, [throttleValue]);

  const toggleLike = () => {
    setLike(!like);
  };

  const createComment = (articleId, e) => {
    e.preventDefault();
    setContent(content.trim());
    addComments({ articleId, content }).then((result) => {
      getFeedComments();
      setContent('');
    });
  };

  const getFeedComments = async () => {
    try {
      const { data } = await getComments(feedData.id);
      setComments([...data]);
    } catch (error) {
      console.error('Get Comments Failed', error);
    }
  };

  // 좋아요 개수를 가져오는 함수
  const getLikeCnt = async () => {
    try {
      const { data } = await getLikeCount(feedData.id);
      setLikeCount(data);
    } catch (error) {
      console.error('Get Liek Count Failed', error);
    }
  };

  // 좋아요 상태를 가져오는 함수
  const getLikeStatus = async (isInit) => {
    try {
      const { data } = await getLike(feedData.id);
      setInitLike(data);
      if (isInit) setLike(data);
    } catch (error) {
      console.error('Get Like Status Failed', error);
    }
  };

  const renderComments = () => {
    if (showAllComments) {
      return comments.map(({ id, nickName, content, createdAt, parentId, childrenComments }) => {
        return (
          <Comment
            key={id}
            id={id}
            nickName={nickName}
            content={content}
            createdAt={createdAt}
            parentId={parentId}
            childrenComments={childrenComments}
            articleId={feedData.id}
          />
        );
      });
    } else {
      // 댓글이 2개 이상이면 처음 2개의 댓글만 표시
      const limitedComments = comments.slice(0, 2);
      return limitedComments.map(
        ({ id, nickName, content, createdAt, parentId, childrenComments }) => {
          return (
            <Comment
              key={id}
              id={id}
              nickName={nickName}
              content={content}
              createdAt={createdAt}
              parentId={parentId}
              childrenComments={childrenComments}
              articleId={feedData.id}
            />
          );
        },
      );
    }
  };

  return (
    <div className="feed">
      <UserBox nickName={feedData.ownerEntityNickname}></UserBox>
      <div className="image-container">
        <img
          src={feedData.imageResponse.imageUrl}
          width={468}
          height={468}
          className="feed-img"
          alt={feedData.title}
        />
      </div>
      <div className="feed-content-container">
        <div className="feed-content-header">
          <div className="content-container">
            <div className="content-onwer">{feedData.ownerEntityNickname}</div>
            <div className="feed-content">{feedData.description}</div>
            <div className="feed-like">
              <div onClick={toggleLike} className="like-box">
                {like ? (
                  <img src="/images/Layer_1.png" alt="Liked" />
                ) : (
                  <img src="/images/empty_heart.png" alt="Not Liked" />
                )}
              </div>
              <div className="like-count">좋아요 {likeCount}개</div>
            </div>
          </div>
        </div>
        <div className="feed-tag">
          {feedData.articleHashtags.map((tag, index) => {
            return (
              <span key={index} className="tag">
                #{tag}
              </span>
            );
          })}
        </div>
        <hr />
        <div className="comment-container">
          {renderComments()}
          {comments.length > 2 && !showAllComments && (
            <div onClick={() => setShowAllComments(true)} style={{ color: '#c7bed6' }}>
              댓글 모두 보기
            </div>
          )}
        </div>
        <div className="create-feed-comment-area">
          <textarea
            type="text"
            name="content"
            id="content"
            value={content}
            onChange={(e) => setContent(e.target.value)}
            placeholder="댓글 입력"
            onKeyUp={(e) => {
              if (e.key === 'Enter') {
                createComment(feedData.id, e);
              }
            }}
          />
          <img src="images/comment_create_btn.png" alt="댓글 생성 버튼" onClick={createComment} />
        </div>
      </div>
    </div>
  );
}
