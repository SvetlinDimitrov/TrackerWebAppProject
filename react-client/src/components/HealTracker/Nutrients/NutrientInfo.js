import { useState } from "react";

import styles from "./NutrientInfo.module.css";

import { Outlet, useNavigate, useParams } from "react-router-dom";

const NutrientInfo = () => {
  const [show, setShow] = useState("");
  const navigate = useNavigate();
  const { nutrition, nutritionType } = useParams();

  const handleElectrolytesClick = () => {
    navigate("/nutrientInfo");
    if (show === "Mineral") {
      setShow("");
    } else {
      setShow("Mineral");
    }
  };

  const handleMacronutrientsClick = () => {
    navigate("/nutrientInfo");
    if (show === "Macronutrients") {
      setShow("");
    } else {
      setShow("Macronutrients");
    }
  };

  const handleVitaminsClick = () => {
    navigate("/nutrientInfo");
    if (show === "Vitamins") {
      setShow("");
    } else {
      setShow("Vitamins");
    }
  };

  const handleCategoryClick = (nutrient, nutrientType) => {
    if (nutrition === undefined && nutritionType === undefined) {
      navigate("/nutrientInfo/" + nutrient + "/" + nutrientType);
    } else if (nutrition === nutrient && nutritionType === nutrientType) {
      navigate("/nutrientInfo");
    } else {
      navigate("/nutrientInfo/" + nutrient + "/" + nutrientType);
    }
  };

  return (
    <div className={styles.body}>
      <h1 className={styles.body_h1}>Click for addition information</h1>
      <div className={styles.body_container}>
        <div className={styles.body_container_category} onClick={handleElectrolytesClick}>
          <p className={styles.body_container_category_p}>Mineral</p>
        </div>
        <div className={styles.body_container_category} onClick={handleMacronutrientsClick}>
          <p className={styles.body_container_category_p}>Macronutrients</p>
        </div>
        <div className={styles.body_container_category} onClick={handleVitaminsClick}>
          <p className={styles.body_container_category_p}>Vitamins</p>
        </div>
      </div>

      {show === "Vitamins" ? (
        <div className={styles.body_containerVitamins}>
          <div
            className={styles.body_container_category}
            onClick={() => handleCategoryClick("vitamin", "A")}
          >
            <p className={styles.body_container_category_p}>A</p>
          </div>
          <div
            className={styles.body_container_category}
            onClick={() => handleCategoryClick("vitamin", "D")}
          >
            <p className={styles.body_container_category_p}>D</p>
          </div>
          <div
            className={styles.body_container_category}
            onClick={() => handleCategoryClick("vitamin", "E")}
          >
            <p className={styles.body_container_category_p}>E</p>
          </div>
          <div
            className={styles.body_container_category}
            onClick={() => handleCategoryClick("vitamin", "K")}
          >
            <p className={styles.body_container_category_p}>K</p>
          </div>
          <div
            className={styles.body_container_category}
            onClick={() => handleCategoryClick("vitamin", "C")}
          >
            <p className={styles.body_container_category_p}>C</p>
          </div>
          <div
            className={styles.body_container_category}
            onClick={() => handleCategoryClick("vitamin", "B1")}
          >
            <p className={styles.body_container_category_p}>B1</p>
          </div>
          <div
            className={styles.body_container_category}
            onClick={() => handleCategoryClick("vitamin", "B2")}
          >
            <p className={styles.body_container_category_p}>B2</p>
          </div>
          <div
            className={styles.body_container_category}
            onClick={() => handleCategoryClick("vitamin", "B3")}
          >
            <p className={styles.body_container_category_p}>B3</p>
          </div>
          <div
            className={styles.body_container_category}
            onClick={() => handleCategoryClick("vitamin", "B5")}
          >
            <p className={styles.body_container_category_p}>B5</p>
          </div>
          <div
            className={styles.body_container_category}
            onClick={() => handleCategoryClick("vitamin", "B6")}
          >
            <p className={styles.body_container_category_p}>B6</p>
          </div>
          <div
            className={styles.body_container_category}
            onClick={() => handleCategoryClick("vitamin", "B7")}
          >
            <p className={styles.body_container_category_p}>B7</p>
          </div>
          <div
            className={styles.body_container_category}
            onClick={() => handleCategoryClick("vitamin", "B9")}
          >
            <p className={styles.body_container_category_p}>B9</p>
          </div>
          <div
            className={styles.body_container_category}
            onClick={() => handleCategoryClick("vitamin", "B12")}
          >
            <p className={styles.body_container_category_p}>B12</p>
          </div>
        </div>
      ) : (
        <></>
      )}

      {show === "Mineral" ? (
        <div className={styles.body_containerMinerals}>
          <div
            className={styles.body_container_category}
            onClick={() => handleCategoryClick("mineral", "Calcium")}
          >
            <p className={styles.body_container_category_p}>Calcium</p>
          </div>
          <div
            className={styles.body_container_category}
            onClick={() => handleCategoryClick("mineral", "Phosphorus")}
          >
            <p className={styles.body_container_category_p}>Phosphorus</p>
          </div>
          <div
            className={styles.body_container_category}
            onClick={() => handleCategoryClick("mineral", "Magnesium")}
          >
            <p className={styles.body_container_category_p}>Magnesium</p>
          </div>
          <div
            className={styles.body_container_category}
            onClick={() => handleCategoryClick("mineral", "Sodium")}
          >
            <p className={styles.body_container_category_p}>Sodium</p>
          </div>
          <div
            className={styles.body_container_category}
            onClick={() => handleCategoryClick("mineral", "Potassium")}
          >
            <p className={styles.body_container_category_p}>Potassium</p>
          </div>
          <div
            className={styles.body_container_category}
            onClick={() => handleCategoryClick("mineral", "Chloride")}
          >
            <p className={styles.body_container_category_p}>Chloride</p>
          </div>
          <div
            className={styles.body_container_category}
            onClick={() => handleCategoryClick("mineral", "Iron")}
          >
            <p className={styles.body_container_category_p}>Iron</p>
          </div>
          <div
            className={styles.body_container_category}
            onClick={() => handleCategoryClick("mineral", "Zinc")}
          >
            <p className={styles.body_container_category_p}>Zinc</p>
          </div>
          <div
            className={styles.body_container_category}
            onClick={() => handleCategoryClick("mineral", "Copper")}
          >
            <p className={styles.body_container_category_p}>Copper</p>
          </div>
          <div
            className={styles.body_container_category}
            onClick={() => handleCategoryClick("mineral", "Manganese")}
          >
            <p className={styles.body_container_category_p}>Manganese</p>
          </div>
          <div
            className={styles.body_container_category}
            onClick={() => handleCategoryClick("mineral", "Selenium")}
          >
            <p className={styles.body_container_category_p}>Selenium</p>
          </div>
          <div
            className={styles.body_container_category}
            onClick={() => handleCategoryClick("mineral", "Iodine")}
          >
            <p className={styles.body_container_category_p}>Iodine</p>
          </div>
          <div
            className={styles.body_container_category}
            onClick={() => handleCategoryClick("mineral", "Fluoride")}
          >
            <p className={styles.body_container_category_p}>Fluoride</p>
          </div>
          <div
            className={styles.body_container_category}
            onClick={() => handleCategoryClick("mineral", "Chromium")}
          >
            <p className={styles.body_container_category_p}>Chromium</p>
          </div>
          <div
            className={styles.body_container_category}
            onClick={() => handleCategoryClick("mineral", "Molybdenum")}
          >
            <p className={styles.body_container_category_p}>Molybdenum</p>
          </div>
        </div>
      ) : (
        <></>
      )}

      {show === "Macronutrients" && (
        <>
          <div className={styles.body_containerMacronutrients}>
            <div
              className={styles.body_container_category}
              onClick={() =>
                handleCategoryClick("macronutrient", "Carbohydrates")
              }
            >
              <p className={styles.body_container_category_p}>Carbohydrates</p>
            </div>
            <div
              className={styles.body_container_category}
              onClick={() => handleCategoryClick("macronutrient", "Protein")}
            >
              <p className={styles.body_container_category_p}>Protein</p>
            </div>
            <div
              className={styles.body_container_category}
              onClick={() => handleCategoryClick("macronutrient", "Fat")}
            >
              <p className={styles.body_container_category_p}>Fat</p>
            </div>
            <div
              className={styles.body_container_category}
              onClick={() =>
                handleCategoryClick("macronutrientTypes", "Saturated Fat")
              }
            >
              <p className={styles.body_container_category_p}>Saturated Fat</p>
            </div>
            <div
              className={styles.body_container_category}
              onClick={() =>
                handleCategoryClick("macronutrientTypes", "Polyunsaturated Fat")
              }
            >
              <p className={styles.body_container_category_p}>Polyunsaturated Fat</p>
            </div>
            <div
              className={styles.body_container_category}
              onClick={() =>
                handleCategoryClick("macronutrientTypes", "Monounsaturated Fat")
              }
            >
              <p className={styles.body_container_category_p}>Monounsaturated Fat</p>
            </div>
            <div
              className={styles.body_container_category}
              onClick={() =>
                handleCategoryClick("macronutrientTypes", "Trans Fat")
              }
            >
              <p className={styles.body_container_category_p}>Trans Fat</p>
            </div>
            <div
              className={styles.body_container_category}
              onClick={() => handleCategoryClick("macronutrientTypes", "Fiber")}
            >
              <p className={styles.body_container_category_p}>Fiber</p>
            </div>
            <div
              className={styles.body_container_category}
              onClick={() => handleCategoryClick("macronutrientTypes", "Sugar")}
            >
              <p className={styles.body_container_category_p}>Sugar</p>
            </div>
          </div>
        </>
      )}

      <Outlet />
    </div>
  );
};

export default NutrientInfo;
