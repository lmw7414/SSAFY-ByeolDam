import { useContext } from 'react';
import { ModalContext } from '../components/modal/ModalContainer';

const { state, useState } = useContext(ModalContext);
export default function useModal() {
  return [state, useState];
}
