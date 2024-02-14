import { useEffect, useState } from 'react';

import ConstellationSelectBox from './ConstellationSelectBox';
import { getUserUniverse } from '../../apis/constellation';

export default function ConstellationSelectList() {
  const [selected, setSelected] = useState(0);
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
          id={-1}
          name={'미분류'}
          thumbnail={'/images/base/close-button.png'}
          index={0}
          selected={selected}
          setSelected={() => {
            setSelected(0);
          }}
        />
      }
      {constellationList.map(({ id, thumbnail, name }, idx) => (
        <ConstellationSelectBox
          key={name}
          thumbnail={thumbnail}
          name={name}
          setSelected={() => {
            setSelected(idx + 1);
          }}
          selected={selected}
          index={idx + 1}
        />
      ))}
    </div>
  );
}
