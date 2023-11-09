import { useState} from "react";

import styles from "./Nutrient.module.css";
import { Outlet, useNavigate , useParams} from "react-router-dom";

const NutrientInfo = () => {
  const [show, setShow] = useState("");
  const navigate = useNavigate();
  const { nutrition, nutritionType } = useParams();

  const handleElectrolytesClick = () => {
    navigate("/nutrientInfo");
    if (show === "Electrolytes") {
      setShow("");
    } else {
      setShow("Electrolytes");
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
    if(nutrition === undefined && nutritionType === undefined){
      navigate("/nutrientInfo/" + nutrient + "/" + nutrientType);
    }else if(nutrition === nutrient && nutritionType === nutrientType){
      navigate("/nutrientInfo");
    }else{
      navigate("/nutrientInfo/" + nutrient + "/" + nutrientType);
    }
  };

  return (
    <div className={styles.body}>
      <h1 className={styles.h1}>Click for addition information</h1>
      <div className={styles.container}>
        <div className={styles.category} onClick={handleElectrolytesClick}>
          <p className={styles.categoryParagraph}>Electrolytes</p>
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

      {show === "Electrolytes" ? (
        <div className={styles.containerElectrolytes}>
          <div className={styles.category} onClick={() => handleCategoryClick("electrolyte", "Sodium")}>
            <p className={styles.categoryParagraph}>Sodium</p>
          </div>
          <div className={styles.category} onClick={() => handleCategoryClick("electrolyte", "Potassium")}>
            <p className={styles.categoryParagraph}>Potassium</p>
          </div>
          <div className={styles.category} onClick={() => handleCategoryClick("electrolyte", "Calcium")}>
            <p className={styles.categoryParagraph}>Calcium</p>
          </div>
          <div className={styles.category} onClick={() => handleCategoryClick("electrolyte", "Magnesium")}>
            <p className={styles.categoryParagraph}>Magnesium</p>
          </div>
          <div className={styles.category} onClick={() => handleCategoryClick("electrolyte", "Chloride")}>
            <p className={styles.categoryParagraph}>Chloride</p>
          </div>
        </div>
      ) : (
        <></>
      )}

      {show === "Macronutrients" ? (
        <div className={styles.containerMacronutrients}>
          <div className={styles.category} onClick={() => handleCategoryClick("macronutrient", "Carbohydrates")}>
            <p className={styles.categoryParagraph}>Carbohydrates</p>
          </div>
          <div className={styles.category} onClick={() => handleCategoryClick("macronutrient", "Protein")}>
            <p className={styles.categoryParagraph}>Protein</p>
          </div>
          <div className={styles.category} onClick={() => handleCategoryClick("macronutrient", "Fat")}>
            <p className={styles.categoryParagraph}>Fat</p>
          </div>
        </div>
      ) : (
        <></>
      )}

      <Outlet />
    </div>
  );
};

export default NutrientInfo;
