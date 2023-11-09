import { useContext, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faPlus, faMinus } from "@fortawesome/free-solid-svg-icons";

import api from "../../../util/api";

import { NutritionRecordContext } from "../../../context/NutritionRecordContext";
import { NotificationContext } from "../../../context/Notification";
import { AuthContext } from "../../../context/UserAuth";
import styles from "./NutritionRecord.module.css";
import { PipeChar, Gauge } from "./Tools";

const NutritionRecordTemplate = () => {
  const navigate = useNavigate();
  const { setRecord, record } = useContext(NutritionRecordContext);
  const { userToken } = useContext(AuthContext);
  const { setFailedMessage } = useContext(NotificationContext);
  const [show, setShow] = useState("");
  const [categoryShow, setCategoryShow] = useState("");

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await api.get("/record/all", {
          headers: { Authorization: `Bearer ${userToken}` },
        });
        setRecord(response.data, response.data.length - 1);
      } catch (error) {
        setFailedMessage({
          message:
            "You need to be fully registered. Please go to settings and finish your register!",
          flag: true,
        });
        navigate("/");
      }
    };

    fetchData();
  }, [navigate, userToken, setFailedMessage]);

  const handleOnClick = (name) => {
    setShow((prevShow) => (prevShow === name ? "" : name));
  };

  const handleOnClickCategory = (name) => {
    setCategoryShow((prevShow) => (prevShow === name ? "" : name));
  };

  if (!record.vitaminDataPrecented) {
    return <div id="preloader"></div>;
  }

  return (
    <div className={styles.body}>
      <h1 className={styles.h1}>Nutrition Record</h1>

      <div className={styles.containerGauge}>
        <Gauge width={200} height={200} type={"Vitamin"} />

        <Gauge width={200} height={200} type={"Electrolyte"} />

        <Gauge width={200} height={200} type={"Macronutrient"} />
      </div>

      <h2 className={styles.h1}>Detailed Information</h2>

      <div className={styles.containerGauge}>
        <div
          className={styles.category}
          onClick={() => handleOnClickCategory("Vitamin")}
        >
          Vitamin
        </div>
        <div
          className={styles.category}
          onClick={() => handleOnClickCategory("Electrolyte")}
        >
          Electrolyte
        </div>
        <div
          className={styles.category}
          onClick={() => handleOnClickCategory("Macronutrient")}
        >
          Macronutrient
        </div>
      </div>
      {categoryShow === "Vitamin" && (
        <div className={styles.containerInfo}>
          {record.vitaminDataPrecented.map((d) => {
            return (
              <div>
                <div
                  className={styles.infoContainer}
                  onClick={() => handleOnClick(d.name)}
                >
                  <span>{d.name}</span>
                  <span>Completed: {d.precented.toFixed(2)}%</span>
                  <FontAwesomeIcon icon={show === d.name ? faMinus : faPlus} />
                </div>
                {show === d.name && (
                  <PipeChar width={300} height={30} data={d} />
                )}
              </div>
            );
          })}
        </div>
      )}
      {categoryShow === "Electrolyte" && (
        <div className={styles.containerInfo}>
          {record.electrolyteDataPrecented.map((d) => {
            return (
              <div>
                <div
                  className={styles.infoContainer}
                  onClick={() => handleOnClick(d.name)}
                >
                  <span>{d.name}</span>
                  <span>Completed: {d.precented.toFixed(2)}%</span>
                  <FontAwesomeIcon icon={show === d.name ? faMinus : faPlus} />
                </div>
                {show === d.name && (
                  <PipeChar width={300} height={30} data={d} />
                )}
              </div>
            );
          })}
        </div>
      )}
      {categoryShow === "Macronutrient" && (
        <div className={styles.containerInfo}>
          {record.macronutrientDataPrecented.map((d) => {
            return (
              <div>
                <div
                  className={styles.infoContainer}
                  onClick={() => handleOnClick(d.name)}
                >
                  <span>{d.name}</span>
                  <span>Completed: {d.precented.toFixed(2)}%</span>
                  <FontAwesomeIcon icon={show === d.name ? faMinus : faPlus} />
                </div>
                {show === d.name && (
                  <PipeChar width={300} height={30} data={d} />
                )}
              </div>
            );
          })}
        </div>
      )}
    </div>
  );
};

export default NutritionRecordTemplate;
