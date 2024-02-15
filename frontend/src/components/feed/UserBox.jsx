import { getProfileImage } from '../../apis/articles.js';
import React, { useEffect, useState } from 'react';

export default function UserBox({ nickName }) {
  const [imageUrl, setImageUrl] = useState('');

  const profileImage = async (nickName) => {
    try {
      const { data } = await getProfileImage(nickName);
      setImageUrl(data);
    } catch (error) {
      console.error('Error getting profile Image', error);
    }
  };

  useEffect(() => {
    profileImage(nickName);
  }, []);

  return (
    <div className="feed-header">
      <img src={imageUrl} alt="profile-image" />
      {nickName}
    </div>
  );
}
