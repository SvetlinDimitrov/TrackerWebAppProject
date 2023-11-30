import { useEffect, useState, useContext } from "react";
import { useNavigate, useParams } from "react-router-dom";

import styles from "./FoodMenu.module.css";
import * as PathCreator from "../../../util/PathCreator";
import api from "../../../util/api";
import { NotificationContext } from "../../../context/Notification";

export const FoodMenu = () => {
  const { recordId, storageId } = useParams();
  const { setFailedMessage } = useContext(NotificationContext);
  const [foods, setFoods] = useState();
  const navigate = useNavigate();
  const [searchTerm, setSearchTerm] = useState("");

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
        navigate("/health-tracker");
      }
    };

    getFoods();
  }, [navigate, setFailedMessage]);

  const onClose = () => {
    navigate(PathCreator.storagePath(recordId, storageId));
  };

  if (foods === undefined) {
    return <div id="preloader"></div>;
  }

  return (
    <>
      <div className={styles.overlay}>
        <button className={styles.closeButton} onClick={onClose}>
          X
        </button>
        <div className={styles.container}>
          <div className={styles.container_CloseButton}>
            <h1 className={styles.foodMenuTitle}>List Of Foods</h1>
            <input
              type="text"
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
            />
            <div className={styles.container_foodDetails}>
              {foods
                .filter((food) =>
                  food.name.toLowerCase().includes(searchTerm.toLowerCase())
                )
                .map((food, index) => {
                  return (
                    <div
                      className={styles.container_foodDetails_food}
                      key={index}
                      onClick={() =>
                        navigate(
                          PathCreator.basicFoodPath(
                            recordId,
                            storageId,
                            food.name,
                            food.size,
                            true
                          )
                        )
                      }
                    >
                      <p className={styles.container_foodDetails_food_info}>
                        {food.name}
                      </p>
                    </div>
                  );
                })}
            </div>
          </div>
        </div>
      </div>
    </>
  );
};

export default FoodMenu;
