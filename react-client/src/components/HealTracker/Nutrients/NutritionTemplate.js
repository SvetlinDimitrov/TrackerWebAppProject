import { useContext, useEffect, useState } from "react";
import { useNavigate, useParams, Outlet } from "react-router-dom";

import { FoodContext } from "../../../context/FoodContext";
import * as PathCreator from "../../../util/PathCreator";
import styles from "./NutritionTemplate.module.css";
import vitamins from "../../../data/vitamin.json";
import minerals from "../../../data/minerals.json";
import macronutrients from "../../../data/macronutrients";
import macroTypes from "../../../data/macronutrientTypes.json";

const NutritionTemplate = () => {
  let { nutrition, nutritionType } = useParams();
  const navigate = useNavigate();
  const [nutritionTemplate, setNutritionTemplate] = useState({});
  const { setNutrient } = useContext(FoodContext);
  const [activeTab, setActiveTab] = useState("");

  useEffect(() => {
    if (nutrition === "vitamin") {
      setNutritionTemplate(filterArray(vitamins, nutritionType));
      setNutrient(filterArray(vitamins, nutritionType));
    } else if (nutrition === "mineral") {
      setNutritionTemplate(filterArray(minerals, nutritionType));
      setNutrient(filterArray(minerals, nutritionType));
    } else if (nutrition === "macronutrient") {
      setNutritionTemplate(filterArray(macronutrients, nutritionType));
      setNutrient(filterArray(macronutrients, nutritionType));
    } else if (nutrition === "macronutrientTypes") {
      setNutritionTemplate(filterArray(macroTypes, nutritionType));
      setNutrient(filterArray(macroTypes, nutritionType));
    }
  }, [navigate, nutrition, nutritionType, setNutrient]);

  if (!nutritionTemplate) {
    return <div id="preloader"></div>;
  }

  return (
    <div className={styles.body_container_main}>
      <h2 className={styles.body_container_section_h2}>
        {`${nutritionTemplate.name}`}
      </h2>
      <div className={styles.tabs}>
        <button
          className={
            activeTab === "description" ? styles.activeTab : styles.tab
          }
          onClick={() => {
            setActiveTab("description");
            navigate(
              PathCreator.nutrientInfo(nutrition, nutritionType, "description")
            );
          }}
        >
          Description
        </button>
        <button
          className={activeTab === "functions" ? styles.activeTab : styles.tab}
          onClick={() => {
            setActiveTab("functions");
            navigate(
              PathCreator.nutrientInfo(nutrition, nutritionType, "functions")
            );
          }}
        >
          Functions
        </button>

        <button
          className={activeTab === "sources" ? styles.activeTab : styles.tab}
          onClick={() => {
            setActiveTab("sources");
            navigate(
              PathCreator.nutrientInfo(nutrition, nutritionType, "sources")
            );
          }}
        >
          Sources
        </button>
        {nutrition === "macronutrientTypes" && (
          <button
            className={
              activeTab === "healthConsiderations"
                ? styles.activeTab
                : styles.tab
            }
            onClick={() => {
              setActiveTab("healthConsiderations");
              navigate(
                PathCreator.nutrientInfo(
                  nutrition,
                  nutritionType,
                  "healthConsiderations"
                )
              );
            }}
          >
            Health Considerations
          </button>
        )}
        {nutrition === "macronutrient" && nutritionType !== "Protein" && (
          <button
            className={activeTab === "types" ? styles.activeTab : styles.tab}
            onClick={() => {
              setActiveTab("types");
              navigate(
                PathCreator.nutrientInfo(nutrition, nutritionType, "types")
              );
            }}
          >
            Types
          </button>
        )}
        {nutrition === "macronutrient" && (
          <button
            className={
              activeTab === "dietaryConsiderations"
                ? styles.activeTab
                : styles.tab
            }
            onClick={() => {
              setActiveTab("dietaryConsiderations");
              navigate(
                PathCreator.nutrientInfo(
                  nutrition,
                  nutritionType,
                  "dietaryConsiderations"
                )
              );
            }}
          >
            Dietary Considerations
          </button>
        )}
        {nutrition !== "macronutrient" && (
          <button
            className={activeTab === "intake" ? styles.activeTab : styles.tab}
            onClick={() => {
              setActiveTab("intake");
              navigate(
                PathCreator.nutrientInfo(nutrition, nutritionType, "intake")
              );
            }}
          >
            Daily Intake
          </button>
        )}
        <button
          className={
            activeTab === "barCharStatistic" ? styles.activeTab : styles.tab
          }
          onClick={() => {
            setActiveTab("barCharStatistic");
            navigate(
              PathCreator.nutrientInfo(
                nutrition,
                nutritionType,
                "barCharStatistic?sort=DO&limit=50"
              )
            );
          }}
        >
          Statistics
        </button>
      </div>

      <div className={styles.tabContent}>
        <Outlet />
      </div>
    </div>
  );
};

export default NutritionTemplate;

const filterArray = (data, name) => {
  const filtered = data.filter((item) => item.name === name);
  return filtered[0];
};
