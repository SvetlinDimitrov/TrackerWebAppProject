import React, { useState } from "react";

import { BarChart2 } from "../../util/Tools";
import styles from "./AchievementDetailsDaily.module.css";

const AchievementDetailsDaily = ({ achievement }) => {
  const [data, setData] = useState(getDailyData(achievement, "date_asc"));
  return (
    <div className={styles.diagram}>
      <div className={styles.dailyTitleContainer}>
          <h1 className={styles.title}>Daily Reports</h1>
          <div className={styles.diagram_container}>
          <h3 className={styles.reportsH3}>Sort By</h3>
          <div className={styles.reportOptions}>
            <select
              defaultValue={"date_asc"}
              onChange={(e) =>
                setData(getDailyData(achievement, e.target.value))
              }
            >
              <option value="date_asc">Date asc</option>
              <option value="date_desc">Date desc</option>
              <option value="data_asc">Data asc</option>
              <option value="data_desc">Data desc</option>
            </select>
          </div>
        </div>
        <div className={styles.BarChart2_container}>
          <BarChart2 height={400} info={data} dataLength={data.length} />
        </div>
      </div>
    </div>
  );
};

export default AchievementDetailsDaily;
const getDailyData = (achievement, sort) => {
  const data = achievement.dailyProgress.map((item) => ({
    data: item.progress,
    dataNames: item.date,
    typeData: item.progress + " " + achievement.measurement,
  }));

  if (sort === "date_asc") {
    data.sort((a, b) => a.dataNames.localeCompare(b.dataNames));
  } else if (sort === "date_desc") {
    data.sort((a, b) => b.dataNames.localeCompare(a.dataNames));
  } else if (sort === "data_asc") {
    data.sort((a, b) => a.data - b.data);
  } else if (sort === "data_desc") {
    data.sort((a, b) => b.data - a.data);
  }

  return data;
};
