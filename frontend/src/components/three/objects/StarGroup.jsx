import { useRef, useEffect, useState } from 'react';
import * as THREE from 'three';
import Star from './Star';
import { useFrame } from '@react-three/fiber';
import { Line, useCursor } from '@react-three/drei';
import { easing } from 'maath';
import getUuidByString from 'uuid-by-string';
import douglasPeucker from '../../../utils/douglasPeucker';

export default function StarGroup({ position = [24, 20, 24], name, selected }) {
  const group = useRef();
  const [target, setTarget] = useState(true);
  const [hovered, hover] = useState(false);
  const [pointList, setPointList] = useState([]);
  const uuid = getUuidByString('' + name);
  useCursor(hovered);

  const isActive = selected === name;

  useEffect(() => {
    group.current.lookAt(new THREE.Vector3(0, 0.03, 0));
    window.addEventListener('keydown', ({ key }) => {
      if (key == 'a') setTarget(!target);
    });

    fetch('./data/contours.json')
      .then((data) => data.json())
      .then(([data]) => {
        const zipped = douglasPeucker(
          data.contours.map(([point]) => {
            return { x: point[0], y: point[1] };
          }),
          5,
        );

        const points = zipped.map(({ x, y }) => [x, y]);

        const [lx, rx] = points.reduce(
          (prev, now) => [Math.min(now[0], prev[0]), Math.max(now[0], prev[1])],
          [data.image_width, 0],
        );

        const [dy, uy] = points.reduce(
          (prev, now) => [Math.min(now[1], prev[0]), Math.max(now[1], prev[1])],
          [data.image_height, 0],
        );

        const width = rx - lx;
        const height = uy - dy;
        const size = Math.max(width, height);

        setPointList(
          points.map(([x, y]) => {
            const X = (x - lx + (size - width) / 2) / size - 1 / 2;
            const Y = (y - dy + (size - height) / 2) / size + 1 / 2;

            return [X * 80, (1 - Y) * 80, 0];
          }),
        );
      });
  }, []);

  useFrame((state, dt) => {
    if (isActive) easing.damp3(group.current.scale, 0.2, dt);
    else easing.damp3(group.current.scale, 0.1, dt);
  });

  return (
    <group ref={group} position={position} scale={0.1} name={uuid}>
      <mesh
        scale={[82, 82, 0.1]}
        position={[0, 0, -1]}
        name={name}
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
      {pointList.map(([x, y, _], i) => (
        <Star key={'' + name + i} position={[x, y, 0]} scale={1.5} name={name} id={i} />
      ))}
      {pointList.length > 0 && (
        <Line points={[...pointList, pointList[0]]} color={'white'} linewidth={2} />
      )}
    </group>
  );
}
