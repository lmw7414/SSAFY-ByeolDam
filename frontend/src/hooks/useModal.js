import { useContext, createContext } from 'react';

export const ModalContext = createContext({
  modalState: null,
  setModalState: () => {},
});

export default function useModal() {
  const modalContext = useContext(ModalContext);
  return [modalContext?.modalState, modalContext?.setModalState];
}
