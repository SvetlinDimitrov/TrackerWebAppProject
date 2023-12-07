import { useContext } from "react";
import { useNavigate, useParams, useLocation } from "react-router-dom";

import * as PathCreator from "../../../../util/PathCreator";
import * as NutrientCalculator from "../../../../util/NutrientCalculator";
import { BarChart2 } from "../../../../util/Tools";
import { FoodContext } from "../../../../context/FoodContext";
import styles from "../NutritionTemplate.module.css";

const NutritionTemplateBarCharInfo = () => {
  const navigate = useNavigate();
  const { nutrient, allFoods } = useContext(FoodContext);
  const query = new URLSearchParams(useLocation().search);

  const { nutrition, nutritionType } = useParams();
  const sort = query.get("sort");
  const limit = query.get("limit");

  return (
    <div className={styles.body_container_barChar}>
      <p className={styles.body_container_barChar_p}>
        Hello there! This diagram provides information about foods that are rich
        in a specific nutrition. You can hover your mouse over each bar to see
        the exact amount of nutrition in each food. Please note that this data
        is sourced from my repository, which may result in some unfiled foods
        that are richer than the diagram indicates. Keep in mind that all food
        data is provided by ChatGPT AI.
      </p>
      <div className={styles.diagram_container}>
        <h3 className={styles.reportsH3}>Modify Data</h3>
        <div>
          <input
            type="number"
            className={styles.reportOptions}
            defaultValue={50}
            onChange={(e) =>
              navigate(
                PathCreator.nutrientInfo(
                  nutrition,
                  nutritionType,
                  "barCharStatistic" +
                    "?limit=" +
                    e.target.value +
                    "&sort=" +
                    sort
                )
              )
            }
          />
        </div>
        <div>
          <select
            className={styles.reportOptions}
            defaultValue={sort}
            onChange={(e) =>
              navigate(
                PathCreator.nutrientInfo(
                  nutrition,
                  nutritionType,
                  "barCharStatistic" +
                    "?sort=" +
                    e.target.value +
                    "&limit=" +
                    limit
                )
              )
            }
          >
            <option value="AO">Ascending Order</option>
            <option value="DO">Descending Order</option>
          </select>
        </div>
      </div>
      <div className={styles.container_BarChar2}>
        <BarChart2
          height={550}
          info={NutrientCalculator.sortedFoodsByNutrientType(
            allFoods,
            nutrient.name,
            sort,
            limit
          )}
          dataLength={limit}
        />
      </div>
    </div>
  );
};
export default NutritionTemplateBarCharInfo;
