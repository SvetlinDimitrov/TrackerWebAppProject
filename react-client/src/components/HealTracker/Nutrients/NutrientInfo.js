import { useState } from "react";

import styles from "./Nutrient.module.css";
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
      <h1 className={styles.h1}>Click for addition information</h1>
      <div className={styles.container}>
        <div className={styles.category} onClick={handleElectrolytesClick}>
          <p className={styles.categoryParagraph}>Mineral</p>
        </div>
        <div className={styles.category} onClick={handleMacronutrientsClick}>
          <p className={styles.categoryParagraph}>Macronutrients</p>
        </div>
        <div className={styles.category} onClick={handleVitaminsClick}>
          <p className={styles.categoryParagraph}>Vitamins</p>
        </div>
      </div>

      {show === "Vitamins" ? (
        <div className={styles.containerVitamins}>
          <div
            className={styles.category}
            onClick={() => handleCategoryClick("vitamin", "A")}
          >
            <p className={styles.categoryParagraph}>A</p>
          </div>
          <div
            className={styles.category}
            onClick={() => handleCategoryClick("vitamin", "D")}
          >
            <p className={styles.categoryParagraph}>D</p>
          </div>
          <div
            className={styles.category}
            onClick={() => handleCategoryClick("vitamin", "E")}
          >
            <p className={styles.categoryParagraph}>E</p>
          </div>
          <div
            className={styles.category}
            onClick={() => handleCategoryClick("vitamin", "K")}
          >
            <p className={styles.categoryParagraph}>K</p>
          </div>
          <div
            className={styles.category}
            onClick={() => handleCategoryClick("vitamin", "C")}
          >
            <p className={styles.categoryParagraph}>C</p>
          </div>
          <div
            className={styles.category}
            onClick={() => handleCategoryClick("vitamin", "B1")}
          >
            <p className={styles.categoryParagraph}>B1</p>
          </div>
          <div
            className={styles.category}
            onClick={() => handleCategoryClick("vitamin", "B2")}
          >
            <p className={styles.categoryParagraph}>B2</p>
          </div>
          <div
            className={styles.category}
            onClick={() => handleCategoryClick("vitamin", "B3")}
          >
            <p className={styles.categoryParagraph}>B3</p>
          </div>
          <div
            className={styles.category}
            onClick={() => handleCategoryClick("vitamin", "B5")}
          >
            <p className={styles.categoryParagraph}>B5</p>
          </div>
          <div
            className={styles.category}
            onClick={() => handleCategoryClick("vitamin", "B6")}
          >
            <p className={styles.categoryParagraph}>B6</p>
          </div>
          <div
            className={styles.category}
            onClick={() => handleCategoryClick("vitamin", "B7")}
          >
            <p className={styles.categoryParagraph}>B7</p>
          </div>
          <div
            className={styles.category}
            onClick={() => handleCategoryClick("vitamin", "B9")}
          >
            <p className={styles.categoryParagraph}>B9</p>
          </div>
          <div
            className={styles.category}
            onClick={() => handleCategoryClick("vitamin", "B12")}
          >
            <p className={styles.categoryParagraph}>B12</p>
          </div>
        </div>
      ) : (
        <></>
      )}

      {show === "Mineral" ? (
        <div className={styles.containerMinerals}>
          <div
            className={styles.category}
            onClick={() => handleCategoryClick("mineral", "Calcium")}
          >
            <p className={styles.categoryParagraph}>Calcium</p>
          </div>
          <div
            className={styles.category}
            onClick={() => handleCategoryClick("mineral", "Phosphorus")}
          >
            <p className={styles.categoryParagraph}>Phosphorus</p>
          </div>
          <div
            className={styles.category}
            onClick={() => handleCategoryClick("mineral", "Magnesium")}
          >
            <p className={styles.categoryParagraph}>Magnesium</p>
          </div>
          <div
            className={styles.category}
            onClick={() => handleCategoryClick("mineral", "Sodium")}
          >
            <p className={styles.categoryParagraph}>Sodium</p>
          </div>
          <div
            className={styles.category}
            onClick={() => handleCategoryClick("mineral", "Potassium")}
          >
            <p className={styles.categoryParagraph}>Potassium</p>
          </div>
          <div
            className={styles.category}
            onClick={() => handleCategoryClick("mineral", "Chloride")}
          >
            <p className={styles.categoryParagraph}>Chloride</p>
          </div>
          <div
            className={styles.category}
            onClick={() => handleCategoryClick("mineral", "Iron")}
          >
            <p className={styles.categoryParagraph}>Iron</p>
          </div>
          <div
            className={styles.category}
            onClick={() => handleCategoryClick("mineral", "Zinc")}
          >
            <p className={styles.categoryParagraph}>Zinc</p>
          </div>
          <div
            className={styles.category}
            onClick={() => handleCategoryClick("mineral", "Copper")}
          >
            <p className={styles.categoryParagraph}>Copper</p>
          </div>
          <div
            className={styles.category}
            onClick={() => handleCategoryClick("mineral", "Manganese")}
          >
            <p className={styles.categoryParagraph}>Manganese</p>
          </div>
          <div
            className={styles.category}
            onClick={() => handleCategoryClick("mineral", "Selenium")}
          >
            <p className={styles.categoryParagraph}>Selenium</p>
          </div>
          <div
            className={styles.category}
            onClick={() => handleCategoryClick("mineral", "Iodine")}
          >
            <p className={styles.categoryParagraph}>Iodine</p>
          </div>
          <div
            className={styles.category}
            onClick={() => handleCategoryClick("mineral", "Fluoride")}
          >
            <p className={styles.categoryParagraph}>Fluoride</p>
          </div>
          <div
            className={styles.category}
            onClick={() => handleCategoryClick("mineral", "Chromium")}
          >
            <p className={styles.categoryParagraph}>Chromium</p>
          </div>
          <div
            className={styles.category}
            onClick={() => handleCategoryClick("mineral", "Molybdenum")}
          >
            <p className={styles.categoryParagraph}>Molybdenum</p>
          </div>
        </div>
      ) : (
        <></>
      )}

      {show === "Macronutrients" && (
        <>
          <div className={styles.containerMacronutrients}>
            <div
              className={styles.category}
              onClick={() =>
                handleCategoryClick("macronutrient", "Carbohydrates")
              }
            >
              <p className={styles.categoryParagraph}>Carbohydrates</p>
            </div>
            <div
              className={styles.category}
              onClick={() => handleCategoryClick("macronutrient", "Protein")}
            >
              <p className={styles.categoryParagraph}>Protein</p>
            </div>
            <div
              className={styles.category}
              onClick={() => handleCategoryClick("macronutrient", "Fat")}
            >
              <p className={styles.categoryParagraph}>Fat</p>
            </div>
            <div
              className={styles.category}
              onClick={() =>
                handleCategoryClick("macronutrientTypes", "Saturated Fat")
              }
            >
              <p className={styles.categoryParagraph}>Saturated Fat</p>
            </div>
            <div
              className={styles.category}
              onClick={() =>
                handleCategoryClick("macronutrientTypes", "Polyunsaturated Fat")
              }
            >
              <p className={styles.categoryParagraph}>Polyunsaturated Fat</p>
            </div>
            <div
              className={styles.category}
              onClick={() =>
                handleCategoryClick("macronutrientTypes", "Monounsaturated Fat")
              }
            >
              <p className={styles.categoryParagraph}>Monounsaturated Fat</p>
            </div>
            <div
              className={styles.category}
              onClick={() =>
                handleCategoryClick("macronutrientTypes", "Trans Fat")
              }
            >
              <p className={styles.categoryParagraph}>Trans Fat</p>
            </div>
            <div
              className={styles.category}
              onClick={() => handleCategoryClick("macronutrientTypes", "Fiber")}
            >
              <p className={styles.categoryParagraph}>Fiber</p>
            </div>
            <div
              className={styles.category}
              onClick={() => handleCategoryClick("macronutrientTypes", "Sugar")}
            >
              <p className={styles.categoryParagraph}>Sugar</p>
            </div>
          </div>
        </>
      )}

      <Outlet />
    </div>
  );
};

export default NutrientInfo;
