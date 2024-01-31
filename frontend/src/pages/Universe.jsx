import { Canvas } from '@react-three/fiber';
import { Stars, OrbitControls } from '@react-three/drei';
import { Suspense, useRef } from 'react';
import * as THREE from 'three';

import constellationList from '../constants/dummyData';

import ConstellationControls from '../components/three/objects/ConstellationControls';
import Camera from '../components/three/objects/Camera';
import Land from '../components/three/objects/Land';

export default function Universe() {
  const controller = useRef();
  const camera = useRef();

  return (
    <div className="canvas-container">
      <Canvas>
        <Camera cameraRef={camera} />
        <OrbitControls
          ref={controller}
          enableZoom={false}
          enablePan={false}
          camera={camera.current}
          reverseOrbit
          target={new THREE.Vector3(0, 0.03, 0)}
          maxPolarAngle={(Math.PI / 180) * 120}
          minPolarAngle={(Math.PI / 180) * 90}
          rotateSpeed={0.2}
          zoomSpeed={5}
          panSpeed={5}
        />
        <Suspense fallback={null}>
          <Land />
          <Stars saturation={1} speed={0.7} fade />
          <Stars saturation={2} speed={1.2} fade />
          <ConstellationControls controller={controller} constellationList={constellationList} />
        </Suspense>
      </Canvas>
    </div>
  );
}
