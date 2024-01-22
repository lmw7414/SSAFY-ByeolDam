import * as THREE from 'three'
import { useEffect, useRef } from 'react'
import { useRoute, useLocation } from 'wouter'
import { easing } from 'maath'
import { useFrame } from '@react-three/fiber' 
import Frame from './Frame'

const GOLDENRATIO = 1.61803398875

export default function Frames ({ images, q = new THREE.quaternion(), p = new THREE.Vector3()}) {
    const ref = useRef()
    const clicked = useRef()
    const [, params] = useRoute('/item/:id')
    const [, setLocation] = useLocation()

    useEffect(() => {
        clicked.current = ref.current.getObjectByName(params?.id)
        if (clicked.current) {
            clicked.current.parent.updateWorldMatrix(true, true)
            clicked.current.parent.localToWorld(p.set(0, GOLDENRATIO / 2, 1.25))
            clicked.current.parent.getWorldQuaternion(q)
        } else {
            p.set(0, 0, 5.5)
            q.identity()
        }
    })

    useFrame((state, dt) => {
        easing.damp3(state.camera.position, p, 0.4, dt)
        easing.dampQ(state.camera.quaternion, p, 0.4, dt)
    })

    return (
        <THREE.group
            ref={ref}
            onClick={(e)=> (e.stopPropagation(), setLocation(clicked.current === e.object ? '/' : '/item/' + e.object.name))}
            onPointerMissed={() => setLocation('/')}
        >
            {images.map((props) => <Frame key={props.url} {...props}/>)}
        </THREE.group>
    )
}