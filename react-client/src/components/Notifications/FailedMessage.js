import React from "react";

import styles from "./FailedMessage.module.css";


const FailedMessage = ({ onClose }) => {
  return (
    <div className={styles.failedMessage}>
      <p>Failed to make changes. Please try again.</p>
      <button style={styles.failedMessageButton} onClick={onClose}>Close</button>
    </div>
  );
};

export default FailedMessage;