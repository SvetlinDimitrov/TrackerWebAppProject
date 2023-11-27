import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

import { FaHeart } from "react-icons/fa";

import api from "../../../util/api";
import styles4 from "./DeleteStorage.module.css";

const DeleteStorage = ({
  selectedStorage,
  recordId,
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
          `/record/${recordId}/storage/${selectedStorage.id}`,
          { headers: { Authorization: `Bearer ${userToken}` } }
        );
        setSuccessfulMessage({
          message:
            "Storage: " + selectedStorage.name + " was deleted successfully!",
          flag: true,
        });
        
      } catch (error) {
        setFailedMessage({
          message:
            "Something went wrong with storage deletion. Please try again later!",
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
    navigate("/health-tracker/record/" + recordId);
    setPopupVisible(false);
  };
  return (
    <>
      <div className={styles4.container}>
        <h2 className={styles4.container_header}>
          Selected storage {selectedStorage && selectedStorage.name}
        </h2>
        <FaHeart className={styles4.container_icon} />
        <p className={styles4.container_text}>
          Delete storage 
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
