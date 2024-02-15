import { useState } from 'react';
import { useEffect } from 'react';
import useModal from '../../hooks/useModal';
import axios from '../../apis/axios';
import { getComments } from '../../apis/comments';
import ArticleModal from '../../components/modal/article/ArticleModal';

export default function SearchResultBox({ data, filterId }) {
  const [cntLike, setCntLike] = useState(0);
  const [cntComment, setCntComment] = useState(0);
  const [modalState, setModalState] = useModal();

  useEffect(() => {
    // 게시물 검색
    if (filterId === 1) {
      const fetchData = async () => {
        const { data: commentsResult } = await getComments(data.id);
        setCntComment(commentsResult.length);
      };

      const getLikeCount = async (id) => {
        const { data: likeData } = await axios.get(`/articles/${id}/likeCount`);
        setCntLike(likeData.result);
      };

      getLikeCount(data.id);
      fetchData();
    } else if (filterId === 2) {
      const getLikeCount = async (id) => {
        const { data: likeData } = await axios.get(`/constellations/${id}/likeCount`);
        setCntLike(likeData.result);
      };

      getLikeCount(data.id);
    }
  }, []);

  const openArticleModal = (data) => {
    setModalState({
      isOpen: true,
      title: '게시물 상세',
      children: (
        <ArticleModal
          articleId={data.id}
          description={data.title}
          hits={data.hits}
          createdAt={data.createdAt}
          imgUrl={data.imageResponse.imageUrl}
          owner={data.ownerEntityNickname}
          tags={data.articleHashtags}
          commentList={data.commentList}
          constellationName={data.constellationEntityName}
          constellationId={data.constellationId}
        />
      ),
    });
  };

  return (
    <div
      key={data.id}
      className="search-result"
      onClick={() => {
        openArticleModal(data);
      }}
    >
      <div className="search-result-overlay">
        <div className="search-result-header">
          <p>{data.ownerEntityNickname || data.adminNickname}</p>
        </div>
        <div className="search-result-main">
          <p id="title">{data.title}</p>
          <div className="search-result-main-content">
            <div className="search-result-main-description">
              <p id="hashtag">{!!data.articleHashtags && data.articleHashtags.join(' ')}</p>
              <p>{!!data.name && data.name}</p>
            </div>
            <div className="search-result-main-like-comment">
              <div className="search-like-comment">
                <img src="/images/search-result/feed_like.png" alt="좋아요 이미지" />
                <p>{cntLike}</p>
              </div>
              <div className="search-like-comment">
                {filterId === 1 ? (
                  <img src="/images/search-result/feed_comment.png" alt="댓글 이미지" />
                ) : (
                  <img src="/images/search-result/feed_star_icon.png" alt="별 이미지" />
                )}
                <p>{filterId === 1 ? cntComment : data.articleCnt}</p>
              </div>
            </div>
          </div>
        </div>
      </div>
      <img
        src={
          (!!data.imageResponse && data.imageResponse.imageUrl) ||
          (!!data.contourResponse && data.contourResponse.cThumbUrl) ||
          (!!data.imgUrl && data.imgUrl)
        }
        alt=""
        className="search-result-image"
      />
    </div>
  );
}
