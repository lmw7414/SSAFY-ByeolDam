import { useState } from 'react';

import ConstellationSelectBox from './ConstellationSelectBox';

export default function ConstellationSelectList() {
  const [selected, setSelected] = useState(0);

  const constellationList = [
    {
      name: '미분류',
      thumbnailUrl: 'https://cdn-icons-png.flaticon.com/512/1/1766.png',
    },
    {
      name: '강아지1',
      thumbnailUrl:
        'https://mblogthumb-phinf.pstatic.net/MjAxNzExMDhfMjQ1/MDAxNTEwMTI2MDE5NTgz.sdPZmq9XMwWhiYONqUUAQ7a5NnrbHn6cD9wuz-IStu0g.a73uBybTHHfkLhcz2dgQFxMxZlMlIPvKYn5eIvp0Q6wg.JPEG.knightsws/%EB%AC%B4%EB%A3%8C_%EA%B0%95%EC%95%84%EC%A7%80_%EC%9D%BC%EB%9F%AC%EC%8A%A4%ED%8A%B8_6.JPEG?type=w800',
    },
    {
      name: '강아지2',
      thumbnailUrl:
        'https://mblogthumb-phinf.pstatic.net/MjAxNzExMDhfMjQ1/MDAxNTEwMTI2MDE5NTgz.sdPZmq9XMwWhiYONqUUAQ7a5NnrbHn6cD9wuz-IStu0g.a73uBybTHHfkLhcz2dgQFxMxZlMlIPvKYn5eIvp0Q6wg.JPEG.knightsws/%EB%AC%B4%EB%A3%8C_%EA%B0%95%EC%95%84%EC%A7%80_%EC%9D%BC%EB%9F%AC%EC%8A%A4%ED%8A%B8_6.JPEG?type=w800',
    },
    {
      name: '강아지3',
      thumbnailUrl:
        'https://mblogthumb-phinf.pstatic.net/MjAxNzExMDhfMjQ1/MDAxNTEwMTI2MDE5NTgz.sdPZmq9XMwWhiYONqUUAQ7a5NnrbHn6cD9wuz-IStu0g.a73uBybTHHfkLhcz2dgQFxMxZlMlIPvKYn5eIvp0Q6wg.JPEG.knightsws/%EB%AC%B4%EB%A3%8C_%EA%B0%95%EC%95%84%EC%A7%80_%EC%9D%BC%EB%9F%AC%EC%8A%A4%ED%8A%B8_6.JPEG?type=w800',
    },
    {
      name: '강아지4',
      thumbnailUrl:
        'https://mblogthumb-phinf.pstatic.net/MjAxNzExMDhfMjQ1/MDAxNTEwMTI2MDE5NTgz.sdPZmq9XMwWhiYONqUUAQ7a5NnrbHn6cD9wuz-IStu0g.a73uBybTHHfkLhcz2dgQFxMxZlMlIPvKYn5eIvp0Q6wg.JPEG.knightsws/%EB%AC%B4%EB%A3%8C_%EA%B0%95%EC%95%84%EC%A7%80_%EC%9D%BC%EB%9F%AC%EC%8A%A4%ED%8A%B8_6.JPEG?type=w800',
    },
    {
      name: '강아지5',
      thumbnailUrl:
        'https://mblogthumb-phinf.pstatic.net/MjAxNzExMDhfMjQ1/MDAxNTEwMTI2MDE5NTgz.sdPZmq9XMwWhiYONqUUAQ7a5NnrbHn6cD9wuz-IStu0g.a73uBybTHHfkLhcz2dgQFxMxZlMlIPvKYn5eIvp0Q6wg.JPEG.knightsws/%EB%AC%B4%EB%A3%8C_%EA%B0%95%EC%95%84%EC%A7%80_%EC%9D%BC%EB%9F%AC%EC%8A%A4%ED%8A%B8_6.JPEG?type=w800',
    },
    {
      name: '강아지6',
      thumbnailUrl:
        'https://mblogthumb-phinf.pstatic.net/MjAxNzExMDhfMjQ1/MDAxNTEwMTI2MDE5NTgz.sdPZmq9XMwWhiYONqUUAQ7a5NnrbHn6cD9wuz-IStu0g.a73uBybTHHfkLhcz2dgQFxMxZlMlIPvKYn5eIvp0Q6wg.JPEG.knightsws/%EB%AC%B4%EB%A3%8C_%EA%B0%95%EC%95%84%EC%A7%80_%EC%9D%BC%EB%9F%AC%EC%8A%A4%ED%8A%B8_6.JPEG?type=w800',
    },
  ];

  return (
    <div className="constellation-select-list">
      {constellationList.map(({ thumbnailUrl, name }, idx) => (
        <ConstellationSelectBox
          key={name}
          thumbnailUrl={thumbnailUrl}
          name={name}
          setSelected={() => {
            setSelected(idx);
          }}
          selected={selected}
          index={idx}
        />
      ))}
    </div>
  );
}
