import { useLoader } from '@react-three/fiber';
import { GLTFLoader } from 'three/examples/jsm/loaders/GLTFLoader';

export default function Model() {
  const gltf = useLoader(GLTFLoader, './src/assets/models/space/scene.gltf');
  return <primitive scale={30} object={gltf.scene} position={[-43, -33, 43]} />;
}
