import React, { useEffect, useState } from 'react'
import "../../assets/styles/scss/components/feed.scss";

export default function Feed({feedData}) {
    const [like, setLike] = useState(feedData.like);
  
    // 좋아요 버튼을 누르면 좋아요 post 요청
    // 댓글 열고 닫기
    useEffect(()=>{
      // 좋아요 버튼이 눌리면 DB에 좋아요 상태를 업데이트 해야하는 코드가 들어가야 한다고 추측중
    },[like])
    
    const toggleLike = () => {
      setLike(!like);
      console.log("like : ", like);
    }

    console.log(feedData);

    
  return (
    <div className='feed'>
        <div className='feed-header'>
          <img src="frontend\src\assets\images\search-bar-filter-icons\user.png" alt="user-profile"/>
          {feedData.ownerEntityNickname}
        </div>
        <div>
        <img src={feedData.imageResponse.imageUrl} width={300} height={300} className='feed-img' alt={feedData.title}/>
        </div>
        <div className='feed-content-container'>
          <div className='feed-content-header'>
            <div className='feed-like'>
              <button onClick={toggleLike} className={like ? 'liked': 'not-liked'}>
                Like
              </button>
            </div>
          <div className='feed-tag'>
            {feedData.articleHashtags.map((tag, index) => {
              return <span key={index} className='tag'>{tag}</span>
            })}
          </div>
          <div className='feed-hits'>
            <span>{feedData.hits}</span>
          </div>
          </div>
          <div className='feed-content'>
            {feedData.description}
          </div>
          <div>
            {/* <Comment /> */}
            {/* <ChildComment></ChildComment> */}
          </div>
        </div>
    </div>
  )
}
