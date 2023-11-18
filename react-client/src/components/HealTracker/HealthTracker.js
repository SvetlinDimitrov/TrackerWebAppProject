import React, { useContext, useEffect, useState } from "react";
import { FaHeart } from "react-icons/fa";
import { Outlet, useNavigate } from "react-router-dom";
import styles from "./HeathTracker.module.css";

import { NotificationContext } from "../../context/Notification";
import { AuthContext } from "../../context/UserAuth";

import api from "../../util/api";

const HealthTracker = () => {
  const [records, setRecords] = useState(undefined);
  const [selectedRecord, setSelectedRecord] = useState(-1);
  const { user } = useContext(AuthContext);
  const { setFailedMessage } = useContext(NotificationContext);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await api.get("/record/all", {
          headers: { Authorization: `Bearer ${user.tokenInfo.token}` },
        });
        setRecords(response.data);
      } catch (error) {
        setFailedMessage({
          message:
            "Something went wrong with record creation. Please try again later!",
          flag: true,
        });
        navigate("/");
      }
    };
    fetchData();
  }, []);

  const selectRecordIndex = (index) => {
    setSelectedRecord(+index);
  };

  const selectPathName = (pathName) => {
    navigate(pathName);
  };

  if (!records) {
    return <div id="preloader"></div>;
  }

  return (
    <>
      <div className={styles.main_container}>
        <div className={styles.container}>
          <h2 className={styles.container_header}>
            Select record for more details
          </h2>
          <FaHeart className={styles.container_icon} />
          <p className={styles.container_text}>
            Choose a record from the dropdown:
          </p>
          <select
            onChange={(e) => selectRecordIndex(parseInt(e.target.value, 10))}
            defaultValue="-1"
            className={styles.choose_RecordContainer_selectValue}
          >
            <option value="-1">Choose Record</option>
            {records.map((record, index) => {
              return (
                <option value={record.id} key={index}>
                  {record.name.substring(0, 70)}
                </option>
              );
            })}
          </select>
        </div>

        {selectedRecord !== -1 && (
          <div className={styles.container}>
            <h2 className={styles.container_header}>
              Select record for more details
            </h2>
            <FaHeart className={styles.container_icon} />
            <p className={styles.container_text}>
              Choose a record from the dropdown:
            </p>
            <select
              onChange={(e) => selectPathName(e.target.value)}
              defaultValue=""
              className={styles.choose_RecordContainer_selectValue}
            >
              <option value="/health-tracker">Choose Record</option>
              <option value={`/health-tracker/record/${selectedRecord}`}>
                Record Detail Information
              </option>
            </select>
          </div>
        )}
      </div>
      <Outlet />
    </>
  );
};

export default HealthTracker;
