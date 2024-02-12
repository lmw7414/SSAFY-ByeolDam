import { useLoader } from '@react-three/fiber';
import { GLTFLoader } from 'three/examples/jsm/loaders/GLTFLoader';
import * as THREE from 'three';

export default function Land() {
  const gltf = useLoader(GLTFLoader, '/models/land/land.glb');
  return (
    <primitive
      scale={5}
      object={gltf.scene}
      position={[0, 0, 0]}
      rotation={
        new THREE.Euler((Math.PI / 180) * -86, (Math.PI / 180) * -10, (Math.PI / 180) * -10)
      }
    />
  );
}
