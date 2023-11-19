import React, { useContext, useEffect, useState } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import DetailsOfRecord from "./Record/DetailsOfRecord";
import styles from "./HeathTracker.module.css";

import { NotificationContext } from "../../context/Notification";
import { AuthContext } from "../../context/UserAuth";
import SuccessMessage from "../Notifications/SuccessfulMessage";
import FailedMessage from "../Notifications/FailedMessage";
import stylesNotification from "../Notifications/SuccessfulMessage.module.css";

import api from "../../util/api";
import SelectRecordDetails from "./Record/SelectRecordDetails";
import CreateRecord from "./Record/CreateRecord";
import SelectRecord from "./Record/SelectRecord";
import DeleteRecord from "./Record/DeleteRecord";
import FoodSection from "./Record/FoodSection/FoodSection";

const HealthTracker = () => {
  const [records, setRecords] = useState(undefined);
  const [selectedRecord, setSelectedRecord] = useState(undefined);
  const [feature, selectedFeature] = useState("");
  const location = useLocation();
  const { user } = useContext(AuthContext);
  const {
    setFailedMessage,
    setSuccessfulMessage,
    failedMessage,
    successfulMessage,
  } = useContext(NotificationContext);
  const navigate = useNavigate();
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
    setSelectedRecord(undefined);
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
        <SelectRecord
          records={records}
          selectedRecord={selectedRecord}
          setSelectedRecord={setSelectedRecord}
        />

        {selectedRecord && (
          <SelectRecordDetails
            selectedRecord={selectedRecord}
            feature={feature}
            selectedFeature={selectedFeature}
          />
        )}
        {selectedRecord && (
          <DeleteRecord
            selectedRecord={selectedRecord}
            userToken={userToken}
            setSuccessfulMessage={setSuccessfulMessage}
            setFailedMessage={setFailedMessage}
          />
        )}
        {selectedRecord && (
          <FoodSection
            selectedRecord={selectedRecord}
            userToken={userToken}
          />
        )}
      </div>

      {feature === "details" && selectedRecord && (
        <DetailsOfRecord record={selectedRecord} />
      )}
    </>
  );
};

export default HealthTracker;
