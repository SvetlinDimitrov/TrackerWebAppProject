import styles from "./AchievementUpdate.module.css";
import React, { useState, useContext } from "react";
import { useNavigate, useParams } from "react-router-dom";

import { NotificationContext } from "../../context/Notification";
import * as PathCreator from "../../util/PathCreator";
import { AuthContext } from "../../context/UserAuth";
import api from "../../util/api";

const AchievementUpdate = () => {
  const navigate = useNavigate();
  const { achId , achName} = useParams();
  const { setFailedMessage, setSuccessfulMessage } =
    useContext(NotificationContext);
  const { user } = useContext(AuthContext);
  const userToken = user.tokenInfo.token;
  const [achievement, setAchievement] = useState({
    progress: 0,
    replace: false,
  });

  const handleUpdate = async () => {
    try {
      await api.patch(
        `/achievement/${achName}/addProgress?progress=${achievement.progress}&replaceDailyProgress=${achievement.replace}`,
        {},
        {
          headers: { Authorization: `Bearer ${userToken}` },
        }
      );
      setSuccessfulMessage({
        message: "Achievement updated successfully!",
        flag: true,
      });
    } catch (error) {
      setFailedMessage({
        message:
          "Something went wrong with achievement updating . Please try again later!",
        flag: true,
      });
    }
    navigate(PathCreator.achievementPathId(achId));
  };
  return (
    <div className={styles.modalBackground}>
      <div className={styles.modal}>
        <h1 className={styles.title}>Add Progress</h1>
        <div className={styles.inputGroup}>
          <label className={styles.label} htmlFor="progress">
            Progress
          </label>
          <input
            className={styles.input}
            type="number"
            id="progress"
            placeholder="Progress"
            min={0}
            required
            value={achievement.progress}
            onChange={(e) =>
              setAchievement((value) => ({
                ...value,
                progress: e.target.value,
              }))
            }
          />
        </div>
        <div className={styles.inputGroup}>
          <label className={styles.label} htmlFor="completed">
            Replace The Existing One
          </label>
          <select
            className={styles.input}
            id="completed"
            defaultValue={"false"}
            value={achievement.replace}
            onChange={(e) =>
              setAchievement((value) => ({
                ...value,
                replace: e.target.value,
              }))
            }
          >
            <option value="true">True</option>
            <option value="false">False</option>
          </select>
        </div>
        <div className={styles.buttonGroup}>
          <button
            type="submit"
            className={styles.button}
            onClick={() => handleUpdate()}
          >
            Submit
          </button>
        </div>
      </div>
    </div>
  );
};

export default AchievementUpdate;
