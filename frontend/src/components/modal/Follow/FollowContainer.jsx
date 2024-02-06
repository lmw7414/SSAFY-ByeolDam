import { useState, useEffect } from 'react';
import UserBox from './UserBox';
import axios from '/src/apis/axios';

export default function FollowingFollower({ followSubject, modalState }) {
  const [array, setArray] = useState([]);

  const readFollowings = async () => {
    const data = await axios.get('/me/followings');
    // console.log('followings', data.data.result);
    return data.data.result;
  };

  const readFollowers = async () => {
    const data = await axios.get('/me/followers');
    // console.log('followers', data.data.result);
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

  // console.log(followSubject);
  // console.log(array);
  const dummyArray = [
    {
      imageUrl: '/src/assets/images/space.png',
      email: 'eung0202@naver.com',
      name: '리은규',
      nickname: '스트링치즈먹고싶다',
      memo: '카페지즈에서 야간 작업중',
      disclosureType: 'VISIBLE',
      birthday: '1956-02-28',
    },
    {
      imageUrl: '/src/assets/images/space.png',
      email: 'chiron7777@naver.com',
      name: '리현쳘',
      nickname: '배가부르다말다',
      memo: '고치소사마 카레 세접시 쌉가능',
      disclosureType: 'VISIBLE',
      birthday: '1956-02-28',
    },
    {
      imageUrl: '/src/assets/images/space.png',
      email: 'lsh243@naver.com',
      name: '리수혁',
      nickname: '메이플하고시팓',
      memo: '헤헤헿',
      disclosureType: 'VISIBLE',
      birthday: '1999-05-12',
    },
  ];

  return (
    <div>
      <input type="text" />
      <div>
        {dummyArray.map((user) => (
          <UserBox
            followSubject={followSubject}
            key={user.nickname}
            profileImg={user.imageUrl}
            nickname={user.nickname}
            // numConstellation={}
            // numStar={}
          />
        ))}
      </div>
    </div>
  );
}
