import React, { useContext, useEffect, useState } from "react";
import { useNavigate, useParams ,  useLocation} from "react-router-dom";

import { NotificationContext } from "../../../context/Notification";
import { AuthContext } from "../../../context/UserAuth";
import api from "../../../util/api";
import FoodSection from "../FoodSection/FoodSection";
import DeleteStorage from "./DeleteStorage";
import styles from "./SelectedStorageHolder.module.css";

const SelectedStorageHolder = () => {
  const location = useLocation();
  const { recordId, storageId } = useParams();
  const { user } = useContext(AuthContext);
  const [storage, setStorage] = useState(undefined);
  const userToken = user.tokenInfo.token;
  const { setFailedMessage, setSuccessfulMessage } =
    useContext(NotificationContext);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await api.get(
          `/storage?recordId=${recordId}&storageId=${storageId}`,
          {
            headers: { Authorization: `Bearer ${userToken}` },
          }
        );
        setStorage(response.data);
      } catch (error) {
        setFailedMessage({
          message:
            "Something went wrong with record creation. Please try again later!",
          flag: true,
        });
        navigate("/");
      }
    };
    setStorage(undefined);
    fetchData();
  }, [navigate, recordId, storageId, setFailedMessage, userToken , location]);
  return (
    <div className={styles.main_container}>
      <DeleteStorage
        selectedStorage={storage}
        userToken={userToken}
        recordId={recordId}
        setFailedMessage={setFailedMessage}
        setSuccessfulMessage={setSuccessfulMessage}
      />
      <FoodSection
        recordId={recordId}
        userToken={userToken}
        setSuccessfulMessage={setSuccessfulMessage}
        setFailedMessage={setFailedMessage}
        selectedStorage={storage}
      />
    </div>
  );
};

export default SelectedStorageHolder;
