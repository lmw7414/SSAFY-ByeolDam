import SearchResultBox from '../../components/base/SearchResultBox';

export default function SearchStar() {
  const dummyPosts = [
    {
      id: 1,
      title: 1,
      description: '1입니당',
      articleHashtags: ['태그', '가족'],
      imgUrl: '/src/assets/images/search-test/1.jpg',
    },
    {
      id: 2,
      title: 2,
      description: '2입니당',
      articleHashtags: ['태그', '가족'],
      imgUrl: '/src/assets/images/search-test/2.jpg',
    },
    {
      id: 3,
      title: 3,
      description: '3입니당',
      articleHashtags: ['태그', '가족'],
      imgUrl: '/src/assets/images/search-test/3.jpg',
    },
    {
      id: 4,
      title: 4,
      description: '4입니당',
      articleHashtags: ['태그', '가족'],
      imgUrl: '/src/assets/images/search-test/4.jpg',
    },
    {
      id: 5,
      title: 5,
      description: '5입니당',
      articleHashtags: ['태그', '가족'],
      imgUrl: '/src/assets/images/search-test/5.jpg',
    },
    {
      id: 6,
      title: 6,
      description: '6입니당',
      articleHashtags: ['태그', '가족'],
      imgUrl: '/src/assets/images/search-test/6.jpg',
    },
    {
      id: 7,
      title: 7,
      description: '7입니당',
      articleHashtags: ['태그', '가족'],
      imgUrl: '/src/assets/images/search-test/7.jpg',
    },
    {
      id: 8,
      title: 8,
      description: '8입니당',
      articleHashtags: ['태그', '가족'],
      imgUrl: '/src/assets/images/search-test/8.jpg',
    },
    {
      id: 9,
      title: 9,
      description: '9입니당',
      articleHashtags: ['태그', '가족'],
      imgUrl: '/src/assets/images/search-test/9.jpg',
    },
    {
      id: 10,
      title: 10,
      description: '10입니당',
      articleHashtags: ['태그', '가족'],
      imgUrl: '/src/assets/images/search-test/10.jpg',
    },
    {
      id: 11,
      title: 11,
      description: '11입니당',
      articleHashtags: ['태그', '가족'],
      imgUrl: '/src/assets/images/search-test/11.jpg',
    },
  ];
  return (
    <div className="search-page-background">
      <div className="search-result-container">
        {dummyPosts.map((post) => (
          <SearchResultBox key={post.id} data={post} />
        ))}
      </div>
    </div>
  );
}
