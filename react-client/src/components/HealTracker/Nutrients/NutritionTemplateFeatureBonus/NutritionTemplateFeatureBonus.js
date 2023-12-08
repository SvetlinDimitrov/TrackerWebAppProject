import { useContext } from "react";

import { FoodContext } from "../../../../context/FoodContext";
import styles from "../NutrientFeatureTemplate.module.css";

const NutrientTemplateDescriptionFeature = () => {
  const { nutrient } = useContext(FoodContext);
  return (
    <div className={styles.body_container_section}>
      <p className={styles.body_container_description}>
        {" "}
        {nutrient.text}
      </p>
    </div>
  );
};
export default NutrientTemplateDescriptionFeature;
