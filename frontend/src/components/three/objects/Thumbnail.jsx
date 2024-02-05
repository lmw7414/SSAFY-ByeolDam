import * as THREE from 'three';
import { useRef, useEffect } from 'react';
import { Image } from '@react-three/drei';

export default function Thumbnail({ url, position = [0, 0, 0] }) {
  const image = useRef();

  return (
    <mesh>
      <Image ref={image} position={position} url={url} scale={40} />
    </mesh>
  );
}
