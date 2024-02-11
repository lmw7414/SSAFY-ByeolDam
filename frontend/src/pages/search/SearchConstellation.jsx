import { useEffect } from 'react';
import axios from '../../apis/axios';

export default function SearchConstellation() {
  const searchConstellation = async () => {
    const data = await axios.get('/constellations');
    console.log('constell', data);
  };

  useEffect(() => {
    searchConstellation();
  }, []);

  return (
    <div>
      <h1>별자리 검색 페이지</h1>
    </div>
  );
}
