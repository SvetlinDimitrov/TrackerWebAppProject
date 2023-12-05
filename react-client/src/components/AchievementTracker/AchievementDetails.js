import React, { useEffect, useState, useContext } from "react";
import styles from "./AchievementDetails.module.css";
import { Outlet, useNavigate, useParams } from "react-router-dom";

import { NotificationContext } from "../../context/Notification";
import * as PathCreator from "../../util/PathCreator";
import { AuthContext } from "../../context/UserAuth";
import api from "../../util/api";
import { BarChart2 } from "../../util/Tools";

const AchievementDetails = () => {
  const navigate = useNavigate();
  const { achId } = useParams();
  const { setFailedMessage } = useContext(NotificationContext);
  const { user } = useContext(AuthContext);
  const userToken = user.tokenInfo.token;
  const [achievement, setAchievement] = useState(undefined);

  useEffect(() => {
    setAchievement(undefined);
    const fetchData = async () => {
      try {
        const response = await api.get(`/achievement?id=${achId}`, {
          headers: { Authorization: `Bearer ${userToken}` },
        });
        setAchievement(response.data);
      } catch (error) {
        setFailedMessage({
          message:
            "Something went wrong with achievement fetching . Please try again later!",
          flag: true,
        });
        navigate("/");
      }
    };
    if (achId) {
      fetchData();
    }
  }, [achId, navigate, setFailedMessage, userToken]);

  if (achievement === undefined) {
    return <div className="preloader"></div>;
  }

  return (
    <>
      <div className={styles.body}>
        <div className={styles.container}>
          <div className={styles.titleRow}>
            <h1 className={styles.title}>{achievement.name}</h1>
            <p className={styles.date}>Started on {achievement.startDate}</p>
          </div>
          <div className={styles.row}>
            <h2 className={styles.subtitle}>{achievement.description}</h2>
            <button
              className={styles.updateButton}
              onClick={() =>
                navigate(
                  PathCreator.achievementPathId(achId) +
                    "/addProgress/" +
                    achievement.name
                )
              }
            >
              Update Progress
            </button>
          </div>
          <p className={styles.goalText}>
            Goal {achievement.goal} {achievement.measurement}
          </p>
          <div className={styles.reports}>
            <h3 className={styles.reportsH3}>Reports</h3>
            <div className={styles.reportOptions}>
              <button>Daily</button>
              <button>Weekly</button>
              <button>Monthly</button>
              <button>Yearly</button>
            </div>
          </div>
        </div>
        <div className={styles.diagram}>
          <div className={styles.diagramContainer}>
            <h1 className={styles.diagramContainerTitle}>Daily reports</h1>
            <BarChart2
              height={400}
              info={getDailyData(achievement)}
              dataLength={achievement.dailyProgress.length}
            />
          </div>
        </div>
      </div>
      <Outlet />
    </>
  );
};

export default AchievementDetails;
const getDailyData = (achievement) => {
  const data = achievement.dailyProgress.map((item) => item.progress);
  const dataNames = achievement.dailyProgress.map((item) => item.date);
  const typeData = achievement.dailyProgress.map((item) => item.progress);
  return { data, dataNames, typeData };
};
