import React, { useState , useEffect} from "react";

import { FaHeart } from "react-icons/fa";
import { getRecordById } from "../../../util/RecordUtils";
import styles from "../HeathTracker.module.css";

const SelectRecord = ({ records, selectedRecord, setSelectedRecord }) => {
  const [selected, setSelected] = useState(-1);

  useEffect(() => {
    if (selectedRecord) {
      setSelected(selectedRecord.id);
    } else {
      setSelected(-1);
    }
  }, [selectedRecord]);
  
  const handleSelectRecord = async(id) => {
    const selectedId = +id;
    setSelected(selectedId);

    if (selectedId === -1) {
      setSelectedRecord(undefined);
      return;
    }
    const record = getRecordById(records, selectedId);
    setSelectedRecord(record);
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
        className={styles.choose_RecordContainer_selectValue}
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
