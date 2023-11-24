import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";

import styles from "./NutritionTemplate.module.css";
import api from "../../../util/api";

const NutrientTemplate = () => {
  let { nutrition, nutritionType } = useParams();
  const navigate = useNavigate();
  const [nutritionTemplate, setNutritionTemplate] = useState({});
 
  useEffect(() => {
    const fetchData = async () => {
      let response;
      try {
        if(nutrition === "macronutrientTypes"){
          response = await api.get("/" + "macronutrient/types" + "/" + nutritionType);
          nutrition = "macronutrient";
        }else{
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
          </div>
        </div>
      )}
    </>
  );
};

export default NutrientTemplate;
