import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

import { FaHeart } from "react-icons/fa";

import api from "../../../util/api";

import styles from "./DeleteRecord.module.css";

const DeleteRecord = ({
  selectedRecord,
  userToken,
  setFailedMessage,
  setSuccessfulMessage,
}) => {
  const [isPopupVisible, setPopupVisible] = useState(false);
  const [nameDeletion, setNameDeletion] = useState("");
  const navigate = useNavigate();

  const handleDeletion = async () => {
    if (selectedRecord.name === nameDeletion) {
      try {
        await api.delete(`/record/${selectedRecord.id}`, {
          headers: { Authorization: `Bearer ${userToken}` },
        });

        setSuccessfulMessage({
          message: "Record created successfully!",
          flag: true,
        });
        
      } catch (error) {
        setFailedMessage({
          message:
            "Something went wrong with record deletion. Please try again later!",
          flag: true,
        });
      }
    } else {
      setFailedMessage({
        message:
          "Names do not match. Please enter the name of the record to confirm deletion!",
        flag: true,
      });
    }
    navigate("/health-tracker");
    setPopupVisible(false);
  };
  return (
    <>
      <div className={styles.container}>
        <h2 className={styles.container_header}>
          Selected record {selectedRecord && selectedRecord.name}
        </h2>
        <FaHeart className={styles.container_icon} />
        <p className={styles.container_text}>Delete record</p>
        <div className={styles.container_section}>
          <button
            className={styles.container_deleteButton}
            onClick={() => setPopupVisible(true)}
          >
            Delete Record
          </button>
        </div>
      </div>
      {isPopupVisible && (
        <div className={styles.overlay}>
          <div className={styles.popup}>
            <h2>Confirm Deletion</h2>
            <p>Please enter the name of the record to confirm deletion:</p>
            <input
              type="text"
              className={styles.popup_input}
              onChange={(e) => setNameDeletion(e.target.value)}
            />
            <button
              className={styles.popup_confirmButton}
              onClick={() => handleDeletion()}
            >
              Confirm
            </button>
            <button
              className={styles.popup_cancelButton}
              onClick={() => setPopupVisible(false)}
            >
              Cancel
            </button>
          </div>
        </div>
      )}
    </>
  );
};

export default DeleteRecord;
