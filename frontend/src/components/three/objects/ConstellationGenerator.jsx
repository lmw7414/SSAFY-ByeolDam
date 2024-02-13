import { Image } from '@react-three/drei';
import cat from '/images/cat.png';

export default function ConstellationGenerator({ controllerRef }) {
  const controller = controllerRef;

  return <Image url={cat} transparent position={[-4, 0, -3]} scale={4} />;
}
