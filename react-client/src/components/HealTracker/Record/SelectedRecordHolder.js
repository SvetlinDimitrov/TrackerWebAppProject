import React, { useEffect, useContext, useState } from "react";
import { useParams, useNavigate, Outlet , useLocation} from "react-router-dom";

import styles from "./SelectedRecordHolder.module.css";
import SelectRecordDetails from "./SelectRecordDetails";
import DetailsOfRecord from "./DetailsOfRecord";
import DeleteRecord from "./DeleteRecord";
import CreateStorage from "../Storage/CreateStorage";
import api from "../../../util/api";
import { AuthContext } from "../../../context/UserAuth";
import { NotificationContext } from "../../../context/Notification";
import SelectStorage from "../Storage/SelectStorage";

const SelectedRecordHolder = () => {
  const location = useLocation();
  const { recordId } = useParams();
  const { user } = useContext(AuthContext);
  const [record, setRecord] = useState(undefined);
  const userToken = user.tokenInfo.token;
  const { setFailedMessage, setSuccessfulMessage } =
    useContext(NotificationContext);
  const [showMoreInfo, setShowMoreInfo] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await api.get("/record/" + recordId, {
          headers: { Authorization: `Bearer ${userToken}` },
        });
        setRecord(response.data);
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
  }, [navigate, recordId, setFailedMessage, userToken , location]);

  if (record === undefined) {
    return <div id="preloader"></div>;
  }

  return (
    <>
      <div className={styles.main_container}>
        <SelectRecordDetails
          selectedRecord={record}
          setShowMoreInfo={setShowMoreInfo}
          showMoreInfo={showMoreInfo}
        />

        <DeleteRecord
          selectedRecord={record}
          userToken={userToken}
          setSuccessfulMessage={setSuccessfulMessage}
          setFailedMessage={setFailedMessage}
        />

        <CreateStorage
          userToken={userToken}
          setFailedMessage={setFailedMessage}
          setSuccessfulMessage={setSuccessfulMessage}
          recordId={record.id}
          
        />
        <SelectStorage recordId={record.id} storages={record.storageViews} />
      </div>
      <Outlet/>
      {showMoreInfo && <DetailsOfRecord record={record} />}
    </>
  );
};

export default SelectedRecordHolder;
