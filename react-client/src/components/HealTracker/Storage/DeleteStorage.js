import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

import { FaHeart } from "react-icons/fa";

import api from "../../../util/api";
import styles from "../HeathTracker.module.css";
import styles3 from "../Record/DeleteRecord.module.css";
import styles2 from "../Record/SelectRecordDetails.module.css";
import styles4 from "./DeleteStorage.module.css";

const DeleteStorage = ({
  selectedStorage,
  selectedRecord,
  userToken,
  setFailedMessage,
  setSuccessfulMessage,
}) => {
  const [isPopupVisible, setPopupVisible] = useState(false);
  const [nameDeletion, setNameDeletion] = useState("");
  const navigate = useNavigate();

  const handleDeletion = async () => {
    if (selectedStorage.name === nameDeletion) {
      try {
        await api.delete(
          `/record/${selectedRecord.id}/storage/${selectedStorage.id}`,
          { headers: { Authorization: `Bearer ${userToken}` } }
        );
        setSuccessfulMessage({
          message:
            "Storage: " + selectedStorage.name + " was deleted successfully!",
          flag: true,
        });
        navigate("/health-tracker");
      } catch (error) {
        setFailedMessage({
          message:
            "Something went wrong with storage deletion. Please try again later!",
          flag: true,
        });
      }
      navigate("/health-tracker");
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
      <div className={styles4.container}>
        <h2 className={styles4.container_header}>
          Selected record {selectedRecord && selectedRecord.name}
        </h2>
        <FaHeart className={styles4.container_icon} />
        <p className={styles4.container_text}>
          Delete storage {selectedStorage.name}
        </p>
        <div className={styles4.container_section}>
          <button
            className={styles4.container_deleteButton}
            onClick={() => setPopupVisible(true)}
          >
            Delete Storage
          </button>
        </div>
      </div>
      {isPopupVisible && (
        <div className={styles4.overlay}>
          <div className={styles4.popup}>
            <h2>Confirm Deletion</h2>
            <p>Please enter the name of the storage to confirm deletion:</p>
            <input
              type="text"
              className={styles4.popup_input}
              onChange={(e) => setNameDeletion(e.target.value)}
            />
            <button
              className={styles4.popup_confirmButton}
              onClick={() => handleDeletion()}
            >
              Confirm
            </button>
            <button
              className={styles4.popup_cancelButton}
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

export default DeleteStorage;
