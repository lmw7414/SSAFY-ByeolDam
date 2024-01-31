export default function ModalContainer({ modalState }) {
  return (
    <>
      {modalState.isOpen && (
        <div className="modal-container">
          <h1>{modalState.title}</h1>
          <hr />
          {modalState.children}
        </div>
      )}
    </>
  );
}
