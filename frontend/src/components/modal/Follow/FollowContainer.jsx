import { useState, useEffect } from 'react';
import UserBox from './UserBox';
import axios from '/src/apis/axios';

export default function FollowingFollower({ followSubject }) {
  const [followings, setFollowings] = useState(null);
  const [followers, setFollowers] = useState(null);

  // const readFollowings = async () => {
  //   const data = await axios.get('/me/followings');
  //   return data;
  // };

  // const readFollowers = async () => {
  //   const data = await axios.get('/me/followers');
  //   return data;
  // };

  // useEffect(() => {
  //   {
  //     followSubject === 'follow'
  //       ? readFollowings().then((result) => {
  //           setFollowings(result);
  //         })
  //       : readFollowers().then((result) => {
  //           setFollowers(result);
  //         });
  //   }
  // }, []);

  const dummyFollowings = {
    imageUrl: '/src/assets/images/space.png',
    email: 'eung0202@naver.com',
    name: '리은규',
    nickname: '스트링치즈먹고싶다',
    memo: '카페지즈에서 야간 작업중',
    disclosureType: 'VISIBLE',
    birthday: '1956-02-28',
  };

  return (
    <div>
      <input type="text" />
      <div>
        <UserBox />
      </div>
    </div>
  );
}
