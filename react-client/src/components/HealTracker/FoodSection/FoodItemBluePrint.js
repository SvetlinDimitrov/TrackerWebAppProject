import { faCheck } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { useState, useContext } from "react";

import { Gauge2 } from "../../../util/Tools";
import api from "../../../util/api";
import styles from "./FoodItemBluePrint.module.css";
import { AuthContext } from "../../../context/UserCredentials";

const FoodItemBluePrint = ({
  foodInfo,
  handleDeleteFood,
  handleAddFood,
  handleEditFood,
  onClose,
}) => {
  const [showVitamins, setShowVitamins] = useState(false);
  const [showMinerals, setShowMinerals] = useState(false);
  const [showMacros, setShowMacros] = useState(false);
  const [inputValue, setInputValue] = useState(foodInfo.size);
  const [food, setFood] = useState(foodInfo);

  const { user } = useContext(AuthContext);
  const userToken = user.tokenInfo.token;

  const changeFoodSize = async (e) => {
    e.preventDefault();
    try {
      const response = await api.patch(
        `/food/calculate?amount=${inputValue}`,
        food,
        { headers: { Authorization: `Bearer ${userToken}` } }
      );
      setFood(response.data);
    } catch (error) {
      console.log(error);
    }
  };

  return (
    <>
      <div className={styles.overlay}>
        <button className={styles.closeButton} onClick={onClose}>
          X
        </button>
        <div className={styles.container}>
          <div className={styles.container_row}>
            <h3>{handleAddFood ? "Add Food" : "Edit Food"}</h3>
            <button
              className={styles.container_row_acceptButton}
              onClick={() =>
                handleAddFood ? handleAddFood(food) : handleEditFood(food)
              }
            >
              <FontAwesomeIcon icon={faCheck} />
            </button>
          </div>

          <div className={styles.container_row}>
            <span>{food.name}</span>
          </div>
          <div className={styles.container_row}>
            <span>Calories</span>
            <span>{food.calories}</span>
          </div>
          <div className={styles.container_gauge}>
            <Gauge2
              width={300}
              height={130}
              diameter={120}
              legendRectSize={15}
              legendSpacing={15}
              fat={food.fat}
              protein={food.protein}
              carbohydrates={food.carbohydrates}
            />
          </div>
          <div className={styles.container_row}>
            <div className={styles.container_row_changeSize}>
              <span>Serving Size In Grams</span>
              <form
                onSubmit={(e) => {
                  changeFoodSize(e);
                }}
              >
                <input
                  type="number"
                  min={1}
                  max={100000}
                  step={0.01}
                  value={inputValue}
                  onChange={(e) => setInputValue(e.target.value)}
                />
                <button type="submit">Change Size</button>
              </form>
            </div>
          </div>
          <div
            className={styles.container_container}
            onClick={() => setShowVitamins(!showVitamins)}
          >
            Vitamins Information
          </div>
          {showVitamins && (
            <>
              <div className={styles.container_row}>
                <span>Vitamin A</span>
                <span>{food.a.toFixed(2)} IU</span>
              </div>
              <div className={styles.container_row}>
                <span>Vitamin D</span>
                <span>{food.d.toFixed(2)} IU</span>
              </div>
              <div className={styles.container_row}>
                <span>Vitamin E</span>
                <span>{food.e.toFixed(2)} mg</span>
              </div>
              <div className={styles.container_row}>
                <span>Vitamin K</span>
                <span>{food.k.toFixed(2)} mcg</span>
              </div>
              <div className={styles.container_row}>
                <span>Vitamin C</span>
                <span>{food.c.toFixed(2)} mg</span>
              </div>
              <div className={styles.container_row}>
                <span>Vitamin B1</span>
                <span>{food.b1.toFixed(2)} mg</span>
              </div>
              <div className={styles.container_row}>
                <span>Vitamin B2</span>
                <span>{food.b2.toFixed(2)} mg</span>
              </div>
              <div className={styles.container_row}>
                <span>Vitamin B3</span>
                <span>{food.b3.toFixed(2)} mg</span>
              </div>
              <div className={styles.container_row}>
                <span>Vitamin B5</span>
                <span>{food.b5.toFixed(2)} mg</span>
              </div>
              <div className={styles.container_row}>
                <span>Vitamin B6</span>
                <span>{food.b6.toFixed(2)} mcg</span>
              </div>
              <div className={styles.container_row}>
                <span>Vitamin B7</span>
                <span>{food.b7.toFixed(2)} mcg</span>
              </div>
              <div className={styles.container_row}>
                <span>Vitamin B9</span>
                <span>{food.b9.toFixed(2)} mcg</span>
              </div>
              <div className={styles.container_row}>
                <span>Vitamin B12</span>
                <span>{food.b12.toFixed(2)} mcg</span>
              </div>
            </>
          )}
          <div
            className={styles.container_container}
            onClick={() => setShowMinerals(!showMinerals)}
          >
            Minerals Information
          </div>
          {showMinerals && (
            <>
              <div className={styles.container_row}>
                <span>Calcium</span>
                <span>{food.calcium.toFixed(2)} mg</span>
              </div>
              <div className={styles.container_row}>
                <span>Phosphorus</span>
                <span>{food.phosphorus.toFixed(2)} mg</span>
              </div>
              <div className={styles.container_row}>
                <span>Magnesium</span>
                <span>{food.magnesium.toFixed(2)} mg</span>
              </div>
              <div className={styles.container_row}>
                <span>Sodium</span>
                <span>{food.sodium.toFixed(2)} mg</span>
              </div>
              <div className={styles.container_row}>
                <span>Potassium</span>
                <span>{food.potassium.toFixed(2)} mg</span>
              </div>
              {/* <div className={styles.container_row}>
                <span>Chloride</span>
                <span>{food.chloride.toFixed(2)} mg</span>
              </div> */}
              <div className={styles.container_row}>
                <span>Iron</span>
                <span>{food.iron.toFixed(2)} mg</span>
              </div>
              <div className={styles.container_row}>
                <span>Zinc</span>
                <span>{food.zinc.toFixed(2)} mg</span>
              </div>
              <div className={styles.container_row}>
                <span>Copper</span>
                <span>{food.copper.toFixed(2)} mg</span>
              </div>
              <div className={styles.container_row}>
                <span>Manganese</span>
                <span>{food.manganese.toFixed(2)} mg</span>
              </div>
              <div className={styles.container_row}>
                <span>Iodine</span>
                <span>{food.iodine.toFixed(2)} mcg</span>
              </div>
              <div className={styles.container_row}>
                <span>Selenium</span>
                <span>{food.selenium.toFixed(2)} mcg</span>
              </div>
              {/* <div className={styles.container_row}>
                <span>Fluoride</span>
                <span>{food.fluoride.toFixed(2)} mg</span>
              </div> */}
              {/* <div className={styles.container_row}>
                <span>Chromium</span>
                <span>{food.chromium.toFixed(2)} mcg</span>
              </div> */}
              <div className={styles.container_row}>
                <span>Molybdenum</span>
                <span>{food.molybdenum.toFixed(2)} mcg</span>
              </div>
            </>
          )}
          <div
            className={styles.container_container}
            onClick={() => setShowMacros(!showMacros)}
          >
            Macros Information
          </div>
          {showMacros && (
            <>
              <div className={styles.container_row}>
                <span>Protein</span>
                <span>{food.protein.toFixed(2)} g</span>
              </div>
              <div className={styles.container_row}>
                <span>Carbohydrates</span>
                <span>{food.carbohydrates.toFixed(2)} g</span>
              </div>
              <div className={styles.container_row}>
                <span>Fiber</span>
                <span>{food.fiber.toFixed(2)} g</span>
              </div>
              <div className={styles.container_row}>
                <span>Sugar</span>
                <span>{food.sugar.toFixed(2)} g</span>
              </div>
              <div className={styles.container_row}>
                <span>Fat</span>
                <span>{food.fat.toFixed(2)} g</span>
              </div>

              <div className={styles.container_row}>
                <span>Trans Fat</span>
                <span>{food.transFat.toFixed(2)} g</span>
              </div>
              <div className={styles.container_row}>
                <span>Saturated Fat</span>
                <span>{food.saturatedFat.toFixed(2)} g</span>
              </div>

              <div className={styles.container_row}>
                <span>Polyunsaturated Fat</span>
                <span>{food.polyunsaturatedFat.toFixed(2)} g</span>
              </div>
              <div className={styles.container_row}>
                <span>Monounsaturated Fat</span>
                <span>{food.monounsaturatedFat.toFixed(2)} g</span>
              </div>
            </>
          )}
          <div className={styles.container_specialRow}>
            {!handleAddFood && (
              <button
                className={styles.container_specialRow_DeleteButton}
                onClick={() => handleDeleteFood(food)}
              >
                Delete Food
              </button>
            )}
          </div>
        </div>
      </div>
    </>
  );
};

export default FoodItemBluePrint;
