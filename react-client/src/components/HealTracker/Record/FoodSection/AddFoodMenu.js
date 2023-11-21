import { faTimes, faPlus } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../../../../util/api";
import styles from "./AddFoodMenu.module.css";
import styles2 from "../FoodSection/FoodSection.module.css";

export const AddFoodMenu = ({
  onClose,
  setFailedMessage,
  setSuccessfulMessage,
  userToken,
  selectedRecord,
  storage,
}) => {
  const [foods, setFoods] = useState([]);
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
        navigate("/health-tracker");
      }
    };

    getFoods();
  }, [navigate, setFailedMessage]);

  const handleAdd = async (food) => {
    if (
      window.confirm(
        "Are you sure you want to add" +
          food.size +
          " " +
          food.measurement +
          " of " +
          food.name +
          "?"
      )
    ) {
      try {
        await api.patch(
          `/storage/addFood`,
          {
            foodName: food.name,
            recordId: selectedRecord.id,
            storageId: storage.id,
          },
          {
            headers: { Authorization: `Bearer ${userToken}` },
          }
        );
        setSuccessfulMessage({
          message:
            food.size +
            " " +
            food.measurement +
            " of " +
            food.name +
            " added successfully!",
          flag: true,
        });
      } catch (error) {
        setFailedMessage({
          message:
            "Something went wrong with food addition. Please try again later!",
          flag: true,
        });
      }
      navigate("/health-tracker");
    }
  };

  return (
    <div className={styles.overlay}>
      <div className={styles.containerInfo}>
        <div className={styles.containerInfo_Heading_CloseButton}>
          <div className={styles.closeButtonContainer}>
            <button
              className={styles.closeButton}
              onClick={() => onClose(false)}
            >
              <FontAwesomeIcon
                className={styles.closeButtonIcon}
                icon={faTimes}
              />
              <span>Close</span>
            </button>
          </div>
          <h1>List of foods</h1>
          <div className={styles2.foodDetails}>
            {foods.map((food, index) => {
              return (
                <div className={styles2.foodItem} key={index}>
                  <div className={styles2.foodItemDetails}>
                    <div>Name: {food.name}</div>
                    <div>
                      {food.size} {food.measurement}
                    </div>
                  </div>
                  <div className={styles2.foodItemDetails}>
                    <div>Calories: {food.calories}</div>
                    <button
                      className={styles2.deleteButton}
                      onClick={() => handleAdd(food)}
                    >
                      <FontAwesomeIcon icon={faPlus} />
                    </button>
                  </div>
                </div>
              );
            })}
          </div>
        </div>
      </div>
    </div>
  );
};
