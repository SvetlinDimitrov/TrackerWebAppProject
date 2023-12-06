import React, { useEffect, useState } from "react";

import { AiOutlineInfoCircle } from "react-icons/ai";
import Tooltip from "./Tooltip";
import { GroupedBarChart } from "../../util/Tools";
import styles from "./AchievementDetailsWeekly.module.css";

const AchievementDetailsWeekly = ({ achievement }) => {
  const [selectedSection, setSelectedSection] = useState(0);
  const [sectionData, setSectionData] = useState([]);
  const [sortData, setSortData] = useState("date_asc");

  useEffect(() => {
    setData(achievement);
  }, [achievement, sortData]);

  function setData(data) {
    let processedData = data.weeklyProgress.map((item) => {
      return {
        data: [
          item.bestProgress,
          item.worstProgress,
          item.totalProgress,
          item.totalProgress / 7,
        ],
        dataNames: item.week,
        typeData: [
          item.bestProgress.toFixed(2) + " " + data.measurement,
          item.worstProgress.toFixed(2) + " " + data.measurement,
          item.totalProgress.toFixed(2) + " " + data.measurement,
          (item.totalProgress / 7).toFixed(2) + " " + data.measurement,
        ],
      };
    });

    if (sortData === "date_AO") {
      processedData.sort((a, b) => {
        return a.dataNames.localeCompare(b.dataNames);
      });
    } else if (sortData === "date_DO") {
      processedData.sort((a, b) => {
        return b.dataNames.localeCompare(a.dataNames);
      });
    } else if (sortData === "totalProgress_AO") {
      processedData.sort((a, b) => {
        return a.data[2] - b.data[2];
      });
    } else if (sortData === "totalProgress_DO") {
      processedData.sort((a, b) => {
        return b.data[2] - a.data[2];
      });
    } else if (sortData === "bestProgress_AO") {
      processedData.sort((a, b) => {
        return a.data[0] - b.data[0];
      });
    } else if (sortData === "bestProgress_DO") {
      processedData.sort((a, b) => {
        return b.data[0] - a.data[0];
      });
    } else if (sortData === "worstProgress_AO") {
      processedData.sort((a, b) => {
        return a.data[1] - b.data[1];
      });
    } else if (sortData === "worstProgress_DO") {
      processedData.sort((a, b) => {
        return b.data[1] - a.data[1];
      });
    } else if (sortData === "averageProgress_AO") {
      processedData.sort((a, b) => {
        return a.data[3] - b.data[3];
      });
    } else if (sortData === "averageProgress_DO") {
      processedData.sort((a, b) => {
        return b.data[3] - a.data[3];
      });
    }

    const sections = groupDataIntoSections(processedData, 9);
    setSectionData(sections);
    setSelectedSection(0);
  }

  if (sectionData.length === 0) {
    return <div className="preloader"></div>;
  }

  return (
    <div className={styles.diagram}>
      <div className={styles.dailyTitleContainer}>
        <div className={styles.titleContainer}>
          <h1 className={styles.title}>Weekly Reports</h1>
          <span className={styles.moreInfo}>
            More Info{" "}
            <Tooltip>
              <AiOutlineInfoCircle />
            </Tooltip>
          </span>
        </div>
        <div className={styles.draggable_container}>
          {sectionData.map((section, index) => {
            const year = section[0].dataNames.split("-")[0];
            return (
              <div
                key={index}
                onClick={() => setSelectedSection(index)}
                className={styles.section}
              >
                {`${year}-${section[0].dataNames.split("-")[1]}/${
                  section[section.length - 1].dataNames.split("-")[1]
                }`}
              </div>
            );
          })}
        </div>
        <div className={styles.diagram_container}>
          <h3 className={styles.reportsH3}>Sort By</h3>
          <div>
            <select
              className={styles.reportOptions}
              defaultValue={"date_asc"}
              onChange={(e) => setSortData(e.target.value)}
            >
              <option value="date_AO">Date AO</option>
              <option value="date_DO">Date DO</option>
              <option value="totalProgress_AO">Total Progress AO</option>
              <option value="totalProgress_DO">
                Total Progress DO
              </option>
              <option value="bestProgress_AO">Best Progress AO</option>
              <option value="bestProgress_DO">Best Progress DO</option>
              <option value="worstProgress_AO">Worst Progress AO</option>
              <option value="worstProgress_DO">
                Worst Progress DO
              </option>
              <option value="averageProgress_AO">
                {" "}
                Average Progress AO
              </option>
              <option value="averageProgress_DO">
                {" "}
                Average Progress DO
              </option>
            </select>
          </div>
        </div>
        <div className={styles.BarChart2_container}>
          <GroupedBarChart
            height={400}
            info={sectionData[selectedSection]}
            dataLength={sectionData[selectedSection].length}
          />
        </div>
      </div>
    </div>
  );
};

export default AchievementDetailsWeekly;

function groupDataIntoSections(data, sectionSize) {
  let sections = [];
  for (let i = 0; i < data.length; i += sectionSize) {
    sections.push(data.slice(i, i + sectionSize));
  }
  return sections;
}
