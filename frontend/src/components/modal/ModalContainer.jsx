import { createContext, useState } from 'react';

export const ModalContext = createContext(null);

export default function ModalContainer() {
  const [modalState, setModalState] = useState({
    isOpen: false,
    children: null,
  });
  return (
    <ModalContext.Provider value={{ modalState: modalState, setModalState: setModalState }}>
      {modalState.isOpen && modalState.children}
    </ModalContext.Provider>
  );
}
