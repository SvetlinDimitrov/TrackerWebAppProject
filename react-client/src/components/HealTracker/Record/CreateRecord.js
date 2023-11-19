import React from "react";
import { useNavigate } from "react-router-dom";
import { FaHeart } from "react-icons/fa";

import api from "../../../util/api";
import styles from "../HeathTracker.module.css";

const CreateRecord = ({
  userToken,
  setFailedMessage,
  setSuccessfulMessage,
}) => {
  const navigate = useNavigate();

  const handleRecordCreation = async (e) => {
    e.preventDefault();
    const name = e.target.name.value;

    if (name.length < 2) {
      setFailedMessage({
        message: "Name must be at least 2 character long!",
        flag: true,
      });
      navigate("/health-tracker");
      return;
    }
    try {
      await api.post(
        `/record?name=${encodeURIComponent(name)}`,
        {},
        { headers: { Authorization: `Bearer ${userToken}` } }
      );

      setSuccessfulMessage({
        message: "Record created successfully!",
        flag: true,
      });
    } catch (error) {
      setFailedMessage({
        message:
          "Something went wrong with record creation. Please try again later!",
        flag: true,
      });
    }
    navigate("/health-tracker");
  };

  return (
    <div className={styles.container}>
      <h2 className={styles.container_header}>Create a new record</h2>
      <FaHeart className={styles.container_icon} />

      <form className={styles.form} onSubmit={(e) => handleRecordCreation(e)}>
        <input
          type="text"
          name="name"
          className={styles.input}
          placeholder="Record name"
        />
        <button type="submit" className={styles.submit}>
          Submit
        </button>
      </form>
    </div>
  );
};

export default CreateRecord;
