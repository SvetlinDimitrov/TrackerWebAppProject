import React, { useContext, useState } from "react";
import { useNavigate } from "react-router-dom";

import * as PathCreator from "../../util/PathCreator";
import api from "../../util/api";
import styles from "./CreateAchievement.module.css";
import { NotificationContext } from "../../context/Notification";
import { AuthContext } from "../../context/UserAuth";

const CreateAchievement = () => {
  const navigate = useNavigate();
  const { setFailedMessage, setSuccessfulMessage } =
    useContext(NotificationContext);
  const { user } = useContext(AuthContext);
  const userToken = user.tokenInfo.token;
  const [ach, setAch] = useState({
    name: "",
    description: "",
    goal: 0,
    measurement: "",
  });

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      await api.post(`/achievement`, ach, {
        headers: { Authorization: `Bearer ${userToken}` },
      });
      setSuccessfulMessage({
        message: "Achievement created successfully!",
        flag: true,
      });
    } catch (error) {
      setFailedMessage({
        message:
          "Something went wrong with record creation. Please try again later!",
        flag: true,
      });
    }
    setAch(undefined);
    navigate(PathCreator.basicAchievementPath());
  };
  return (
    <div className={styles.overlay}>
      <div className={styles.popup}>
        <button
          className={styles.closeButton}
          onClick={() => navigate(PathCreator.basicAchievementPath())}
        >
          X
        </button>
        <form onSubmit={handleSubmit} className={styles.form}>
          <h2 className={styles.header}>Create Achievement</h2>
          <input
            type="text"
            value={ach.name}
            minLength={3}
            onChange={(e) =>
              setAch((value) => ({ ...value, name: e.target.value }))
            }
            className={styles.input}
            placeholder="Achievement Name"
            required
          />
          <input
            type="text"
            value={ach.description}
            minLength={3}
            onChange={(e) =>
              setAch((value) => ({ ...value, description: e.target.value }))
            }
            className={styles.input}
            placeholder="Achievement Description"
            required
          />

          <input
            type="number"
            value={ach.goal}
            min={0}
            onChange={(e) =>
              setAch((value) => ({ ...value, goal: e.target.value }))
            }
            className={styles.input}
            placeholder="Achievement Goal"
            required
          />

          <input
            type="text"
            value={ach.measurement}
            onChange={(e) =>
              setAch((value) => ({ ...value, measurement: e.target.value }))
            }
            className={styles.input}
            placeholder="Achievement Measurement"
            required
          />
          <button type="submit" className={styles.button}>
            Create
          </button>
        </form>
      </div>
    </div>
  );
};

export default CreateAchievement;
