import { faTimes } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../../../../util/api";
import styles2 from "../FoodSection/FoodSection.module.css";
import styles from "./AddFoodMenu.module.css";
import FoodItem from "./FoodItem";

export const AddFoodMenu = ({
  onClose,
  setFailedMessage,
  setSuccessfulMessage,
  userToken,
  selectedRecord,
  storage,
}) => {
  const [foods, setFoods] = useState([]);
  const [food, setFood] = useState(undefined);
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

  const handleAdd = async (food, length) => {
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
          `/storage/${storage.id}/addFood?recordId=${selectedRecord.id}`,
          {
            foodName: food.name,
            amount: length,
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

  console.log(1);
  return (
    <>
      {food && (
        <FoodItem
          fun={"Add Food"}
          showFood={food}
          setShowFood={setFood}
          userToken={userToken}
          onAddChangeFunction={handleAdd}
        />
      )}
      <div className={styles.overlay}>
        <div className={styles.containerInfo}>
          <div className={styles.containerInfo_Heading_CloseButton}>
            <h1>List of foods</h1>
            <input
              type="text"
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
            />
            <div className={styles.foodDetails}>
              {foods
                .filter((food) =>
                  food.name.toLowerCase().includes(searchTerm.toLowerCase())
                )
                .map((food, index) => {
                  return (
                    <div
                      className={styles2.foodItem}
                      key={index}
                      onClick={() => setFood(food)}
                    >
                      <div className={styles2.foodItemDetails}>
                        <div>{food.name}</div>
                      </div>
                    </div>
                  );
                })}
            </div>
            <div className={styles.closeButtonContainer}>
              <button
                className={styles.closeButton}
                onClick={() => onClose(false)}
              >
                Close
              </button>
            </div>
          </div>
        </div>
      </div>
    </>
  );
};
