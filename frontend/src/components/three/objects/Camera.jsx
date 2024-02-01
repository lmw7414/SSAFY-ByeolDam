import { PerspectiveCamera } from '@react-three/drei';
import React from 'react';

const Camera = React.forwardRef((props, ref) => (
  <PerspectiveCamera ref={ref} makeDefault position={[0, 0, 0.1]} />
));

export default Camera;
