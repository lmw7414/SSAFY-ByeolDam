import { useState, useEffect } from 'react';
import UserBox from './UserBox';
import axios from '/src/apis/axios';

export default function FollowModal({ followSubject, modalState }) {
  const [array, setArray] = useState([]);

  const readFollowings = async () => {
    const data = await axios.get('/me/followings');
    return data.data.result;
  };

  const readFollowers = async () => {
    const data = await axios.get('/me/followers');
    return data.data.result;
  };

  useEffect(() => {
    if (followSubject === 'following') {
      readFollowings().then((result) => {
        setArray(result);
      });
    } else if (followSubject === 'follower') {
      readFollowers().then((result) => {
        setArray(result);
      });
    }
  }, [modalState]);

  return (
    <div>
      <input type="text" placeholder="검색" className="follow-modal-input" />
      <div className="user-box-container">
        {array.map((user) => (
          <UserBox
            key={user.nickname}
            followSubject={followSubject}
            profileImg={user.imageUrl}
            nickname={user.nickname}
            constellationCounts={user.constellationCounts}
            articleCounts={user.articleCounts}
          />
        ))}
      </div>
    </div>
  );
}
