import { useEffect, useRef, useState } from 'react';
import { useFrame } from '@react-three/fiber';
import { easing } from 'maath';
import * as THREE from 'three';

import Constellation from './Constellation';

export default function ConstellationControls({ controller, constellationList }) {
  const controllerRef = controller;
  const ref = useRef();
  const clicked = useRef();
  const [selected, setSelected] = useState('none');
  const [lastCameraState, setLastCameraState] = useState({
    prev: controllerRef?.current?.object?.clone(),
    target: new THREE.Vector3(0, 0.03, 0),
    position: new THREE.Vector3(0, 0, 0.1),
    isDone: true,
  });

  useEffect(() => {
    clicked.current = ref.current.getObjectByName(selected);

    if (clicked.current) {
      setLastCameraState({
        prev: !lastCameraState.prev ? controllerRef.current.object.clone() : lastCameraState.prev,
        target: clicked.current.localToWorld(new THREE.Vector3(0, 0, 0)),
        position: new THREE.Vector3(0, 3, 0),
        isDone: false,
      });
    } else {
      setLastCameraState({
        prev: null,
        target: new THREE.Vector3(0, 0.03, 0),
        position: lastCameraState.prev.position,
        isDone: false,
      });
    }
  }, [selected]);

  useFrame((state, dt) => {
    if (!lastCameraState.isDone) {
      easing.damp3(controllerRef.current.target, lastCameraState.target, 0.2, dt);
      easing.damp3(controllerRef.current.object.position, lastCameraState.position, 0.2, dt);
      controllerRef.current.minDistance = 0;

      if (
        controllerRef.current.target.distanceTo(lastCameraState.target) < 0.04 &&
        controllerRef.current.object.position.distanceTo(lastCameraState.position) < 0.04
      ) {
        setLastCameraState({ ...lastCameraState, isDone: true });
        if (!lastCameraState.prev) {
          controllerRef.current.enableRotate = true;
          controllerRef.current.enableDamping = true;
        } else {
          const distance = new THREE.Vector3(0, 0, 0).distanceTo(lastCameraState.target);

          controllerRef.current.enableZoom = true;
          controllerRef.current.maxDistance = distance;
          controllerRef.current.minDistance = distance / 3;
        }
      }
    }
  });

  return (
    <group
      ref={ref}
      onClick={(e) => {
        if (!lastCameraState.isDone || !e.object || !e.object.name) return;
        e.stopPropagation();
        controllerRef.current.enableRotate = false;
        controllerRef.current.enableDamping = false;
        controllerRef.current.enableZoom = false;
        setSelected(clicked.current === e.object ? 'none' : e.object.name);
      }}
    >
      {constellationList.map(({ id, url, position }) => (
        <Constellation key={id} url={url} name={id} position={position} selected={selected} />
      ))}
    </group>
  );
}
