import { useCursor } from '@react-three/drei';
import { useState } from 'react';
import Thumbnail from './Thumbnail';
import useModal from '../../../hooks/useModal';
import ArticleModal from '../../modal/article/ArticleModal';

import { getArticles } from '../../../apis/articles';

export default function Star({
  position = [0, 0, 0],
  scale = 1.5,
  isActive,
  thumbnail,
  articleId = 1,
}) {
  const [hovered, hover] = useState(false);
  const [modalState, setModalState] = useModal();

  const openAritcleModal = async () => {
    const { result: data } = await getArticles(3);

    console.log(data);

    setModalState({
      isOpen: true,
      title: data.title,
      children: (
        <ArticleModal
          articleId={articleId}
          description={data.title}
          hits={data.hits}
          createdAt={data.createdAt}
          imgUrl={data.imageResponse.imageUrl}
          owner={data.ownerEntityNickname}
          tags={data.articleHashtags}
          commentList={data.commentList}
          constellationName={data.constellationEntityName}
        />
      ),
    });
  };

  useCursor(hovered);

  return (
    <group position={position}>
      <mesh
        scale={scale * (hovered ? 0.9 : 0.6)}
        onPointerOver={(e) => {
          e.stopPropagation();
          if (!isActive) return;
          hover(true);
        }}
        onPointerOut={() => {
          if (!isActive) return;
          hover(false);
        }}
        onClick={(e) => {
          e.stopPropagation();
          if (!isActive) return;
          openAritcleModal();
        }}
      >
        <sphereGeometry />
        <meshBasicMaterial toneMapped={false} />
      </mesh>
      {hovered && (
        <Thumbnail
          url={thumbnail}
          position={[position[0] < 0 ? -15 : 15, position[1] < 0 ? -15 : 15, 2]}
        />
      )}
    </group>
  );
}
