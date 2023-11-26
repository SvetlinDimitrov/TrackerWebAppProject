import { useContext, useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";

import { FoodContext } from "../../../context/FoodContext";
import api from "../../../util/api";
import { BarChart } from "../../../util/Tools";
import styles from "./NutritionTemplate.module.css";

const NutrientTemplate = () => {
  let { nutrition, nutritionType } = useParams();
  const navigate = useNavigate();
  const [nutritionTemplate, setNutritionTemplate] = useState({});
  const { sortedFoods } = useContext(FoodContext);

  useEffect(() => {
    const fetchData = async () => {
      let response;
      try {
        if (nutrition === "macronutrientTypes") {
          response = await api.get("/macronutrient/types/" + nutritionType);
          nutrition = "macronutrient";
        } else {
          response = await api.get("/" + nutrition + "/" + nutritionType);
        }
        const capitalizedNutrition =
          nutrition.charAt(0).toUpperCase() + nutrition.slice(1);
        response.data["type"] = capitalizedNutrition;

        setNutritionTemplate(response.data);
      } catch (error) {
        navigate("/error");
      }
    };
    fetchData();
  }, [navigate, nutrition, nutritionType]);

  return (
    <>
      {nutritionTemplate.name === undefined ? (
        <div id="preloader"></div>
      ) : (
        <div className={styles.body}>
          <div className={styles.container}>
            <div className={styles.section}>
              <h2 className={styles.sectionH2}>
                Name: {`${nutritionTemplate.type} ${nutritionTemplate.name}`}
              </h2>
              <p>{nutritionTemplate.description}</p>
            </div>

            {nutritionTemplate.functions && (
              <div className={styles.section}>
                <h2>Functions:</h2>
                <ul className={styles.functionsUl}>
                  {nutritionTemplate.functions.map((func, index) => (
                    <li key={index}>
                      {func.key && func.key !== "-" && func.key !== "" ? (
                        <>
                          <strong>{func.key}:</strong> {func.value}
                        </>
                      ) : (
                        <>
                          <strong>{func.value}</strong>
                        </>
                      )}
                    </li>
                  ))}
                </ul>
              </div>
            )}

            {nutritionTemplate.sources && (
              <div className={styles.section}>
                <h2>Sources:</h2>
                <ul className={styles.sourcesUl}>
                  {nutritionTemplate.sources.map((func, index) => (
                    <li key={index}>
                      {func.key && func.key !== "-" && func.key !== "" ? (
                        <>
                          <strong>{func.key}:</strong> {func.value}
                        </>
                      ) : (
                        <>
                          <strong>{func.value}</strong>
                        </>
                      )}
                    </li>
                  ))}
                </ul>
              </div>
            )}

            {nutritionTemplate.healthConsiderations && (
              <div className={styles.section}>
                <h2>Health Considerations:</h2>
                <ul className={styles.sourcesUl}>
                  {nutritionTemplate.healthConsiderations.map((func, index) => (
                    <li key={index}>
                      {func.key && func.key !== "-" && func.key !== "" ? (
                        <>
                          <strong>{func.key}:</strong> {func.value}
                        </>
                      ) : (
                        <>
                          <strong>{func.value}</strong>
                        </>
                      )}
                    </li>
                  ))}
                </ul>
              </div>
            )}

            {nutritionTemplate.types && nutritionTemplate.types[0] && (
              <div className={styles.section}>
                <h2>Types:</h2>
                <ul className={styles.sourcesUl}>
                  {nutritionTemplate.types.map((func, index) => (
                    <li key={index}>
                      {func.key && func.key !== "-" && func.key !== "" ? (
                        <>
                          <strong>{func.key}:</strong> {func.value}
                        </>
                      ) : (
                        <>
                          <strong>{func.value}</strong>
                        </>
                      )}
                    </li>
                  ))}
                </ul>
              </div>
            )}

            {nutritionTemplate.dietaryConsiderations && (
              <div className={styles.section}>
                <h2>Dietary Considerations:</h2>
                <ul className={styles.sourcesUl}>
                  {nutritionTemplate.dietaryConsiderations.map(
                    (func, index) => (
                      <li key={index}>
                        {func.key && func.key !== "-" && func.key !== "" ? (
                          <>
                            <strong>{func.key}:</strong> {func.value}
                          </>
                        ) : (
                          <>
                            <strong>{func.value}</strong>
                          </>
                        )}
                      </li>
                    )
                  )}
                </ul>
              </div>
            )}

            {nutritionTemplate.maleLowerBoundIntake !== undefined && (
              <div className={styles.section}>
                <h2>Intake Recommendations:</h2>
                <p>
                  For males, the recommended intake ranges from{" "}
                  {`${nutritionTemplate.maleLowerBoundIntake} to ${nutritionTemplate.maleHigherBoundIntake} ${nutritionTemplate.measure}.`}
                </p>
                <p>
                  For females, the recommended intake ranges from{" "}
                  {`${nutritionTemplate.femaleLowerBoundIntake} to ${nutritionTemplate.femaleHigherBoundIntake} ${nutritionTemplate.measure}.`}
                </p>
              </div>
            )}
            <div className={styles.barCharSection}>
              <p>
                Hello there! This diagram provides information about foods that
                are rich in a specific nutrition. You can hover your mouse over
                each bar to see the exact amount of nutrition in each food.
                Please note that this data is sourced from my repository, which
                may result in some unfiled foods that are richer than the
                diagram indicates. Keep in mind that all food data is provided
                by ChatGPT AI.
              </p>
              <BarChart
                height={550}
                info={getDataForBarChart(sortedFoods, nutritionTemplate.name)}
              />
            </div>
          </div>
        </div>
      )}
    </>
  );
};

export default NutrientTemplate;
const getDataForBarChart = (sortedFoods, type) => {
  let data;
  let dataNames;
  if (type === "A") {
    data = sortedFoods.sortedFoodsByVitaminA.map((food) => food.a);
    dataNames = sortedFoods.sortedFoodsByVitaminA.map((food) => food.name);
  } else if (type === "D") {
    data = sortedFoods.sortedFoodsByVitaminD.map((food) => food.d);
    dataNames = sortedFoods.sortedFoodsByVitaminD.map((food) => food.name);
  } else if (type === "E") {
    data = sortedFoods.sortedFoodsByVitaminE.map((food) => food.e);
    dataNames = sortedFoods.sortedFoodsByVitaminE.map((food) => food.name);
  } else if (type === "B5") {
    data = sortedFoods.sortedFoodsByVitaminB5.map((food) => food.b5);
    dataNames = sortedFoods.sortedFoodsByVitaminB5.map((food) => food.name);
  } else if (type === "B1") {
    data = sortedFoods.sortedFoodsByVitaminB1.map((food) => food.b1);
    dataNames = sortedFoods.sortedFoodsByVitaminB1.map((food) => food.name);
  } else if (type === "B2") {
    data = sortedFoods.sortedFoodsByVitaminB2.map((food) => food.b2);
    dataNames = sortedFoods.sortedFoodsByVitaminB2.map((food) => food.name);
  } else if (type === "B3") {
    data = sortedFoods.sortedFoodsByVitaminB3.map((food) => food.b3);
    dataNames = sortedFoods.sortedFoodsByVitaminB3.map((food) => food.name);
  } else if (type === "B6") {
    data = sortedFoods.sortedFoodsByVitaminB6.map((food) => food.b6);
    dataNames = sortedFoods.sortedFoodsByVitaminB6.map((food) => food.name);
  } else if (type === "B7") {
    data = sortedFoods.sortedFoodsByVitaminB7.map((food) => food.b7);
    dataNames = sortedFoods.sortedFoodsByVitaminB7.map((food) => food.name);
  } else if (type === "B9") {
    data = sortedFoods.sortedFoodsByVitaminB9.map((food) => food.b9);
    dataNames = sortedFoods.sortedFoodsByVitaminB9.map((food) => food.name);
  } else if (type === "B12") {
    data = sortedFoods.sortedFoodsByVitaminB12.map((food) => food.b12);
    dataNames = sortedFoods.sortedFoodsByVitaminB12.map((food) => food.name);
  } else if (type === "K") {
    data = sortedFoods.sortedFoodsByVitaminK.map((food) => food.k);
    dataNames = sortedFoods.sortedFoodsByVitaminK.map((food) => food.name);
  } else if (type === "C") {
    data = sortedFoods.sortedFoodsByVitaminC.map((food) => food.c);
    dataNames = sortedFoods.sortedFoodsByVitaminC.map((food) => food.name);
  } else if (type === "Magnesium") {
    data = sortedFoods.sortedFoodsByMagnesium.map((food) => food.magnesium);
    dataNames = sortedFoods.sortedFoodsByMagnesium.map((food) => food.name);
  } else if (type === "Phosphorus") {
    data = sortedFoods.sortedFoodsByPhosphorus.map((food) => food.phosphorus);
    dataNames = sortedFoods.sortedFoodsByPhosphorus.map((food) => food.name);
  } else if (type === "Sodium") {
    data = sortedFoods.sortedFoodsBySodium.map((food) => food.sodium);
    dataNames = sortedFoods.sortedFoodsBySodium.map((food) => food.name);
  } else if (type === "Potassium") {
    data = sortedFoods.sortedFoodsByPotassium.map((food) => food.potassium);
    dataNames = sortedFoods.sortedFoodsByPotassium.map((food) => food.name);
  } else if (type === "Calcium") {
    data = sortedFoods.sortedFoodsByCalcium.map((food) => food.calcium);
    dataNames = sortedFoods.sortedFoodsByCalcium.map((food) => food.name);
  } else if (type === "Chloride") {
    data = sortedFoods.sortedFoodsByChloride.map((food) => food.chloride);
    dataNames = sortedFoods.sortedFoodsByChloride.map((food) => food.name);
  } else if (type === "Chromium") {
    data = sortedFoods.sortedFoodsByChromium.map((food) => food.chromium);
    dataNames = sortedFoods.sortedFoodsByChromium.map((food) => food.name);
  } else if (type === "Zinc") {
    data = sortedFoods.sortedFoodsByZinc.map((food) => food.zinc);
    dataNames = sortedFoods.sortedFoodsByZinc.map((food) => food.name);
  } else if (type === "Copper") {
    data = sortedFoods.sortedFoodsByCopper.map((food) => food.copper);
    dataNames = sortedFoods.sortedFoodsByCopper.map((food) => food.name);
  } else if (type === "Iodine") {
    data = sortedFoods.sortedFoodsByIodine.map((food) => food.iodine);
    dataNames = sortedFoods.sortedFoodsByIodine.map((food) => food.name);
  } else if (type === "Manganese") {
    data = sortedFoods.sortedFoodsByManganese.map((food) => food.manganese);
    dataNames = sortedFoods.sortedFoodsByManganese.map((food) => food.name);
  } else if (type === "Iron") {
    data = sortedFoods.sortedFoodsByIron.map((food) => food.iron);
    dataNames = sortedFoods.sortedFoodsByIron.map((food) => food.name);
  } else if (type === "Fluoride") {
    data = sortedFoods.sortedFoodsByFluoride.map((food) => food.fluoride);
    dataNames = sortedFoods.sortedFoodsByFluoride.map((food) => food.name);
  } else if (type === "Molybdenum") {
    data = sortedFoods.sortedFoodsByMolybdenum.map((food) => food.molybdenum);
    dataNames = sortedFoods.sortedFoodsByMolybdenum.map((food) => food.name);
  } else if (type === "Selenium") {
    data = sortedFoods.sortedFoodsBySelenium.map((food) => food.selenium);
    dataNames = sortedFoods.sortedFoodsBySelenium.map((food) => food.name);
  } else if (type === "Saturated Fat") {
    data = sortedFoods.sortedFoodsBySaturatedFat.map(
      (food) => food.saturatedFat
    );
    dataNames = sortedFoods.sortedFoodsBySaturatedFat.map((food) => food.name);
  } else if (type === "Protein") {
    data = sortedFoods.sortedFoodsByProtein.map((food) => food.protein);
    dataNames = sortedFoods.sortedFoodsByProtein.map((food) => food.name);
  } else if (type === "Carbohydrates") {
    data = sortedFoods.sortedFoodsByCarbohydrates.map(
      (food) => food.carbohydrates
    );
    dataNames = sortedFoods.sortedFoodsByCarbohydrates.map((food) => food.name);
  } else if (type === "Fat") {
    data = sortedFoods.sortedFoodsByFat.map((food) => food.fat);
    dataNames = sortedFoods.sortedFoodsByFat.map((food) => food.name);
  } else if (type === "Sugar") {
    data = sortedFoods.sortedFoodsBySugar.map((food) => food.sugar);
    dataNames = sortedFoods.sortedFoodsBySugar.map((food) => food.name);
  } else if (type === "Fiber") {
    data = sortedFoods.sortedFoodsByFiber.map((food) => food.fiber);
    dataNames = sortedFoods.sortedFoodsByFiber.map((food) => food.name);
  } else if (type === "Trans Fat") {
    data = sortedFoods.sortedFoodsByTransFat.map((food) => food.transFat);
    dataNames = sortedFoods.sortedFoodsByTransFat.map((food) => food.name);
  } else if (type === "Monounsaturated Fat") {
    data = sortedFoods.sortedFoodsByMonounsaturatedFat.map(
      (food) => food.monounsaturatedFat
    );
    dataNames = sortedFoods.sortedFoodsByMonounsaturatedFat.map(
      (food) => food.name
    );
  } else if (type === "Polyunsaturated Fat") {
    data = sortedFoods.sortedFoodsByPolyunsaturatedFat.map(
      (food) => food.polyunsaturatedFat
    );
    dataNames = sortedFoods.sortedFoodsByPolyunsaturatedFat.map(
      (food) => food.name
    );
  }

  return { data, dataNames };
};
