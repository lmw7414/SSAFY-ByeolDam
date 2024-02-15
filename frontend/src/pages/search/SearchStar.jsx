import SearchResultBox from '../../components/base/SearchResultBox';
import { useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';
import axios from '../../apis/axios';

export default function SearchStar() {
  const [articles, setArticles] = useState([]);
  const location = useLocation();

  const keyword = location.state === null ? '' : location.state.keyword;
  const filterId = location.state === null ? 0 : location.state.filterId;

  useEffect(() => {
    searchArticle(keyword);
  }, [keyword]);

  const searchArticle = async (keyword) => {
    const { data } = await axios.get(`/search/title?keyword=${keyword}`);
    setArticles(data.result);
  };

  return (
    <div className="search-page-background">
      <div className="search-result-container">
        {articles.map((data) => (
          <SearchResultBox key={data.id} data={data} filterId={filterId} />
        ))}
      </div>
    </div>
  );
}
