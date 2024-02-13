import axios from 'axios';
import {useCallback, useEffect, useState} from 'react'
import Feed from "../../components/feed/Feed.jsx"
import { useInView } from 'react-intersection-observer';
import { getFeeds } from '../../apis/articles.js';
import "../../assets/styles/scss/pages/feed.scss";

export default function FeedList() {
    const [page, setPage] = useState(1);
    const [feedList, setFeedList] = useState([]);
    const [ref, inView] = useInView();
    
    const getPage = async (pageNum) => {
        const data  = axios.get(`/feed/${page}`)
        setFeedList(data);
    }
    const fetchData = async () => {
        try{
            const response = await axios.get("./src/assets/data/feed.json");
            setFeedList([...feedList, ...response.data.feedData]);
        }catch(error){
            console.error("Error fetching feed: ", error);
        }
    };

    useEffect(() => {

        fetchData();

        //  무한 스크롤 구현 필요
        
        // window.addEventListener('scroll', (e) => {
        //     // 스크롤 위치 확인하는 동작, 현재 스크롤이 일정 범위를 넘어가면 새로 data 요청
        //     getPage(page);
        //     setPage(page + 1);
        // })
        
    }, [])

    useEffect(() => {
        if(inView) {
            console.log(inView, "무한 스크롤 요청")
            // getFeeds();
            fetchData();
        }
    }, [inView]);


    console.log("Feed list after useEffect: ", feedList);

  return (
    <div className='feed-container'>
        {feedList.map((feed) => {
            return <Feed key={feed.feedId} feedData = {feed} />
        })}
        <div ref={ref}>테스트</div>
    </div>
  )
}
