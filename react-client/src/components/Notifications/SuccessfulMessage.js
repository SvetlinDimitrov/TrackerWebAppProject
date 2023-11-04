import React from "react";
import styles from "./SuccessfulMessage.module.css";

const SuccessMessage = ({ onClose }) => {
  return (
    <div className={styles.successMessage}>
      <p>Changes were made successfully!</p>
      <button className={styles.successMessageButton} onClick={onClose}>Close</button>
    </div>
  );
};

export default SuccessMessage;