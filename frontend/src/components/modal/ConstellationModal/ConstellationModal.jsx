import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

import useModal from '../../../hooks/useModal';

import ConstellationRow from './ConstellationRow';

import { getMyConstellations } from '../../../apis/constellation';
import ArticleWritingModal from '../article/ArticleWritingModal';

export default function ConstellationModal() {
  const [currentModalState, setModalState] = useModal();
  const [constellationList, setConstellationList] = useState([]);
  const [unLabeled, setUnLabeled] = useState([]);

  const closeModal = () => {
    setModalState({
      isOpen: false,
      title: '',
      children: null,
    });
  };

  const navigate = useNavigate();

  const openConstellaionWriting = () => {
    navigate('/constellation');
    closeModal();
  };

  const openArticleWritingModal = () => {
    setModalState({
      isOpen: true,
      title: '새로운 별 생성하기',
      children: <ArticleWritingModal />,
    });
  };

  useEffect(() => {
    const nickname = JSON.parse(sessionStorage.profile).nickname;

    getMyConstellations(nickname).then(({ labeledList, unLabeledList }) => {
      setConstellationList(labeledList);
      setUnLabeled(unLabeledList);
    });
  }, []);

  return (
    <div className="constellation-modal-container">
      <div className="constellation-row-container">
        <ConstellationRow
          title={'미분류'}
          constellationThumb={'/images/base/unclassified.png'}
          hoverArticles={unLabeled.map(({ id, imageResponse }) => {
            return { id: id, articleThumbnail: imageResponse.thumbnailUrl };
          })}
        />
        {constellationList.map(({ name, id, hoverArticles, contourResponse }) => (
          <ConstellationRow
            key={id}
            title={name}
            constellationThumb={contourResponse.cThumbUrl}
            constellationId={id}
            hoverArticles={hoverArticles}
            constellationName={name}
          />
        ))}
      </div>
      <div className="constellation_main_buttons_box">
        <img
          src="/images/constellation-modal/constellation_create_button.png"
          alt="post_create_button"
          className="constellation-main-button"
          onClick={openConstellaionWriting}
        />
        <img
          src="/images/constellation-modal/post_create_button.png"
          alt="post_create_button"
          className="constellation-main-button"
          onClick={openArticleWritingModal}
        />
      </div>
    </div>
  );
}
