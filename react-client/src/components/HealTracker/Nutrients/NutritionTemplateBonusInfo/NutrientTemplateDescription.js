import { useContext } from "react";

import { FoodContext } from "../../../../context/FoodContext";
import styles from "../NutritionTemplate.module.css";

const NutrientTemplateDescription = () => {
  const { nutrient } = useContext(FoodContext);

  return (
    <div className={styles.body_container_section}>
      <p className={styles.body_container_description}>
        {" "}
        {nutrient.description}
      </p>
    </div>
  );
};
export default NutrientTemplateDescription;
