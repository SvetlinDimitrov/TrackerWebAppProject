import React , { useContext } from "react";
import styles from "./FoodSection.module.css";
import { NutritionRecordContext } from "../../../context/NutritionRecordContext";

const FoodSection = () => {
  const { allRecords , setAllRecord } = useContext(NutritionRecordContext);

  return (
    <div className={styles.mainContainer}>
      <h1 className={styles.heading}>Food Section</h1>
      <div className={styles.containerInfo}>
        <p className={styles.paragraph}>
          Make the perfect balanced food record
        </p>
        <select defaultValue="" className={styles.selectValue}>
          <option value="">Choose Record</option>
        </select>
      </div>
    </div>
  );
};

export default FoodSection;
