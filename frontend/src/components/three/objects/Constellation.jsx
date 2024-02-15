import { useRef, useEffect, useState } from 'react';
import * as THREE from 'three';
import Star from './Star';
import { useFrame } from '@react-three/fiber';
import { Line, useCursor, Image } from '@react-three/drei';
import { easing } from 'maath';
import getUuidByString from 'uuid-by-string';

export default function Constellation({
  position = [5, 1.5, 5],
  name,
  selected,
  id,
  points,
  thumbnail,
  hoverArticles,
}) {
  const group = useRef();
  const imageRef = useRef();
  const [hovered, hover] = useState(false);
  const [pointList, setPointList] = useState([]);
  const [starList, setStarList] = useState([]);
  const uuid = getUuidByString('' + name);
  useCursor(hovered);

  const isActive = selected === uuid;

  useEffect(() => {
    const [lx, rx] = points.reduce(
      (prev, now) => [Math.min(now[0], prev[0]), Math.max(now[0], prev[1])],
      [260, 0],
    );

    const [dy, uy] = points.reduce(
      (prev, now) => [Math.min(now[1], prev[0]), Math.max(now[1], prev[1])],
      [260, 0],
    );

    const width = rx - lx;
    const height = uy - dy;
    const size = Math.max(width, height);

    const point = points.map(([x, y]) => {
      const X = (x - lx + (size - width) / 2) / size - 1 / 2;
      const Y = (y - dy + (size - height) / 2) / size + 1 / 2;

      return [X * 80, (1 - Y) * 80, 0];
    });

    setPointList(point);

    setStarList(
      hoverArticles.map(({ id, articleThumbnail }, idx) => {
        return {
          id: id,
          position: [point[idx][0], point[idx][1], 0],
          thumbnail: articleThumbnail,
        };
      }),
    );

    group.current.lookAt(new THREE.Vector3(0, 0.03, 0));
  }, []);

  useFrame((state, dt) => {
    if (isActive) easing.damp3(group.current.scale, 0.2, dt);
    else easing.damp3(group.current.scale, 0.1, dt);
  });

  return (
    <group ref={group} position={position} scale={0.1}>
      <mesh
        scale={[82, 82, 0.1]}
        position={[0, 0, -1]}
        name={uuid}
        onPointerOver={(e) => {
          e.stopPropagation();
          hover(true);
        }}
        onPointerOut={() => {
          hover(false);
        }}
      >
        <boxGeometry />
        <meshPhongMaterial opacity={0} transparent />
      </mesh>

      {!!thumbnail && (
        <Image
          ref={imageRef}
          url={thumbnail}
          scale={90}
          opacity={0.09}
          transparent
          raycast={() => null}
        />
      )}
      {starList.map(({ id, position, thumbnail }) => (
        <Star
          key={id}
          position={position}
          scale={1.5}
          name={id}
          articleId={id}
          isActive={isActive}
          thumbnail={thumbnail}
        />
      ))}
      {pointList.length > 0 && (
        <Line points={[...pointList, pointList[0]]} color={'white'} linewidth={2} />
      )}
    </group>
  );
}
