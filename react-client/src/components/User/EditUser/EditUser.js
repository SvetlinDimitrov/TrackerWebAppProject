import React, { useState, useEffect, useContext } from "react";
import { useNavigate } from "react-router-dom";
import { AuthContext } from "../../../context/UserAuth";
import { NotificationContext } from "../../../context/Notification";

import api from "../../../util/api";
import styles from "./EditUser.module.css";

const EditUser = () => {
  const { userToken , loginUser } = useContext(AuthContext);
  const { setSuccessfulMessage, setFailedMessage } =
    useContext(NotificationContext);
  const [user, setUser] = useState({});
  const navigate = useNavigate();

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await api.get("/auth/details", {
          headers: { Authorization: `Bearer ${userToken}` },
        });

        setUser(response.data);
      } catch (error) {
        navigate("/logout");
      }
    };

    fetchData();
  }, [navigate, userToken]);

  const handleSubmit = async (e) => {
    await e.preventDefault();
    try {
      const response = await api.patch("/auth/edit", user, {
        headers: { Authorization: `Bearer ${userToken}` },
      });
      loginUser(response.data.token);
      setSuccessfulMessage( {message:'Changes were made successfully!' , flag:true} );
      navigate("/");
    } catch (error) {
      setFailedMessage( {message:'Your has expired please login again!' , flag:true} );
      navigate("/logout");
    }
  };

  return (
    <>
      {user.id !== undefined ? (
        <div className={styles.body}>
          <div className={styles.container}>
            <h1 className={styles.title}>Edit User Settings</h1>
            <form onSubmit={handleSubmit} method="POST">
              <div className={styles.formGroup}>
                <label htmlFor="username">Username</label>
                <input
                  type="text"
                  id="username"
                  name="username"
                  minLength="4"
                  required
                  value={user.username ? user.username : ""}
                  onChange={(e) =>
                    setUser({ ...user, username: e.target.value })
                  }
                />
              </div>

              <div className={styles.formGroup}>
                <label htmlFor="kilograms">Weight (kg)</label>
                <input
                  type="number"
                  id="kilograms"
                  name="kilograms"
                  step="0.01"
                  min="5"
                  value={user.kilograms || ""}
                  onChange={(e) =>
                    setUser({ ...user, kilograms: e.target.value })
                  }
                />
              </div>
              <div className={styles.formGroup}>
                <label htmlFor="height">Height (cm)</label>
                <input
                  type="number"
                  id="height"
                  name="height"
                  step="0.01"
                  min="0.5"
                  value={user.height || ""}
                  onChange={(e) => setUser({ ...user, height: e.target.value })}
                />
              </div>
              <div className={styles.formGroup}>
                <label htmlFor="workoutState">Workout State</label>
                <select
                  id="workoutState"
                  name="workoutState"
                  value={user.workoutState ? user.workoutState : ""}
                  onChange={(e) =>
                    setUser({ ...user, workoutState: e.target.value })
                  }
                >
                  <option value="">Choose Workout State</option>
                  <option value="SEDENTARY">Sedentary</option>
                  <option value="LIGHTLY_ACTIVE">Lightly Active</option>
                  <option value="MODERATELY_ACTIVE">Moderately Active</option>
                  <option value="VERY_ACTIVE">Very Active</option>
                  <option value="SUPER_ACTIVE">Super Active</option>
                </select>
              </div>
              <div className={styles.formGroup}>
                <label htmlFor="gender">Gender</label>
                <select
                  id="gender"
                  name="gender"
                  value={user.gender ? user.gender : ""}
                  onChange={(e) => setUser({ ...user, gender: e.target.value })}
                >
                  <option value="">Choose Gender</option>
                  <option value="MALE">Male</option>
                  <option value="FEMALE">Female</option>
                </select>
              </div>
              <div className={styles.formGroup}>
                <label htmlFor="age">Age</label>
                <input
                  type="number"
                  id="age"
                  name="age"
                  step="1"
                  min="1"
                  value={user.age || ""}
                  onChange={(e) => setUser({ ...user, age: e.target.value })}
                />
              </div>
              <button className={styles.submitButton}>Save Changes</button>
            </form>
          </div>
        </div>
      ) : (
        <div id="preloader"></div>
      )}
    </>
  );
};

export default EditUser;
