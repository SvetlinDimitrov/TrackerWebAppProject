import { useContext, useEffect, useState } from "react";
import { useNavigate, useParams, useLocation } from "react-router-dom";

import * as PathCreator from "../../../../util/PathCreator";
import { BarChart2 } from "../../../../util/Tools";
import { FoodContext } from "../../../../context/FoodContext";
import styles from "../NutritionTemplate.module.css";

const NutritionTemplateFeatureBarCharInfo = () => {
  const navigate = useNavigate();
  const { nutrient } = useContext(FoodContext);
  const [data, setData] = useState(undefined);
  const query = new URLSearchParams(useLocation().search);

  const { featureType } = useParams();
  const sort = query.get("sort");
  const limit = query.get("limit");

  useEffect(() => {
    const sortData = async () => {
      if(sort === "DO"){
        setData(nutrient.data.sort((a, b) => a.data - b.data));
        return;
      }else{
        setData(nutrient.data.sort((a, b) => b.data - a.data));
        return;
      }
    };
    sortData();
  }, [nutrient, sort]);

  if (data === undefined) {
    return <div id="preloader"></div>;
  }
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
                  "feature",
                  featureType,
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
                  "feature",
                  featureType,
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
        <BarChart2 height={550} info={data} dataLength={limit} />
      </div>
    </div>
  );
};
export default NutritionTemplateFeatureBarCharInfo;
