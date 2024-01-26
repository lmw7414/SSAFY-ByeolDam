import { useRoute, useLocation } from 'wouter';
import { useEffect, useRef } from 'react';
// import { useFrame } from '@react-three/fiber';
// import { easing } from 'maath';
import * as THREE from 'three';

import Constellation from './Constellation';

export default function ConstellationGroup({
  constellationList,
  q = new THREE.Quaternion(),
  p = new THREE.Vector3(),
}) {
  const ref = useRef();
  const clicked = useRef();
  const [, params] = useRoute('/home/constellation/:id');
  const [, setLocation] = useLocation();
  // let lastPosition = { x: 0, y: 0, z: 0.1 };

  useEffect(() => {
    clicked.current = ref.current.getObjectByName(params?.id);
    if (clicked.current) {
      clicked.current.updateWorldMatrix(true, true);
      clicked.current.localToWorld(p.set(0, 0, 0));
      clicked.current.getWorldQuaternion(q);
    } else {
      p.set(0, 0, 0.1);
      // q.identity();
    }
  });

  // useFrame((state, dt) => {
  // easing.dampQ(state.camera.quaternion, q, 0.4, dt);
  // console.log(state.camera.position, state.camera.quaternion);
  // });

  return (
    <group
      ref={ref}
      onClick={(e) => {
        e.stopPropagation();
        setLocation(clicked.current === e.object ? '/' : `/home/constellation/${e.object.name}`);
      }}
    >
      {constellationList.map(({ id, url, position }) => (
        <Constellation key={id} url={url} position={position} id={id} />
      ))}
    </group>
  );
}
