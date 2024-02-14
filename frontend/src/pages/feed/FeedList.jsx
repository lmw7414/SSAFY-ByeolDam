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

    const getFeedData = async () => {
        try{
            const {data} = await getFeeds();
            setFeedList([...feedList, ...data]);
        } catch(error) {
            console.error("Error while fetching feed: ", error);
        }
    }

    useEffect(() => {
        getFeedData();
    }, []);

    useEffect(() => {
        if(inView) {
            getFeedData();
        }
    }, [inView]);

  return (
    <div className='feed-container'>
        {feedList.map((feed) => {
            return <Feed key={feed.id} feedData = {feed} />
        })}
        <div ref={ref}>팔로우의 게시글이 없습니다!</div>
    </div>
  )
}
