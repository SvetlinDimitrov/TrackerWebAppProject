import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

import { getStorageById } from "../../../util/RecordUtils.js";
import { AddFoodMenu } from "./AddFoodMenu.js";
import FoodItem from "./FoodItem.js";
import styles from "./FoodSection.module.css";
import api from "../../../util/api.js";


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
  const [food, setFood] = useState(undefined);
  const navigate = useNavigate();

  const handleViewFood = (food) => {
    setFood(food);
  };

  const changeFood = async (food, length) => {
    try {
      await api.patch(
        `/storage/${selectedStorage.id}/changeFood?recordId=${selectedRecord.id}`,
        {
          foodName: food.name,
          amount: length,
        },
        {
          headers: { Authorization: `Bearer ${userToken}` },
        }
      );
      setSuccessfulMessage({
        message: "successfully modified " + food.name + "!",
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
  };
  useEffect(() => {
    setSelectedStorage(getStorageById(selectedRecord, selected));
  }, [selected, selectedRecord, setSelectedStorage]);

  return (
    <>
      {food && (
        <FoodItem
          setShowFood={setFood}
          showFood={food}
          fun={"Edit Food"}
          onAddChangeFunction={changeFood}
          storage={selectedStorage}
          record={selectedRecord}
          userToken={userToken}
          setFailedMessage={setFailedMessage}
          setSuccessfulMessage={setSuccessfulMessage}
        />
      )}
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
          onChange={(e) => setSelected(e.target.value)}        >
          <option value={-1}>Choose Storage</option>
          {selectedRecord.storageViews.map((storageData) => (
            <option key={storageData.id} value={storageData.id}>
              {storageData.name}
            </option>
          ))}
        </select>
        {selectedStorage !== undefined && (
          <>
            <div className={styles.container_totalCount}>
              Total Count: {selectedStorage.consumedCalories}
            </div>
            <div className={styles.container_foodDetails}>
              {selectedStorage.foods.map((food, index) => (
                <div
                  className={styles.container_foodDetails_food}
                  key={food.name + index}
                  onClick={() => handleViewFood(food)}
                >
                  <div className={styles.container_foodDetails_food_info}>
                    <div>{food.name}</div>
                    <div>{food.size} gram</div>
                  </div>
                  <div className={styles.container_foodDetails_food_info}>
                    <div>Calories: {food.calories}</div>
                  </div>
                </div>
              ))}
            </div>
            <button
              className={styles.container_addButton}
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