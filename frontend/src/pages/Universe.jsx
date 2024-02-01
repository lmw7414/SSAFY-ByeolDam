import { Canvas } from '@react-three/fiber';
import { Stars, OrbitControls } from '@react-three/drei';
import { Suspense, useRef, useEffect } from 'react';
import * as THREE from 'three';

import constellationList from '../constants/dummyData';

import ConstellationControls from '../components/three/objects/ConstellationControls';
import Camera from '../components/three/objects/Camera';
import Land from '../components/three/objects/Land';
import useModal from '../hooks/useModal';

import ConstellationListModal from '../components/modal/ConstellationListModal';
export default function Universe() {
  const controller = useRef();
  const camera = useRef();
  const [modalState, setModalState] = useModal();

  const openConstellationModal = () => {
    setModalState({
      isOpen: true,
      title: '별자리 리스트',
      children: <ConstellationListModal />,
    });
  };

  return (
    <div className="canvas-container">
      <div className="main_buttons_box">
        <img
          src="/src/assets/images/main_buttons/post_create_button.png"
          alt="post_create_button"
          className="main-button"
          onClick={openConstellationModal}
        />
        <img
          src="/src/assets/images/main_buttons/constellation_list.png"
          alt="post_create_button"
          className="main-button"
          onClick={openConstellationModal}
        />
      </div>

      <Canvas>
        <Camera cameraRef={camera} />
        <OrbitControls
          ref={controller}
          enableZoom={false}
          enablePan={false}
          camera={camera.current}
          reverseOrbit
          target={new THREE.Vector3(0, 0.03, 0)}
          // target={new THREE.Vector3(1, 1, 1)}
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
