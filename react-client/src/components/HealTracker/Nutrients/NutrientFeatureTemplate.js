import { useContext, useEffect, useState } from "react";
import { useParams, useNavigate, Outlet } from "react-router-dom";
import { FoodContext } from "../../../context/FoodContext";
import { AuthContext } from "../../../context/UserAuth";
import * as nutrientCalculations from "../../../util/NutrientCalculator";
import * as PathCreator from "../../../util/PathCreator";
import styles from "./NutrientFeatureTemplate.module.css";

const NutrientFeatureTemplate = () => {
  const { user } = useContext(AuthContext);
  const navigate = useNavigate();
  const { allFoods, setNutrient } = useContext(FoodContext);
  const { featureType } = useParams();
  const [data, setData] = useState();
  const [title , setTitle] = useState();
  const [activeTab, setActiveTab] = useState("");

  useEffect(() => {
    const fetchData = async () => {
      const dataToSet = nutrientCalculations.featureNutritionDate(allFoods, user, featureType);
      setData(dataToSet);
      setNutrient(dataToSet);
      setTitle(featureType.replace(/([A-Z])/g, ' $1').trim());
    };
    fetchData();
  }, [ allFoods, user, featureType, setNutrient]);

  if (data === undefined) {
    return <div id="preloader"></div>;
  }
  return (
    <div className={styles.body_container_main}>
      <h2 className={styles.body_container_section_h2}>{title}</h2>
      <div className={styles.tabs}>
        <button
          className={
            activeTab === "description" ? styles.activeTab : styles.tab
          }
          onClick={() => {
            setActiveTab("description");
            navigate(
              PathCreator.nutrientInfo("feature", featureType, "description")
            );
          }}
        >
          Description
        </button>
        <button
          className={
            activeTab === "barCharStatistic" ? styles.activeTab : styles.tab
          }
          onClick={() => {
            setActiveTab("barCharStatistic");
            navigate(
              PathCreator.nutrientInfo(
                "feature",
                featureType,
                "barCharStatistic?sort=DO&limit=50"
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
};

export default NutrientFeatureTemplate;
