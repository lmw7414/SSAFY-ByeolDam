import { getArticles } from '../../../apis/articles';
import useModal from '../../../hooks/useModal';
import ArticleModal from '../article/ArticleModal';

export default function ConstellationRow({
  title,
  constellationId,
  constellationThumb,
  hoverAritcles,
}) {
  const [modalState, setModalState] = useModal();

  const openAritcle = async (articleId) => {
    const { result } = await getArticles(articleId);

    setModalState({
      isOpen: true,
      title: '게시물 상세보기',
      children: (
        <ArticleModal
          articleId={result.id}
          imgUrl={result.imageResponse.imageUrl}
          owner={result.ownerEntityNickname}
          tags={result.articleHashtags}
          constellationName={''}
          commentList={result.commentList}
        />
      ),
    });
  };

  return (
    <div className="constellation-row">
      <div className="constellation-thumbnail-container">
        <img src={constellationThumb} alt="" className="constellation-thumbnail" />
      </div>
      <div className="star-list-container">
        {hoverAritcles &&
          hoverAritcles.map(({ id, articleThumbnail }) => (
            <div
              key={id}
              className="star-image-container"
              onClick={() => {
                openAritcle(id);
              }}
            >
              <img src={articleThumbnail} alt="" className="star-image" />
            </div>
          ))}
      </div>
    </div>
  );
}
