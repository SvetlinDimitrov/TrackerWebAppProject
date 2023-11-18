import { faTimes } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { useContext, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { NotificationContext } from "../../../context/Notification";
import api from "../../../util/api";
import styles from "./AddFoodMenu.module.css";

export const AddFoodMenu = ({ onClose }) => {
  const [foods, setFoods] = useState([]);
  const { setFailedMessage } = useContext(NotificationContext);
  const navigate = useNavigate();

  useEffect(() => {
    const getFoods = async () => {
      try {
        const getFoodsData = await api.get("/food");
        setFoods(getFoodsData.data);
      } catch (error) {
        setFailedMessage({
          message: "Something went wrong . Please try again later!",
          flag: true,
        });
        navigate("/");
      }
    };

    getFoods();
  }, []);

  return (
    <div className={styles.containerInfo}>
      <div className={styles.containerInfo_Heading_CloseButton}>
        <span>Close</span>
        <FontAwesomeIcon
          className={styles.containerInfo_Heading_CloseButton_Button}
          icon={faTimes}
          onClick={() => onClose()}
        />
      </div>
    </div>
  );
};
