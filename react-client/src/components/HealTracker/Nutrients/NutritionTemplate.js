import { useContext, useEffect, useState } from "react";
import { useNavigate, useParams, Outlet } from "react-router-dom";

import { FoodContext } from "../../../context/FoodContext";
import api from "../../../util/api";
import * as PathCreator from "../../../util/PathCreator";
import styles from "./NutritionTemplate.module.css";

const NutritionTemplate = () => {
  let { nutrition, nutritionType } = useParams();
  const navigate = useNavigate();
  const [nutritionTemplate, setNutritionTemplate] = useState({});
  const { setNutrient } = useContext(FoodContext);
  const [activeTab, setActiveTab] = useState("");

  useEffect(() => {
    const fetchData = async () => {
      let response;
      try {
        if (nutrition === "macronutrientTypes") {
          response = await api.get("/macronutrient/types/" + nutritionType);
        } else {
          response = await api.get("/" + nutrition + "/" + nutritionType);
        }
        const capitalizedNutrition =
          nutrition.charAt(0).toUpperCase() + nutrition.slice(1);
        response.data["type"] = capitalizedNutrition;

        setNutritionTemplate(response.data);
        setNutrient(response.data);
      } catch (error) {
        navigate("/error");
      }
    };
    fetchData();
  }, [navigate, nutrition, nutritionType]);

  if (!nutritionTemplate.name) {
    return <div id="preloader"></div>;
  }

  return (
    <div className={styles.body_container_main}>
      <h2 className={styles.body_container_section_h2}>
        {`${nutritionTemplate.type} ${nutritionTemplate.name}`}
      </h2>
      <p>{nutritionTemplate.description}</p>

      <div className={styles.tabs}>
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
                "barCharStatistic" + "?sort=DO" + "&limit=50"
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

  // Existing code...

  // Existing code...
};

export default NutritionTemplate;
