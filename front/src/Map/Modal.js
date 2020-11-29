import ReactModal from "react-modal";
import styled from "styled-components";
import modalContent from "./modal-content.png";

const customStyles = {
  content: {
    top: "50%",
    left: "50%",
    right: "auto",
    bottom: "auto",
    marginRight: "-50%",
    transform: "translate(-50%, -50%)",
  },
  overlay: {
    backgroundColor: "rgba(0,0,0,.8)",
    zIndex: 1111,
  },
};

const ContentImg = styled.img.attrs({ src: modalContent })`
  width: 600px;
`;

export function Modal({ isVisible, onClose }) {
  return (
    <>
      <ReactModal
        isOpen={isVisible}
        onRequestClose={onClose}
        style={customStyles}
        contentLabel="Example Modal"
      >
        <ContentImg />
      </ReactModal>
    </>
  );
}
