import * as THREE from 'three'
import { useRef, useState } from 'react'
import { useRoute} from 'wouter'
import { easing } from 'maath'
import getUuid from 'uuid-by-string'
import { useCursor, Image, Text } from '@react-three/drei'
import { useFrame } from '@react-three/fiber'

const GOLDENRATIO = 1.61803398875

export default function Frame({ url, c = new THREE.Color(), ...props}) {
    const image = useRef()
    const frame = useRef()
    const [, params] = useRoute('/item/:id')
    const [hovered, hover] = useState(false)
    const [rnd] = useState(() => Math.random())
    const name = getUuid(url);
    const isActive = params?.id === name
    useCursor(hovered)
    useFrame((state, dt) => {
        image.current.material.zoom = 2 + Math.sin(rnd * 10000 + state.clock.elapsedTime / 3) / 2
        easing.damp3(image.current.scale, [0.85 * (!isActive && hovered ? 0.85 : 1), 0.9 * (!isActive && hovered ? 0.905 : 1), 1], 0.1, dt)
        easing.dampC(frame.current.material.color, hovered ? 'orange' : 'white', 0.1, dt)
      })
      return (
        <group {...props}>
          <THREE.mesh
            name={name}
            onPointerOver={(e) => (e.stopPropagation(), hover(true))}
            onPointerOut={() => hover(false)}
            scale={[1, GOLDENRATIO, 0.05]}
            position={[0, GOLDENRATIO / 2, 0]}>
            <THREE.boxGeometry />
            <THREE.meshStandardMaterial color="#151515" metalness={0.5} roughness={0.5} envMapIntensity={2} />
            <THREE.mesh ref={frame} raycast={() => null} scale={[0.9, 0.93, 0.9]} position={[0, 0, 0.2]}>
              <THREE.boxGeometry />
              <THREE.meshBasicMaterial toneMapped={false} fog={false} />
            </THREE.mesh>
            <Image raycast={() => null} ref={image} position={[0, 0, 0.7]} url={url} />
          </THREE.mesh>
          <Text maxWidth={0.1} anchorX="left" anchorY="top" position={[0.55, GOLDENRATIO, 0]} fontSize={0.025}>
            {name.split('-').join(' ')}
          </Text>
        </group>
      )
}