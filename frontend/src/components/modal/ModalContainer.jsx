import useModal from '../../hooks/useModal';

export default function ModalContainer() {
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
        <div className="modal-container-background">
          <div className="modal-container">
            <div className="modal-header">
              <img
                src="/src/assets/images/nav-bar-toggle-button/close.png"
                className="modal-prev-btn"
              />
              <h1 className="modal-title">{currentModalState.title}</h1>
              <img
                src="/src/assets/images/base/close-button.png"
                alt="modal-close"
                className="modal-close"
                onClick={closeModal}
              />
            </div>
            <hr className="modal-container-divider" />
            {currentModalState.children}
          </div>
        </div>
      )}
    </>
  );
}
