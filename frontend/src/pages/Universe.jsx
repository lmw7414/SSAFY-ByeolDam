import { Canvas } from '@react-three/fiber';
import { Stars, OrbitControls } from '@react-three/drei';
import { Suspense } from 'react';
import * as THREE from 'three';

import constellationList from '../constants/dummyData';

import ConstellationGroup from '../components/three/objects/ConstellationGroup';
import Camera from '../components/three/objects/Camera';
import Land from '../components/three/objects/Land';

export default function Universe() {
  return (
    <div className="canvas-container">
      <Canvas>
        <Camera />
        <OrbitControls
          enableZoom={false}
          reverseOrbit
          target={new THREE.Vector3(0, 0.03, 0)}
          maxPolarAngle={(Math.PI / 180) * 120}
          minPolarAngle={(Math.PI / 180) * 90}
          rotateSpeed={0.2}
        />
        <Suspense fallback={null}>
          <Land />
          <Stars factor={10} fade />
          <Stars saturation={2} factor={20} speed={1.2} fade />
          <ConstellationGroup constellationList={constellationList} />
        </Suspense>
      </Canvas>
    </div>
  );
}
