import { faCheck } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { useState, useContext } from "react";

import { Gauge2 } from "../../../util/Tools";
import api from "../../../util/api";
import styles from "./FoodItemBluePrint.module.css";
import { convertComplexFoodIntoSimpleFood , convertSimpleFoodIntroComplexFood} from "../../../util/FoodUtils";
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
        convertComplexFoodIntoSimpleFood(food),
        { headers: { Authorization: `Bearer ${userToken}` } }
      );
      setFood(convertSimpleFoodIntroComplexFood(response.data));
    } catch (error) {
      console.log(error);
    }
  };

  if (!food) {
    return <div id="preloader"></div>;
  }
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
            <span>{food.description}</span>
          </div>
          <div className={styles.container_row}>
            <span>{food.calories.name}</span>
            <span>
              {formatNumber(food.calories.amount)} {food.calories.unit}
            </span>
          </div>
          <div className={styles.container_gauge}>
            <Gauge2
              width={300}
              height={130}
              diameter={120}
              legendRectSize={15}
              legendSpacing={15}
              fat={
                food.macronutrients.find((macro) => macro.name === "Fat").amount
              }
              protein={
                food.macronutrients.find((macro) => macro.name === "Protein")
                  .amount
              }
              carbohydrates={
                food.macronutrients.find(
                  (macro) => macro.name === "Carbohydrates"
                ).amount
              }
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
              {food.vitaminNutrients.map((vitaminNutrient, index) => {
                return (
                  <div className={styles.container_row} key={index}>
                    <span>{vitaminNutrient.name}</span>
                    <span>
                      {formatNumber(vitaminNutrient.amount)}{" "}
                      {vitaminNutrient.unit}
                    </span>
                  </div>
                );
              })}
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
              {food.mineralNutrients.map((vitaminNutrient, index) => {
                return (
                  <div className={styles.container_row} key={index}>
                    <span>{vitaminNutrient.name}</span>
                    <span>
                      {formatNumber(vitaminNutrient.amount)}{" "}
                      {vitaminNutrient.unit}
                    </span>
                  </div>
                );
              })}
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
              {food.macronutrients.map((vitaminNutrient, index) => {
                return (
                  <div className={styles.container_row} key={index}>
                    <span>{vitaminNutrient.name}</span>
                    <span>
                      {formatNumber(vitaminNutrient.amount)}{" "}
                      {vitaminNutrient.unit}
                    </span>
                  </div>
                );
              })}
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
function formatNumber(num) {
  if (Math.abs(Math.round(num) - num) < 0.01) {
    return num;
  } else {
    return num.toFixed(3);
  }
}
