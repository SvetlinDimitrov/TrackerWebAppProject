import {
  faCaretDown,
  faCaretUp,
  faCheck,
} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { Gauge2 } from "../Tools";
import api from "../../../../util/api";
import ChangeLength from "./ChangeLength";
import styles from "./FoodItem.module.css";

const FoodItem = ({
  fun,
  showFood,
  setShowFood,
  userToken,
  onAddChangeFunction,
  storage,
  record,
  setSuccessfulMessage,
  setFailedMessage,
}) => {
  const [showVitamins, setShowVitamins] = useState(false);
  const [showMinerals, setShowMinerals] = useState(false);
  const [showMacros, setShowMacros] = useState(false);
  const [food, setFood] = useState(showFood);
  const [showLength, setShowLength] = useState(false);
  const [length, setLength] = useState(food.size);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchData = async () => {
      const response = await api.get(`/food/${food.name}?amount=${length}`);
      setFood(response.data);
      setLength(response.data.size);
    };

    if (length !== undefined) {
      fetchData();
    }
    setShowLength(false);
  }, [length, food.name]);

  const handleDeleteFood = async () => {
    if (window.confirm("Are you sure you want to delete this item?")) {
      try {
        await api.patch(
          `/storage/${storage.id}/removeFood?foodName=${food.name}&recordId=${record.id}`,
          {},
          {
            headers: { Authorization: `Bearer ${userToken}` },
          }
        );
        setSuccessfulMessage({
          message: food.name + " deleted successfully!",
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

  if (!food) {
    return <div id="preloader"></div>;
  }
  return (
    <>
      {showLength && (
        <ChangeLength
          setShowLength={setShowLength}
          setLength={setLength}
          length={length}
          storage={storage}
          record={record}
          userToken={userToken}
          setFailedMessage={setFailedMessage}
          setSuccessfulMessage={setSuccessfulMessage}
        />
      )}
      <div className={styles.overlay}>
        <div className={styles.foodItem}>
          <div className={styles.row}>
            <h3>{fun}</h3>
            <button
              className={styles.acceptButton}
              onClick={() => onAddChangeFunction(food, length)}
            >
              <FontAwesomeIcon icon={faCheck} />
            </button>
          </div>

          <div className={styles.row}>
            <span>{food.name}</span>
          </div>
          <div className={styles.row}>
            <span>Calories</span>
            <span>{food.calories}</span>
          </div>
          <div className={styles.rowGauge}>
            <Gauge2
              width={300}
              height={120}
              diameter={120}
              legendRectSize={15}
              legendSpacing={15}
              fat={food.fat}
              protein={food.protein}
              carbohydrates={food.carbohydrates}
            />
          </div>
          <div className={styles.row}>
            <span>Serving Size</span>
            <span onClick={() => setShowLength(true)}>{food.size} gram</span>
          </div>
          <div
            className={styles.container}
            onClick={() => setShowVitamins(!showVitamins)}
          >
            <span>More Information about Vitamins</span>
            <button>
              <FontAwesomeIcon icon={showVitamins ? faCaretDown : faCaretUp} />
            </button>
          </div>
          {showVitamins && (
            <>
              <div className={styles.row}>
                <span>Vitamin A</span>
                <span>{food.a.toFixed(2)}</span>
              </div>
              <div className={styles.row}>
                <span>Vitamin D</span>
                <span>{food.d.toFixed(2)}</span>
              </div>
              <div className={styles.row}>
                <span>Vitamin E</span>
                <span>{food.e.toFixed(2)}</span>
              </div>
              <div className={styles.row}>
                <span>Vitamin K</span>
                <span>{food.k.toFixed(2)}</span>
              </div>
              <div className={styles.row}>
                <span>Vitamin C</span>
                <span>{food.c.toFixed(2)}</span>
              </div>
              <div className={styles.row}>
                <span>Vitamin B1</span>
                <span>{food.b1.toFixed(2)}</span>
              </div>
              <div className={styles.row}>
                <span>Vitamin B2</span>
                <span>{food.b2.toFixed(2)}</span>
              </div>
              <div className={styles.row}>
                <span>Vitamin B3</span>
                <span>{food.b3.toFixed(2)}</span>
              </div>
              <div className={styles.row}>
                <span>Vitamin B5</span>
                <span>{food.b5.toFixed(2)}</span>
              </div>
              <div className={styles.row}>
                <span>Vitamin B6</span>
                <span>{food.b6.toFixed(2)}</span>
              </div>
              <div className={styles.row}>
                <span>Vitamin B7</span>
                <span>{food.b7.toFixed(2)}</span>
              </div>
              <div className={styles.row}>
                <span>Vitamin B9</span>
                <span>{food.b9.toFixed(2)}</span>
              </div>
              <div className={styles.row}>
                <span>Vitamin B12</span>
                <span>{food.b12.toFixed(2)}</span>
              </div>
            </>
          )}
          <div
            className={styles.container}
            onClick={() => setShowMinerals(!showMinerals)}
          >
            <span>More Information about Minerals</span>
            <button>
              <FontAwesomeIcon icon={showMinerals ? faCaretDown : faCaretUp} />
            </button>
          </div>
          {showMinerals && (
            <>
              <div className={styles.row}>
                <span>Calcium</span>
                <span>{food.calcium.toFixed(2)}</span>
              </div>
              <div className={styles.row}>
                <span>Phosphorus</span>
                <span>{food.phosphorus.toFixed(2)}</span>
              </div>
              <div className={styles.row}>
                <span>Magnesium</span>
                <span>{food.magnesium.toFixed(2)}</span>
              </div>
              <div className={styles.row}>
                <span>Sodium</span>
                <span>{food.sodium.toFixed(2)}</span>
              </div>
              <div className={styles.row}>
                <span>Potassium</span>
                <span>{food.potassium.toFixed(2)}</span>
              </div>
              <div className={styles.row}>
                <span>Chloride</span>
                <span>{food.chloride.toFixed(2)}</span>
              </div>
              <div className={styles.row}>
                <span>Iron</span>
                <span>{food.iron.toFixed(2)}</span>
              </div>
              <div className={styles.row}>
                <span>Zinc</span>
                <span>{food.zinc.toFixed(2)}</span>
              </div>
              <div className={styles.row}>
                <span>Copper</span>
                <span>{food.copper.toFixed(2)}</span>
              </div>
              <div className={styles.row}>
                <span>Manganese</span>
                <span>{food.manganese.toFixed(2)}</span>
              </div>
              <div className={styles.row}>
                <span>Iodine</span>
                <span>{food.iodine.toFixed(2)}</span>
              </div>
              <div className={styles.row}>
                <span>Selenium</span>
                <span>{food.selenium.toFixed(2)}</span>
              </div>
              <div className={styles.row}>
                <span>Fluoride</span>
                <span>{food.fluoride.toFixed(2)}</span>
              </div>
              <div className={styles.row}>
                <span>Chromium</span>
                <span>{food.chromium.toFixed(2)}</span>
              </div>
              <div className={styles.row}>
                <span>Molybdenum</span>
                <span>{food.molybdenum.toFixed(2)}</span>
              </div>
            </>
          )}
          <div
            className={styles.container}
            onClick={() => setShowMacros(!showMacros)}
          >
            <span>More Information about Macros</span>
            <button>
              <FontAwesomeIcon icon={showMacros ? faCaretDown : faCaretUp} />
            </button>
          </div>
          {showMacros && (
            <>
              <div className={styles.row}>
                <span>Protein</span>
                <span>{food.protein.toFixed(2)}</span>
              </div>
              <div className={styles.row}>
                <span>Carbohydrates</span>
                <span>{food.carbohydrates.toFixed(2)}</span>
              </div>
              <div className={styles.row}>
                <span>Fiber</span>
                <span>{food.fiber.toFixed(2)}</span>
              </div>
              <div className={styles.row}>
                <span>Sugar</span>
                <span>{food.sugar.toFixed(2)}</span>
              </div>
              <div className={styles.row}>
                <span>Fat</span>
                <span>{food.fat.toFixed(2)}</span>
              </div>

              <div className={styles.row}>
                <span>Trans Fat</span>
                <span>{food.transFat.toFixed(2)}</span>
              </div>
              <div className={styles.row}>
                <span>Saturated Fat</span>
                <span>{food.saturatedFat.toFixed(2)}</span>
              </div>

              <div className={styles.row}>
                <span>Polyunsaturated Fat</span>
                <span>{food.polyunsaturatedFat.toFixed(2)}</span>
              </div>
              <div className={styles.row}>
                <span>Monounsaturated Fat</span>
                <span>{food.monounsaturatedFat.toFixed(2)}</span>
              </div>
            </>
          )}
          <div className={styles.specialRow}>
            <div className={styles.closeButtonContainer}>
              <button
                className={styles.closeButton}
                onClick={() => setShowFood(undefined)}
              >
                Close
              </button>
            </div>
            {fun === "Edit Food" && (
              <div className={styles.closeButtonContainer}>
                <button
                  className={styles.closeButton}
                  onClick={() => handleDeleteFood()}
                >
                  Delete
                </button>
              </div>
            )}
          </div>
        </div>
      </div>
    </>
  );
};

export default FoodItem;
