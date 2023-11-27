import React, { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";

import { FaHeart } from "react-icons/fa";
import styles from "./SelectRecord.module.css";

const SelectRecord = ({ records }) => {
  const { recordId } = useParams();
  const [selected, setSelected] = useState(-1);
  const navigate = useNavigate();

  useEffect(() => {
    setSelected( (recordId === null || recordId === undefined) ? -1 : recordId);
  }, [recordId]);

  const handleSelectRecord = (id) => {
    const selectedId = +id;
    setSelected(selectedId);

    if (selectedId === -1) {
      navigate(`/health-tracker`);
      return;
    }
    navigate(`/health-tracker/record/${selectedId}`);
  };

  return (
    <div className={styles.container}>
      <h2 className={styles.container_header}>
        Select record for more details
      </h2>
      <FaHeart className={styles.container_icon} />
      <p className={styles.container_text}>
        Choose a record from the dropdown:
      </p>
      <select
        value={selected}
        onChange={(e) => handleSelectRecord(e.target.value)}
      >
        <option value={-1}>Choose Record</option>
        {records.map((record) => (
          <option key={record.id} value={record.id}>
            {record.name}
          </option>
        ))}
      </select>
    </div>
  );
};

export default SelectRecord;
