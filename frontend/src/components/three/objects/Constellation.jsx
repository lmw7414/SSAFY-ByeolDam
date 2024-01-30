import * as THREE from 'three';
import { useRef, useState, useEffect } from 'react';
import { Image, useCursor } from '@react-three/drei';
import { useFrame } from '@react-three/fiber';
import { easing } from 'maath';
import getUuid from 'uuid-by-string';
import { useRoute } from 'wouter';

export default function Constellation({
  id,
  url,
  rotation = new THREE.Euler(0, 0, 0),
  position = [0, 0, 0],
}) {
  const image = useRef();
  const [hovered, hover] = useState(false);
  const name = getUuid(url + id);
  const [, params] = useRoute('/home/constellation/:id');
  const isActive = params?.id === name;

  useCursor(hovered);

  useEffect(() => {
    image.current.lookAt(new THREE.Vector3(0, 0, 0));
  });

  useFrame((state, dt) => {
    if (isActive) easing.damp3(image.current.scale, 5, dt);
    else easing.damp3(image.current.scale, hovered ? 2.5 : 2, 0.1, dt);
  });
  return (
    <mesh
      onPointerOver={(e) => {
        e.stopPropagation();
        hover(true);
        // document.body.classList.add('pointer');
      }}
      onPointerOut={() => {
        hover(false);
        // document.body.classList.remove('pointer');
      }}
      transparent
    >
      <Image
        name={name}
        ref={image}
        position={position}
        url={url}
        rotation={rotation}
        scale={2}
        transparent
      />
    </mesh>
  );
}
