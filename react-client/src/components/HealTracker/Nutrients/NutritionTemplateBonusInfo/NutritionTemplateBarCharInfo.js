import { useContext, useEffect, useState } from "react";
import { useNavigate, useParams, useLocation } from "react-router-dom";

import * as PathCreator from "../../../../util/PathCreator";
import * as NutrientCalculator from "../../../../util/NutrientCalculator";
import { BarChart2 } from "../../../../util/Tools";
import { FoodContext } from "../../../../context/FoodContext";
import styles from "../NutritionTemplate.module.css";

const NutritionTemplateBarCharInfo = () => {
  const navigate = useNavigate();
  const { nutrient, allFoods, convertComplexFoodIntoSimpleFood } =
    useContext(FoodContext);
  const query = new URLSearchParams(useLocation().search);
  const { nutrition, nutritionType } = useParams();
  const sort = query.get("sort");
  const limit = query.get("limit");
  const [data, setData] = useState();
  const [selectedOptionsOfData, setSelectedOptionsOfData] = useState([
    "FinalFood",
  ]);

  useEffect(() => {
    setData(
      NutrientCalculator.sortedFoodsByNutrientType(
        allFoods
          .filter((food) =>
            selectedOptionsOfData.find((option) => option === food.type)
          )
          .map((food) => food.data)
          .flat()
          .map((chunk) => convertComplexFoodIntoSimpleFood(chunk)),
        nutritionType,
        sort,
        limit
      )
    );
  }, [allFoods, nutrient, sort, limit, convertComplexFoodIntoSimpleFood , nutritionType , selectedOptionsOfData]);
  const handleChange = (event) => {
    if (event.target.checked) {
      setSelectedOptionsOfData((prev) => [...prev, event.target.value]);
    } else {
      setSelectedOptionsOfData((prev) =>
        prev.filter((value) => value !== event.target.value)
      );
    }
  };
  if (data === undefined) {
    return <div>Loading...</div>;
  }
  return (
    <div className={styles.body_container_barChar}>
      <p className={styles.body_container_barChar_p}>
        The information provided on this platform is sourced from the FoodData
        Central (FDC) database, which is maintained by the National Agricultural
        Library of the United States Department of Agriculture (USDA). The data
        can be accessed and downloaded directly from the official FDC website at
        <a
          href="https://fdc.nal.usda.gov/download-datasets.html"
          target="_blank"
          rel="noopener noreferrer"
          style={{ margin: "10px" }}
        >
          Link Here
        </a>.
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
          <label>
            <input
              type="checkbox"
              value="FinalFood"
              checked={selectedOptionsOfData.includes("FinalFood")}
              onChange={handleChange}
            />
            FoundationFoods
          </label>
          <label>
            <input
              type="checkbox"
              value="Survey"
              checked={selectedOptionsOfData.includes("Survey")}
              onChange={handleChange}
            />
            SurveyFoods
          </label>
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
        <BarChart2 height={550} info={data} dataLength={limit} />
      </div>
    </div>
  );
};
export default NutritionTemplateBarCharInfo;
