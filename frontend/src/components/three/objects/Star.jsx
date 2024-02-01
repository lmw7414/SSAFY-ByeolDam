import { useCursor } from '@react-three/drei';
import { useState } from 'react';
import Thumbnail from './Thumbnail';

export default function Star({ position = [0, 0, 0], scale = 1.5 }) {
  const [hovered, hover] = useState(false);
  useCursor(hovered);

  return (
    <group position={position}>
      <mesh
        scale={scale * (hovered ? 1 : 0.7)}
        onPointerOver={(e) => {
          e.stopPropagation();
          hover(true);
        }}
        onPointerOut={() => {
          hover(false);
        }}
        onClick={(e) => {
          e.stopPropagation();
        }}
      >
        <sphereGeometry />
        <meshBasicMaterial toneMapped={false} />
        {/* <meshStandardMaterial emissive="red" emissiveIntensity={2} toneMapped={false} /> */}
      </mesh>
      {hovered && (
        <Thumbnail
          url={'./src/assets/images/dog.jpeg'}
          position={[position[0] < 0 ? -23 : 23, position[1] < 0 ? -23 : 23, 2]}
        />
      )}
    </group>
  );
}
