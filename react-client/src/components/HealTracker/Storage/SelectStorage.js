import React, { useState , useEffect} from "react";
import { useNavigate, useParams } from "react-router-dom";

import { FaHeart } from "react-icons/fa";
import styles from "./SelectStorage.module.css";

const SelectStorage = ({ storages }) => {
  const { recordId , storageId} = useParams();
  const [selected, setSelected] = useState(-1);
  const navigate = useNavigate();

  useEffect(() => {
    setSelected( (storageId === null || storageId === undefined) ? -1 : storageId);
  }, [storageId]);

  const handleSelectStorage = (id) => {
    const selectedId = +id;
    setSelected(selectedId);

    if (selectedId === -1) {
      navigate(`/health-tracker/record/${recordId}`);
      return;
    }
    navigate(`/health-tracker/record/${recordId}/storage/${selectedId}`);
  };

  return (
    <div className={styles.container}>
      <h2 className={styles.container_header}>
        Select storage for more food details
      </h2>
      <FaHeart className={styles.container_icon} />
      <p className={styles.container_text}>
        Choose a storage from the dropdown:
      </p>
      <select
        value={selected}
        onChange={(e) => handleSelectStorage(e.target.value)}
      >
        <option value={-1}>Choose Storage</option>
        {storages.map((storage) => (
          <option key={storage.id} value={storage.id}>
            {storage.name}
          </option>
        ))}
      </select>
    </div>
  );
};

export default SelectStorage;
