import React, { useState, useEffect } from "react";
import { FaHeart } from "react-icons/fa";

import styles from "./SelectRecordDetails.module.css";

const SelectRecordDetails = ({ selectedRecord, selectedFeature }) => {
  const [selected, setSelected] = useState(false);

  useEffect(() => {
    selectedFeature("");
    setSelected(false);
  }, [selectedRecord, selectedFeature]);

  const handleSelected = () => {
    if (!selected) {
      setSelected(true);
      selectedFeature("details");
    } else {
      setSelected(false);
      selectedFeature("");
    }
  };
  return (
    <>
      <div className={styles.container}>
        <h2 className={styles.container_header}>
          Selected record {selectedRecord && selectedRecord.name}
        </h2>
        <FaHeart className={styles.container_icon} />
        <p className={styles.container_text}>Get detailed information</p>
        <div className={styles.container_section}>
          <div
            className={styles.container_section_container2}
            onClick={() => handleSelected()}
          >
            {selected ? "Hide Details" : "More Details"}
          </div>
        </div>
      </div>
    </>
  );
};

export default SelectRecordDetails;
