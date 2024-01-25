import { Canvas } from '@react-three/fiber';
import { Stars, OrbitControls } from '@react-three/drei';
import { Suspense } from 'react';
import * as THREE from 'three';

import universeBackgroundColor from '../../constants/color';
import constellationList from '../../constants/dummyData';

import Constellation from './objects/Constellation';
import Camera from './objects/Camera';
import Land from './objects/Land';

export default function Universe() {
  return (
    <Canvas>
      <color attach="background" args={universeBackgroundColor} />
      <Camera />
      <OrbitControls
        enableZoom={false}
        target={new THREE.Vector3(0, 0.03, 0.5)}
        maxPolarAngle={(Math.PI / 180) * 120}
        minPolarAngle={(Math.PI / 180) * 90}
      />
      <Suspense fallback={null}>
        <Land />
        <Stars />
        {constellationList.map(({ id, url, position }) => (
          <Constellation key={id} url={url} position={position} />
        ))}
      </Suspense>
    </Canvas>
  );
}
