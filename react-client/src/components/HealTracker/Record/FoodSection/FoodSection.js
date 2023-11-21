import React, { useState, useEffect } from "react";
import styles from "../../HeathTracker.module.css";
import styles2 from "./FoodSection.module.css";
import {
  getStorageById,
  getTotalFoodCaloriesFromStorage,
} from "../../../../util/RecordUtils.js";
import api from "../../../../util/api";
import { useNavigate } from "react-router-dom";
import { AddFoodMenu } from "./AddFoodMenu.js";

const FoodSection = ({
  selectedRecord,
  userToken,
  setSuccessfulMessage,
  setFailedMessage,
  selectedStorage,
  setSelectedStorage,
}) => {
  const [selected, setSelected] = useState(-1);
  const [showAddFoodMenu, setShowAddFoodMenu] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    setSelectedStorage(getStorageById(selectedRecord, selected));
  }, [selected, selectedRecord , setSelectedStorage]);

  const handleDelete = async (foodName) => {
    if (window.confirm("Are you sure you want to delete this item?")) {
      try {
        await api.patch(
          `/storage/removeFood`,
          {
            foodName,
            recordId: selectedRecord.id,
            storageId: selectedStorage.id,
          },
          {
            headers: { Authorization: `Bearer ${userToken}` },
          }
        );
        setSuccessfulMessage({
          message: foodName + " deleted successfully!",
          flag: true,
        });
      } catch (error) {
        setFailedMessage({
          message:
            "Something went wrong with food deletion. Please try again later!",
          flag: true,
        });
      }
      navigate("/health-tracker");
    }
  };

  return (
    <>
      {showAddFoodMenu && (
        <AddFoodMenu
          onClose={setShowAddFoodMenu}
          setFailedMessage={setFailedMessage}
          setSuccessfulMessage={setSuccessfulMessage}
          userToken={userToken}
          selectedRecord={selectedRecord}
          storage={selectedStorage}
          setSelectedStorage={setSelectedStorage}
        />
      )}
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
        {selectedStorage !== undefined && (
          <>
            <div className={styles2.totalCount}>
              Total Count: {getTotalFoodCaloriesFromStorage(selectedStorage)}
            </div>
            <div className={styles2.foodDetails}>
              {selectedStorage.foods.map((food, index) => (
                <div className={styles2.foodItem} key={food.name + index}>
                  <div className={styles.foodItemDetails}>
                    <div>Name: {food.name}</div>
                    <div>
                      {food.size} {food.measurement}
                    </div>
                  </div>
                  <div className={styles.foodItemDetails}>
                    <div>Calories: {food.calories}</div>
                    <button
                      className={styles2.deleteButton}
                      onClick={() => handleDelete(food.name)}
                    >
                      🗑️ Delete
                    </button>
                  </div>
                </div>
              ))}
            </div>
            <button
              className={styles2.addButton}
              onClick={() => setShowAddFoodMenu(true)}
            >
              Add Food
            </button>
          </>
        )}
      </div>
    </>
  );
};

export default FoodSection;
