import React, { useContext, useEffect, useState } from "react";
import { Outlet, useNavigate, useParams } from "react-router-dom";
import styles from "./AchievementDetails.module.css";

import AchievementDetailsDaily from "./AchievementDetailsDaily";
import { NotificationContext } from "../../context/Notification";
import { AuthContext } from "../../context/UserAuth";
import * as PathCreator from "../../util/PathCreator";

import api from "../../util/api";
import AchievementDetailsWeekly from "./AchievementDetailsWeekly";

const AchievementDetails = () => {
  const navigate = useNavigate();
  const { achId } = useParams();
  const { setFailedMessage } = useContext(NotificationContext);
  const { user } = useContext(AuthContext);
  const userToken = user.tokenInfo.token;
  const [achievement, setAchievement] = useState(undefined);
  const [show, setShow] = useState("");

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
                navigate(PathCreator.achievementPathId(achId) + "/addProgress")
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
              <button
                onClick={() =>
                  show === "Daily" ? setShow("") : setShow("Daily")
                }
              >
                Daily
              </button>
              <button
                onClick={() =>
                  show === "Weekly" ? setShow("") : setShow("Weekly")
                }
              >
                Weekly
              </button>
              <button
                onClick={() =>
                  show === "Monthly" ? setShow("") : setShow("Monthly")
                }
              >
                Monthly
              </button>
              <button
                onClick={() =>
                  show === "Yearly" ? setShow("") : setShow("Yearly")
                }
              >
                Yearly
              </button>
            </div>
          </div>
        </div>
        {show !== "" && show === "Daily" && (
          <AchievementDetailsDaily achievement={achievement} />
        )}

        {show !== "" && show === "Weekly" && (
          <AchievementDetailsWeekly achievement={achievement} />
        )}
      </div>

      <Outlet />
    </>
  );
};

export default AchievementDetails;
