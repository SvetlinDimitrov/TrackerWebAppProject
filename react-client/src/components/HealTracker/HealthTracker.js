import React, { useContext, useEffect, useState } from "react";
import { Outlet, useLocation, useNavigate } from "react-router-dom";
import styles from "./HeathTracker.module.css";

import { NotificationContext } from "../../context/Notification";
import { AuthContext } from "../../context/UserAuth";
import FailedMessage from "../Notifications/FailedMessage";
import SuccessMessage from "../Notifications/SuccessfulMessage";
import stylesNotification from "../Notifications/SuccessfulMessage.module.css";

import api from "../../util/api";
import CreateRecord from "./Record/CreateRecord";
import SelectRecord from "./Record/SelectRecord";

const HealthTracker = () => {
  const location = useLocation();
  const navigate = useNavigate();

  const [records, setRecords] = useState(undefined);

  const { user } = useContext(AuthContext);
  const {
    setFailedMessage,
    setSuccessfulMessage,
    failedMessage,
    successfulMessage,
  } = useContext(NotificationContext);

  const userToken = user.tokenInfo.token;

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await api.get("/record/all", {
          headers: { Authorization: `Bearer ${userToken}` },
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
  }, [navigate, setFailedMessage, userToken, location]);

  if (!records) {
    return <div id="preloader"></div>;
  }

  return (
    <>
      {(successfulMessage.flag || failedMessage.flag) && (
        <div className={stylesNotification.overlay}></div>
      )}
      {successfulMessage.flag && (
        <SuccessMessage
          message={successfulMessage.message}
          onClose={() => {
            setSuccessfulMessage(false);
          }}
        />
      )}
      {failedMessage.flag && (
        <FailedMessage
          message={failedMessage.message}
          onClose={() => {
            setFailedMessage(false);
          }}
        />
      )}

      <div className={styles.main_container}>
        <CreateRecord
          userToken={userToken}
          setFailedMessage={setFailedMessage}
          setSuccessfulMessage={setSuccessfulMessage}
        />
        <SelectRecord records={records} />
      </div>

      <Outlet />
    </>
  );
};

export default HealthTracker;
