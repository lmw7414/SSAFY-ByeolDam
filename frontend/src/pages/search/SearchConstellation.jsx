// import SearchResultBox from '../../components/base/SearchResultBox';
// import { useState } from 'react';
// import { useEffect } from 'react';
// import { useLocation } from 'react-router-dom';
// import axios from '../../apis/axios';

// export default function SearchConstellation() {
//   const [constellations, setconstellations] = useState([]);
//   const location = useLocation();

//   const keyword = location.state === null ? '' : location.state.keyword;
//   const filterId = location.state === null ? 0 : location.state.filterId;

//   useEffect(() => {
//     SearchConstellation(keyword);
//   }, [keyword]);

//   const SearchConstellation = async (keyword) => {
//     const { data } = await axios.get(`/search/constellation?keyword=${keyword}`);
//     setconstellations(data.result);
//   };

//   return (
//     <div className="search-page-background">
//       <div className="search-result-container">
//         {constellations.map((data) => (
//           <SearchResultBox key={data.id} data={data} filterId={filterId} />
//         ))}
//       </div>
//     </div>
//   );
// }

import SearchResultBox from '../../components/base/SearchResultBox';

export default function SearchConstellation() {
  const dummyConstellations = [
    {
      id: 1,
      title: 1,
      description: '1입니당',
      articleHashtags: ['태그', '가족'],
      imgUrl: '/images/search-test/1.jpg',
    },
    {
      id: 2,
      title: 2,
      description: '2입니당',
      articleHashtags: ['태그', '가족'],
      imgUrl: '/images/search-test/2.jpg',
    },
    {
      id: 3,
      title: 3,
      description: '3입니당',
      articleHashtags: ['태그', '가족'],
      imgUrl: '/images/search-test/3.jpg',
    },
    {
      id: 4,
      title: 4,
      description: '4입니당',
      articleHashtags: ['태그', '가족'],
      imgUrl: '/images/search-test/4.jpg',
    },
    {
      id: 5,
      title: 5,
      description: '5입니당',
      articleHashtags: ['태그', '가족'],
      imgUrl: '/images/search-test/5.jpg',
    },
    {
      id: 6,
      title: 6,
      description: '6입니당',
      articleHashtags: ['태그', '가족'],
      imgUrl: '/images/search-test/6.jpg',
    },
    {
      id: 7,
      title: 7,
      description: '7입니당',
      articleHashtags: ['태그', '가족'],
      imgUrl: '/images/search-test/7.jpg',
    },
    {
      id: 8,
      title: 8,
      description: '8입니당',
      articleHashtags: ['태그', '가족'],
      imgUrl: '/images/search-test/8.jpg',
    },
    {
      id: 9,
      title: 9,
      description: '9입니당',
      articleHashtags: ['태그', '가족'],
      imgUrl: '/images/search-test/9.jpg',
    },
    {
      id: 10,
      title: 10,
      description: '10입니당',
      articleHashtags: ['태그', '가족'],
      imgUrl: '/images/search-test/10.jpg',
    },
    {
      id: 11,
      title: 11,
      description: '11입니당',
      articleHashtags: ['태그', '가족'],
      imgUrl: '/images/search-test/11.jpg',
    },
  ];
  return (
    <div className="search-page-background">
      <div className="search-result-container">
        {dummyConstellations.map((constellation) => (
          <SearchResultBox key={constellation.id} data={constellation} />
        ))}
      </div>
    </div>
  );
}
