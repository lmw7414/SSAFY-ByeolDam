import { useRef, useEffect, useState } from 'react';
import * as THREE from 'three';
import Star from './Star';
import { useFrame } from '@react-three/fiber';
import { Line, useCursor, Image } from '@react-three/drei';
import { easing } from 'maath';
import getUuidByString from 'uuid-by-string';

export default function Constellation({ position = [5, 1.5, 5], name, selected, url, thumbnail }) {
  const group = useRef();
  const imageRef = useRef();
  const [hovered, hover] = useState(false);
  const [pointList, setPointList] = useState([]);
  const uuid = getUuidByString('' + name);
  useCursor(hovered);

  const isActive = selected === uuid;

  useEffect(() => {
    group.current.lookAt(new THREE.Vector3(0, 0.03, 0));

    fetch(url)
      .then((data) => data.json())
      .then((data) => {
        const [lx, rx] = data.points.reduce(
          (prev, now) => [Math.min(now[0], prev[0]), Math.max(now[0], prev[1])],
          [data.width, 0],
        );

        const [dy, uy] = data.points.reduce(
          (prev, now) => [Math.min(now[1], prev[0]), Math.max(now[1], prev[1])],
          [data.height, 0],
        );

        const width = rx - lx;
        const height = uy - dy;
        const size = Math.max(width, height);

        const points = data.points.map(([x, y]) => {
          const X = (x - lx + (size - width) / 2) / size - 1 / 2;
          const Y = (y - dy + (size - height) / 2) / size + 1 / 2;

          return [X * 80, (1 - Y) * 80, 0];
        });

        setPointList(points);
      });
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
      <Image
        ref={imageRef}
        url={thumbnail}
        scale={90}
        opacity={0.5}
        transparent
        raycast={() => null}
      />
      {pointList.map(([x, y, _], i) => (
        <Star
          key={'' + name + i}
          position={[x, y, 0]}
          scale={1.5}
          name={name}
          id={i}
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
