import { createContext } from 'react';

export const ModalContext = createContext(null);

export default function ModalContainer() {
  const [state, setState] = useState({
    isOpen: false,
    children: null,
  });
  return (
    <ModalContext.Provider value={{ state: state, setState: setState }}>
      {state.isOpen && state.children}
    </ModalContext.Provider>
  );
}
