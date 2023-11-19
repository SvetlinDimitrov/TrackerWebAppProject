import React, { useState, useEffect } from "react";
import styles from "../../HeathTracker.module.css";
import styles2 from "./FoodSection.module.css";
import {
  getStorageById,
  getTotalFoodCalories,
} from "../../../../util/RecordUtils";

const FoodSection = ({ selectedRecord }) => {
  const [selected, setSelected] = useState(-1);
  const [storage, setStorage] = useState(undefined);

  useEffect(() => {
    setStorage(getStorageById(selectedRecord, selected));
  }, [selected, selectedRecord]);

  const handleAddFood = () => {
    // code to add a new food item
  };
  return (
    <div className={styles.container}>
      <h2 className={styles.container_header}>
        Select Schema to list all the food items
      </h2>
      <select
        value={selected}
        onChange={(e) => setSelected(e.target.value)}
        className={styles.choose_RecordContainer_selectValue}
      >
        <option value={-1}>Choose Storage</option>
        {selectedRecord.storageViews.map((storageData) => (
          <option key={storageData.id} value={storageData.id}>
            {storageData.name}
          </option>
        ))}
      </select>
      {storage && (
        <>
          <div className={styles2.totalCount}>
            Total Count: {getTotalFoodCalories(storage.foods)}
          </div>
          <div className={styles2.foodDetails}>
            <div className={styles2.foodItem}>
              <div className={styles.foodItemDetails}>
                <div>Name: </div>
                <div>Measurement: </div>
              </div>
              <div className={styles.foodItemDetails}>
                <div>Calories: </div>
                <div>Remove: </div>
              </div>
            </div>
            <div className={styles2.foodItem}>
              <div className={styles.foodItemDetails}>
                <div>Name: </div>
                <div>Measurement: </div>
              </div>
              <div>Calories: </div>
            </div>
            <div className={styles2.foodItem}>
              <div className={styles.foodItemDetails}>
                <div>Name: </div>
                <div>Measurement: </div>
              </div>
              <div>Calories: </div>
            </div>
          </div>
          <button className={styles2.addButton} onClick={handleAddFood}>
            Add Food
          </button>
        </>
      )}
    </div>
  );
};

export default FoodSection;
