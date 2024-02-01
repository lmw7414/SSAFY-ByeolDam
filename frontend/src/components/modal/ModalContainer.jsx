import useModal from '../../hooks/useModal';

export default function ModalContainer({ modalState }) {
  const [currentModalState, setModalState] = useModal();
  const closeModal = () => {
    setModalState({
      isOpen: false,
      title: '',
      children: null,
    });
  };
  return (
    <>
      {currentModalState.isOpen && (
        <div className="modal-container">
          <div className="modal-header">
            <h1 className="modal-title">{currentModalState.title}</h1>
            <img
              src="/src/assets/images/close-button.png"
              alt="modal-close"
              className="modal-close"
              onClick={closeModal}
            />
          </div>
          <hr />
          {currentModalState.children}
        </div>
      )}
    </>
  );
}
