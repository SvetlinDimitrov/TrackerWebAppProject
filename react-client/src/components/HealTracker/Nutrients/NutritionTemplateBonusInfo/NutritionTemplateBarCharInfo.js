import { useEffect, useState, useContext } from "react";
import { useNavigate, useParams, useLocation } from "react-router-dom";

import api from "../../../../util/api";
import { NutrientContext } from "../../../../context/NutrientContextProvider";
import { AuthContext } from "../../../../context/UserCredentials";
import * as PathCreator from "../../../../util/PathCreator";
import { BarChart2 } from "../../../../util/Tools";
import styles from "../NutritionTemplate.module.css";

const NutritionTemplateBarCharInfo = () => {
  const navigate = useNavigate();
  const { nutrient } = useContext(NutrientContext);
  const { user } = useContext(AuthContext);
  const userToken = user.tokenInfo.token;
  const query = new URLSearchParams(useLocation().search);
  const { nutrition, nutritionType } = useParams();
  const sort = query.get("sort");
  const limit = query.get("limit");
  const min = query.get("min");
  const max = query.get("max");
  const [data, setData] = useState();
  const [selectedOptionsOfData, setSelectedOptionsOfData] =
    useState("finalFoods");

  useEffect(() => {
    setData(undefined);
    const fetchData = async () => {
      const response = await api.get(
        `/food/embedded/${selectedOptionsOfData}/filter?nutrientName=${
          nutrient.name
        }&limit=${limit}&desc=${
          sort === "DO" ? true : false
        }&min=${min}&max=${max}`,
        {
          headers: { Authorization: `Bearer ${userToken}` },
        }
      );
      setData(
        response.data.map((food) => {
          return {
            data: food.amount,
            dataNames: food.description,
            typeData: food.amount,
          };
        })
      );
    };
    fetchData();
  }, [
    userToken,
    nutrient.name,
    limit,
    sort,
    min,
    max,
    selectedOptionsOfData,
  ]);

  if (data === undefined) {
    return <div id="preloader"></div>;
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
        </a>
        .
      </p>
      <div className={styles.diagram_container}>
        <h3 className={styles.reportsH3}>Modify Data</h3>
        <div>
          <label>
            Limit:
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
                      sort +
                      "&min=" +
                      min +
                      "&max=" +
                      max
                  )
                )
              }
            />
          </label>
        </div>
        <div>
          <label>
            <input
              type="checkbox"
              value="finalFoods"
              checked={selectedOptionsOfData === "finalFoods"}
              onChange={() => setSelectedOptionsOfData("finalFoods")}
            />
            FoundationFoods
          </label>
          <label>
            <input
              type="checkbox"
              value="surveyFoods"
              checked={selectedOptionsOfData === "surveyFoods"}
              onChange={() => setSelectedOptionsOfData("surveyFoods")}
            />
            SurveyFoods
          </label>
          <label>
            <input
              type="checkbox"
              value="brandedFoods"
              checked={selectedOptionsOfData === "brandedFoods"}
              onChange={() => setSelectedOptionsOfData("brandedFoods")}
            />
            BrandedFoods
          </label>
        </div>
        <div
          style={{ display: "flex", flexDirection: "column", width: "200px" }}
        >
          <label style={{ marginBottom: "10px" }}>
            Min Value:
            <input
              type="number"
              value={min}
              onChange={(e) =>
                navigate(
                  PathCreator.nutrientInfo(
                    nutrition,
                    nutritionType,
                    "barCharStatistic" +
                      "?limit=" +
                      limit +
                      "&sort=" +
                      sort +
                      "&min=" +
                      e.target.value +
                      "&max=" +
                      max
                  )
                )
              }
              style={{ width: "100%", padding: "5px" }}
            />
          </label>
          <label>
            Max Value:
            <input
              type="number"
              value={max}
              onChange={(e) =>
                navigate(
                  PathCreator.nutrientInfo(
                    nutrition,
                    nutritionType,
                    "barCharStatistic" +
                      "?limit=" +
                      limit +
                      "&sort=" +
                      sort +
                      "&min=" +
                      min +
                      "&max=" +
                      e.target.value
                  )
                )
              }
              style={{ width: "100%", padding: "5px" }}
            />
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
                    "?limit=" +
                    limit +
                    "&sort=" +
                    e.target.value +
                    "&min=" +
                    min +
                    "&max=" +
                    max
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
