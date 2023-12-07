import { useContext } from "react";

import { FoodContext } from "../../../../context/FoodContext";
import styles from "../NutritionTemplate.module.css";

const NutritionTemplateHealthConsideration = () => {
  const { nutrient } = useContext(FoodContext);

  return (
    <div className={styles.body_container_section}>
      <ul className={styles.body_container_section_sources}>
        {nutrient.healthConsiderations.map((func, index) => (
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
  );
};
export default NutritionTemplateHealthConsideration;
