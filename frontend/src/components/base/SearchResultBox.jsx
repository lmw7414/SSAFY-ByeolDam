export default function SearchResultBox({ data }) {
  return (
    <div key={data.id} className="search-result">
      <div className="search-result-overlay">
        <div className="search-result-header">
          <p>닉네임</p>
        </div>
        <div className="search-result-main">
          <p>{data.title}</p>
          <div className="search-result-main-content">
            <div className="search-result-main-description">
              <p>{data.description}</p>
            </div>
            <div className="search-result-main-like-comment">
              <p>좋아요</p>
              <p>댓글</p>
            </div>
          </div>
        </div>
      </div>
      <img src={data.imgUrl} alt="" className="search-result-image" />
    </div>
  );
}
