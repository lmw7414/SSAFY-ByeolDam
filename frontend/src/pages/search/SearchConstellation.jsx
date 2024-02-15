import SearchResultBox from '../../components/base/SearchResultBox';
import { useState } from 'react';
import { useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import axios from '../../apis/axios';

export default function SearchConstellation() {
  const [constellations, setconstellations] = useState([]);
  const location = useLocation();

  const keyword = location.state === null ? '' : location.state.keyword;
  const filterId = location.state === null ? 0 : location.state.filterId;

  useEffect(() => {
    SearchConstellation(keyword);
  }, [keyword]);

  const SearchConstellation = async (keyword) => {
    const { data } = await axios.get(`/search/constellation?keyword=${keyword}`);
    setconstellations(data.result);
  };

  return (
    <div className="search-page-background">
      <div className="search-result-container">
        {constellations.map((data) => (
          <SearchResultBox key={data.id} data={data} filterId={filterId} />
        ))}
      </div>
    </div>
  );
}
