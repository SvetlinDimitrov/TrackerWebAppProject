import { faMinus, faPlus } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { useState } from "react";

import { calculatedPrecentedValues } from "../../../util/RecordUtils";
import styles from "./DetailsOfRecord.module.css";
import { Gauge, PipeChar, Gauge2 } from "./Tools";

const DetailsOfRecord = ({ record }) => {
  const [categoryShow, setCategoryShow] = useState(undefined);
  const [show, setShow] = useState("");
  const types = ["Vitamin", "Mineral", "Macronutrient", "Calories"];
  const categories = ["Vitamin", "Mineral", "Macronutrient", "Calories"];

  const handleOnClick = (name) => {
    setShow((prevShow) => (prevShow === name ? "" : name));
  };

  if (record === undefined) {
    return <div id="preloader"></div>;
  }

  return (
    <>
      <div className={styles.body}>
        <div>
          <h1 className={styles.h1}>Record: {record.name}</h1>
          <div className={styles.containerGauge}>
            {types.map((type) => (
              <Gauge
                key={type}
                width={200}
                height={200}
                type={type}
                data={record}
              />
            ))}
            ;
          </div>
          <h2 className={styles.h1}>Detailed Information</h2>
          <div className={styles.containerGauge}>
            {categories.map((category) => (
              <div
                key={category}
                className={styles.category}
                onClick={() => setCategoryShow(category)}
              >
                {category}
              </div>
            ))}
          </div>
          {categoryShow && (
            <div className={styles.containerInfo}>
              {calculatedPrecentedValues(record, categoryShow).map((d) => {
                return (
                  <div key={d.id}>
                    <div
                      className={styles.infoContainer}
                      onClick={() => handleOnClick(d.name)}
                    >
                      <span>{d.name.replace(/([A-Z])/g, " $1").trim()}</span>
                      <span>
                        Completed:{" "}
                        {isNaN(d.precented) ? 0 : d.precented.toFixed(2)}%
                      </span>
                      <FontAwesomeIcon
                        icon={show === d.name ? faMinus : faPlus}
                      />
                    </div>
                    {show === d.name && (
                      <PipeChar width={300} height={30} data={d} />
                    )}
                    {show === d.name && d.type === "Calories" && (
                      <Gauge2
                        width={260}
                        height={110}
                        diameter={100}
                        legendRectSize={12}
                        legendSpacing={10}
                        protein={d.protein}
                        fat={d.fat}
                        carbohydrates={d.carbohydrates}
                      />
                    )}
                  </div>
                );
              })}
            </div>
          )}
        </div>
      </div>
    </>
  );
};

export default DetailsOfRecord;
