import * as THREE from 'three';
import { useRef } from 'react';
import { Image } from '@react-three/drei';
import { useFrame } from '@react-three/fiber';

export default function Constellation({
  url,
  rotation = new THREE.Euler(0, 0, 0),
  position = [0, 0, 0],
}) {
  const image = useRef();
  useFrame(() => {
    image.current.lookAt(new THREE.Vector3(0, 0, 0));
  });
  return (
    <Image ref={image} position={position} url={url} rotation={rotation} scale={2} transparent />
  );
}
