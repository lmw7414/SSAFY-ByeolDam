import useModal from '../../hooks/useModal';

export default function ModalContainer({ isNavBarVisible }) {
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
        <div
          className={`modal-container-background ${isNavBarVisible ? 'modal-container-padding' : ''}`}
          onClick={() => {
            setModalState({ ...currentModalState, isOpen: false });
          }}
        >
          <div
            className="modal-container"
            onClick={(e) => {
              e.stopPropagation();
            }}
          >
            <div className="modal-header">
              <div></div>
              <h1 className="modal-title">{currentModalState.title}</h1>
              <img
                src="/images/base/close-button.png"
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
