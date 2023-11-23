import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

import { FaHeart } from "react-icons/fa";

import api from "../../../util/api";
import styles from "../HeathTracker.module.css";
import styles3 from "./DeleteRecord.module.css";
import styles2 from "./SelectRecordDetails.module.css";

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
        navigate("/health-tracker");
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
        <div className={styles2.sectionContainer}>
          <button
            className={styles.deleteButton}
            onClick={() => setPopupVisible(true)}
          >
            Delete Record
          </button>
        </div>
      </div>
      {isPopupVisible && (
        <div className={styles3.overlay}>
          <div className={styles3.popup}>
            <h2>Confirm Deletion</h2>
            <p>Please enter the name of the record to confirm deletion:</p>
            <input
              type="text"
              className={styles3.input}
              onChange={(e) => setNameDeletion(e.target.value)}
            />
            <button
              className={styles3.confirmButton}
              onClick={() => handleDeletion()}
            >
              Confirm
            </button>
            <button
              className={styles3.cancelButton}
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
