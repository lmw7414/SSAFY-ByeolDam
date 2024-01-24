import { useLoader } from '@react-three/fiber';
import { GLTFLoader } from 'three/examples/jsm/loaders/GLTFLoader';

export default function Land() {
  const gltf = useLoader(GLTFLoader, './src/assets/models/land/scene.gltf');
  return <primitive scale={1} object={gltf.scene} position={[120, -250, 0]} />;
}
