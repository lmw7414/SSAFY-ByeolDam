import { Canvas } from '@react-three/fiber';
import { Stars, OrbitControls } from '@react-three/drei';
import { Suspense, useEffect, useRef, useState } from 'react';
import { Bloom, EffectComposer } from '@react-three/postprocessing';
import { useParams } from 'react-router-dom';

import ConstellationControls from '../components/three/objects/ConstellationControls';
import Camera from '../components/three/objects/Camera';
import Land from '../components/three/objects/Land';

import { getUserUniverse } from '../apis/constellation';
import getPositionList from '../utils/getPositionList';

export default function OtherUniverse({ setNickname }) {
  const [constellationList, setConstellationList] = useState([]);
  const controller = useRef();
  const params = useParams();
  const camera = useRef();

  useEffect(() => {
    const nickname = params.nickname;
    setNickname(nickname);

    getUserUniverse(nickname).then(({ result }) => {
      const positionList = getPositionList(Math.min(15, result.length));

      setConstellationList(
        result.slice(0, 15).map(({ name, id, contourResponse, hoverArticles }, idx) => {
          return {
            id,
            hoverArticles,
            thumbnail: contourResponse.thumbUrl,
            points: contourResponse.ultimate,
            position: positionList[idx],
          };
        }),
      );
    });
  }, []);

  return (
    <div className="canvas-container">
      <Canvas>
        <Camera ref={camera} />
        <OrbitControls
          ref={controller}
          enableZoom={false}
          enablePan={false}
          camera={camera.current}
          reverseOrbit
          // target={new THREE.Vector3(0, 0.03, 0)}
          maxPolarAngle={(Math.PI / 180) * 140}
          minPolarAngle={(Math.PI / 180) * 90}
          rotateSpeed={0.2}
          zoomSpeed={5}
          panSpeed={5}
        />
        <EffectComposer>
          <Bloom minimapBlur intensity={0.3} luminanceThreshold={0.7} />
        </EffectComposer>
        <Suspense fallback={null}>
          <Land />
          <Stars saturation={1} speed={0.7} fade />
          {/* <Stars saturation={2} speed={1.2} fade /> */}
          <ConstellationControls controller={controller} constellationList={constellationList} />
        </Suspense>
      </Canvas>
    </div>
  );
}
