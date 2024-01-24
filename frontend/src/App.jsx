import { Canvas } from '@react-three/fiber';
import { FirstPersonControls } from '@react-three/drei';
import { Suspense } from 'react';

import Constellation from './components/three/Constellation';
import Space from './components/three/Space';
import Land from './components/three/Land';

import constellationList from './constants/dummyData';

export default function App() {
  return (
    <div id="canvas-container">
      <Canvas camera={{ position: [0, 0, 0] }}>
        <color attach="background" args={['#191920']} />
        <Suspense fallback={null}>
          <Space />
          <Land />
          {constellationList.map(({ id, url, position }) => (
            <Constellation key={id} url={url} position={position} />
          ))}
        </Suspense>
        <FirstPersonControls lookSpeed={0.1} />
      </Canvas>
    </div>
  );
}
