import SearchResultBox from '../../components/base/SearchResultBox';
import { useEffect, useState } from 'react';
import useModal from '../../hooks/useModal';
import { useLocation } from 'react-router-dom';
import axios from '../../apis/axios';
import ArticleModal from '../../components/modal/article/ArticleModal';

export default function SearchStar() {
  const [articles, setArticles] = useState([]);
  const [modalState, setModalState] = useModal();
  const location = useLocation();

  const keyword = location.state === null ? '' : location.state.keyword;
  const filterId = location.state === null ? 0 : location.state.filterId;

  useEffect(() => {
    searchArticle(keyword);
  }, [keyword]);

  const searchArticle = async (keyword) => {
    const { data } = await axios.get(`/search/title?keyword=${keyword}`);
    console.log(data);
    setArticles(data.result);
  };

  const openArticleModal = (data) => {
    console.log(data);
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
    <div className="search-page-background">
      <div className="search-result-container">
        {articles.map((data) => (
          <SearchResultBox
            key={data.id}
            data={data}
            filterId={filterId}
            onClick={() => {
              openArticleModal(data);
            }}
          />
        ))}
      </div>
    </div>
  );
}
