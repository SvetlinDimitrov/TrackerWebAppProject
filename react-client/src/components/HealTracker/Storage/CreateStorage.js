import React, { useEffect, useState } from "react";
import { useNavigate , useParams} from "react-router-dom";
import { FaHeart } from "react-icons/fa";

import api from "../../../util/api";
import styles from "./CreateStorage.module.css";

const CreateStorage = ({
  userToken,
  setFailedMessage,
  setSuccessfulMessage,
}) => {
  const { recordId } = useParams();
  const navigate = useNavigate();
  const [storageName, setStorageName] = useState(undefined);

  useEffect(() => {
    setStorageName("");
  }, []);

  const handleStorageCreation = async (e) => {
    e.preventDefault();

    if (storageName.length < 2) {
      setFailedMessage({
        message: "Name must be at least 2 character long!",
        flag: true,
      });
      navigate("/health-tracker/record/" + recordId);
      return;
    }
    try {
      await api.post(
        `/record/${recordId}/storage?storageName=${encodeURIComponent(
          storageName
        )}`,
        {},
        { headers: { Authorization: `Bearer ${userToken}` } }
      );
      setSuccessfulMessage({
        message: "Storage: " + storageName + " was created successfully!",
        flag: true,
      });
    } catch (error) {
      setFailedMessage({
        message:
          "Something went wrong with record creation. Please try again later!",
        flag: true,
      });
    }
    setStorageName("");
    navigate("/health-tracker/record/" + recordId);
  };

  return (
    <div className={styles.container}>
      <h2 className={styles.container_header}>Create a new storage</h2>
      <FaHeart className={styles.container_icon} />

      <form
        className={styles.container_form}
        onSubmit={(e) => handleStorageCreation(e)}
      >
        <input
          type="text"
          name="name"
          onChange={(e) => setStorageName(e.target.value)}
          value={storageName}
          className={styles.container_input}
          placeholder="Storage name"
        />
        <button type="submit" className={styles.container_submit}>
          Submit
        </button>
      </form>
    </div>
  );
};

export default CreateStorage;
