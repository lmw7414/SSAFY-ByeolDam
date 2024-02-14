import { useEffect, useState } from 'react';
import Comment from '../../article/Comment';
import axios from '../../../apis/axios';
import { addComments, getComments } from '../../../apis/comments';

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
}) {
  const [comments, setComments] = useState(commentList);
  const [constellation, setConstellation] = useState(constellationName);
  const [liked, setLiked] = useState(false);
  const [content, setContent] = useState('');
  const [profile, setProfile] = useState({});
  const [constellationList, setConstellationList] = useState([]);

  const readProfile = async () => {
    const profileStr = sessionStorage.getItem('profile');
    const profile = JSON.parse(profileStr);
    const data = await axios.get(`/users/${profile.nickname}`);
    setProfile(data.data.result);
  };

  const readConstellationList = async () => {
    try {
      const response = await axios.get(`/constellations/user/${profile.nickname}`);
      const data = response.data;
      // console.log('데이터 가져오기 성공:', data);
    } catch (error) {
      // console.log('데이터 가져오기 실패:', error);
    }
  };

  useEffect(() => {
    readProfile();
    readConstellationList();
  }, []);

  const createComment = (e) => {
    e.preventDefault();
    setContent(content.trim());
    addComments({articleId, content}).then((result) => {
      console.log('댓글이 생성되었습니다:', content);
    });
    setContent('');
  };

  const likeCount = 1;

  return (
    <div className="modal-box">
      <div className="article-img-box">
        <img src={imgUrl} alt="사용자 게시물 이미지" />
        <p className="nickname">
          현재 속한 별자리:
          <li>
            {constellation}
            <i class="down"></i>
            <div class="dropdown">
              <a class="drop-link" href="#">
                Link1
              </a>
              <a class="drop-link" href="#">
                Link2
              </a>
              <a class="drop-link" href="#">
                Link3
              </a>
            </div>
          </li>
        </p>
      </div>

      <div className="modal-right-box">
        <div className="modal-describe-box">
          <p className="nickname article-owner">{owner}</p>
          <div id="tags">
            {tags.map((tag) => (
              <p key={tag} className="nickname">
                {tag}
              </p>
            ))}
          </div>
          <div id="like-area">
            <img
              src={liked ? '/images/heart_activated.png' : '/images/heart.png'}
              alt="좋아요"
              onClick={() => {
                setLiked(!liked);
              }}
            />
            <p className="nickname">좋아요 {likeCount}개</p>
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
          <img src="images/comment_create_btn.png" alt="댓글 생성 버튼" onClick={createComment} />
        </div>
      </div>
    </div>
  );
}
