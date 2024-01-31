import { useContext } from 'react';
import { ModalContext } from '../components/modal/ModalContainer';

const { modalState, setModalState } = useContext(ModalContext);
export default function useModal() {
  return [modalState, setModalState];
}
