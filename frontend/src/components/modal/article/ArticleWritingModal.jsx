import { useState, useRef } from 'react';
import ImageUpload from '../../article/ImageUpload';
import ConstellationSelect from '../../article/ConstellationSelect';
import { addArticle } from '../../../apis/articles';
import useModal from '../../../hooks/useModal';

export default function ArticleWritingModal() {
  const [step, setStep] = useState(1);
  const [file, setFile] = useState(null);
  const [article, setArticle] = useState({
    description: '',
    articleHashtagSet: [],
    disclosureType: 'VISIBLE',
    imageType: 'ARTICLE',
    constellation: '',
  });
  const [modalState, setModalState] = useModal();

  const writeArticle = () => {
    addArticle(article, file).then((result) => {
      useModal({
        ...modalState,
        isOpen: false,
      });
      alert('별이 생성되었습니다.');
    });
  };

  return (
    <div>
      {step == 1 && (
        <ImageUpload
          setStep={setStep}
          setFile={setFile}
          setArticle={setArticle}
          article={article}
        />
      )}
      {step == 2 && (
        <ConstellationSelect
          writeArticle={writeArticle}
          article={article}
          setArticle={setArticle}
        />
      )}
    </div>
  );
}
