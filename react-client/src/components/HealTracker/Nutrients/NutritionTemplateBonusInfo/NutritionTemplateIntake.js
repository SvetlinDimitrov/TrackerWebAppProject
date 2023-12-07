import { useContext } from "react";

import { FoodContext } from "../../../../context/FoodContext";
import styles from "../NutritionTemplate.module.css";

const NutritionTemplateIntake = () => {
  const { nutrient } = useContext(FoodContext);

  return (
    <div className={styles.body_container_section}>
      <ul className={styles.body_container_section_sources}>
        <li>
          <strong>Males:</strong> ranges from{" "}
          {`${nutrient.maleLowerBoundIntake} to ${nutrient.maleHigherBoundIntake} ${nutrient.measure}.`}
        </li>
        <li>
          <strong>Females:</strong> ranges from{" "}
          {`${nutrient.femaleLowerBoundIntake} to ${nutrient.femaleHigherBoundIntake} ${nutrient.measure}.`}
        </li>
      </ul>
    </div>
  );
};
export default NutritionTemplateIntake;
