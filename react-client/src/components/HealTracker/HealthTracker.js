import React, { useContext, useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import styles from "./HeathTracker.module.css";
import DetailsOfRecord from "./Record/DetailsOfRecord";

import { NotificationContext } from "../../context/Notification";
import { AuthContext } from "../../context/UserAuth";
import FailedMessage from "../Notifications/FailedMessage";
import SuccessMessage from "../Notifications/SuccessfulMessage";
import stylesNotification from "../Notifications/SuccessfulMessage.module.css";

import api from "../../util/api";
import CreateRecord from "./Record/CreateRecord";
import DeleteRecord from "./Record/DeleteRecord";
import FoodSection from "./FoodSection/FoodSection";
import SelectRecord from "./Record/SelectRecord";
import SelectRecordDetails from "./Record/SelectRecordDetails";
import CreateStorage from "./Storage/CreateStorage";
import DeleteStorage from "./Storage/DeleteStorage";

const HealthTracker = () => {
  const location = useLocation();
  const navigate = useNavigate();

  const [records, setRecords] = useState(undefined);
  const [selectedRecord, setSelectedRecord] = useState(undefined);
  const [selectedStorage, setSelectedStorage] = useState(undefined);

  const [feature, selectedFeature] = useState("");

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
          <CreateStorage
            userToken={userToken}
            setFailedMessage={setFailedMessage}
            setSuccessfulMessage={setSuccessfulMessage}
            selectedRecord={selectedRecord}
          />
        )}
        {selectedRecord && (
          <FoodSection
            selectedRecord={selectedRecord}
            selectedStorage={selectedStorage}
            setSelectedStorage={setSelectedStorage}
            userToken={userToken}
            setSuccessfulMessage={setSuccessfulMessage}
            setFailedMessage={setFailedMessage}
          />
        )}
        {selectedRecord && selectedStorage && (
          <DeleteStorage
            selectedRecord={selectedRecord}
            userToken={userToken}
            setSuccessfulMessage={setSuccessfulMessage}
            setFailedMessage={setFailedMessage}
            selectedStorage={selectedStorage}
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
