import { getArticles } from '../../../apis/articles';
import useModal from '../../../hooks/useModal';
import ArticleModal from '../article/ArticleModal';

export default function ConstellationRow({
  title,
  constellationId,
  constellationThumb,
  hoverArticles,
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
          constellationId={result.constellationId}
          description={result.description}
        />
      ),
    });
  };

  return (
    <div className="constellation-row">
      <div className="constellation-thumbnail-container">
        <img src={constellationThumb} alt="" className="constellation-thumbnail" />
        <p className="constellation-title">{title}</p>
      </div>
      <div className="star-list-container">
        {hoverArticles &&
          hoverArticles.map(({ id, articleThumbnail }) => (
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
