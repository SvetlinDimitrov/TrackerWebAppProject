import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

import { AddFoodMenu } from "./AddFoodMenu.js";
import FoodItem from "./FoodItem.js";
import styles from "./FoodSection.module.css";
import api from "../../../util/api.js";

const FoodSection = ({
  recordId,
  userToken,
  setSuccessfulMessage,
  setFailedMessage,
  selectedStorage,
}) => {
  const [showAddFoodMenu, setShowAddFoodMenu] = useState(false);
  const [food, setFood] = useState(undefined);
  const navigate = useNavigate();

  const handleViewFood = (food) => {
    setFood(food);
  };

  const changeFood = async (food, length) => {
    try {
      await api.patch(
        `/storage/${selectedStorage.id}/changeFood?recordId=${recordId}`,
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

  if(selectedStorage === undefined){
    return <div id="preloader"></div>;
  }

  return (
    <>
      {food && (
        <FoodItem
          setShowFood={setFood}
          showFood={food}
          fun={"Edit Food"}
          onAddChangeFunction={changeFood}
          storage={selectedStorage}
          recordId={recordId}
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
          recordId={recordId}
          storage={selectedStorage}
        />
      )}
      <div className={styles.container}>
      <h2>
        Selected foods from {selectedStorage.name}
      </h2>
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
      </div>
    </>
  );
};

export default FoodSection;
