import { useEffect, useState } from 'react';

import ConstellationSelectBox from './ConstellationSelectBox';
import { getUserUniverse } from '../../apis/constellation';

export default function ConstellationSelectList({ article, setArticle }) {
  const [constellationList, setConstellationList] = useState([]);

  useEffect(() => {
    const nickname = JSON.parse(sessionStorage.profile).nickname;
    getUserUniverse(nickname).then(({ result }) => {
      setConstellationList(
        result.map((constellation) => {
          return {
            id: constellation.id,
            name: constellation.name,
            thumbnail: constellation.contourResponse.cThumbUrl,
          };
        }),
      );
    });
  }, []);

  return (
    <div className="constellation-select-list">
      {
        <ConstellationSelectBox
          key={-1}
          id={-1}
          name={'미분류'}
          thumbnail={'/images/base/unclassified.png'}
          index={0}
          article={article}
          setSelected={() => {
            setArticle({ ...article, constellationId: -1 });
          }}
        />
      }
      {constellationList.map(({ id, thumbnail, name }, idx) => (
        <ConstellationSelectBox
          key={id}
          thumbnail={thumbnail}
          name={name}
          setSelected={() => {
            setArticle({
              ...article,
              constellationId: id,
            });
          }}
          article={article}
          id={id}
        />
      ))}
    </div>
  );
}
