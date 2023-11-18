import { faMinus, faPlus } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import React, { useContext, useState } from "react";
import { useNavigate } from "react-router-dom";
import { NotificationContext } from "../../../context/Notification";
import { RecordContext } from "../../../context/RecordContext";
import { AuthContext } from "../../../context/UserAuth";
import { getRecordById, getStorageById } from "../../../util/RecordUtils";
import { validatedUserToken } from "../../../util/UserUtils";
import { AddFoodMenu } from "./AddFoodMenu";
import styles from "./FoodSection.module.css";

const FoodSection = () => {
  const { user, logout } = useContext(AuthContext);
  const { setFailedMessage } = useContext(NotificationContext);
  const { allRecords } = useContext(RecordContext);
  const [recordId, setRecordId] = useState(-1);
  const [storageId, setStorageId] = useState(-1);
  const [showAddFoodMenu, setShowAddFoodMenu] = useState(false);
  const navigate = useNavigate();
  const validatedUser = validatedUserToken(user);

  const handleSelectRecord = (e) => {
    const currentRecordId = +e.target.value;
    setRecordId(currentRecordId);
  };
  const handleSelectStorage = (e) => {
    const currentStorage = +e.target.value;
    setStorageId(currentStorage);
  };
  const handleRemoveFood = (foodName) => {
    console.log(foodName);
  };
  const handleAddFoodMenu = () => {
    setShowAddFoodMenu(!showAddFoodMenu);
  };

  if (validatedUser === "userNotCompleted") {
    setFailedMessage({
      message: "Please complete your user details. Go to edit user page!",
      flag: true,
    });
    navigate("/");
    return;
  }
  if (validatedUser === "dateExpired") {
    setFailedMessage({
      message: "Your token has expired please login again!",
      flag: true,
    });
    logout();
    navigate("/");
    return;
  }
  if(!allRecords){
    return <div id="preloader"></div>;
  }
  return (
    <>
      {showAddFoodMenu && (
        <div style={{ position: "relative" }}>
          <div className={styles.popup_container}>
            <AddFoodMenu onClose={handleAddFoodMenu} />
          </div>
        </div>
      )}
      <div className={styles.mainContainer}>
        <h1 className={styles.heading}>Food Section</h1>
        <div className={styles.containerInfo}>
          <p className={styles.paragraph}>
            Make the perfect balanced food record
          </p>
          <select
            defaultValue=""
            className={styles.selectValue}
            onChange={(e) => handleSelectRecord(e)}
          >
            <option value="-1">Choose Record</option>
            {allRecords.map((record) => {
              return (
                <option value={record.id} key={record.id}>
                  {record.name.substring(0, 70)}
                </option>
              );
            })}
          </select>
          {recordId !== -1 && (
            <>
              <select
                defaultValue=""
                className={styles.selectValue}
                onChange={(e) => handleSelectStorage(e)}
              >
                <option value="-1">Choose Storage</option>
                {getRecordById(allRecords, recordId).storageViews.map(
                  (storage) => {
                    return (
                      <option value={storage.id} key={storage.id}>
                        {storage.name.substring(0, 70)}
                      </option>
                    );
                  }
                )}
              </select>
            </>
          )}
        </div>
        {storageId !== -1 && (
          <>
            <h1 className={styles.heading}>Listed Foods</h1>
            <div className={styles.containerInfo}>
              <div className={styles.containerInfo_addFoodDiv}>
                <span>Add Food</span>
                <FontAwesomeIcon
                  icon={faPlus}
                  onClick={() => handleAddFoodMenu()}
                  className={styles.food_infoContainer_button}
                />
              </div>
            </div>
            {getStorageById(allRecords, recordId, storageId).foods.map(
              (food, index) => {
                <div key={index} className={styles.food_infoContainer}>
                  <p className={styles.food_infoContainer_paragraph}>
                    {food.name}
                  </p>
                  <p className={styles.food_infoContainer_paragraph}>
                    {food.size}
                    {food.measurement}
                  </p>
                  <p className={styles.food_infoContainer_paragraph}>
                    {food.calories}
                  </p>
                  <p className={styles.food_infoContainer_paragraph}>
                    Remove
                    <FontAwesomeIcon
                      icon={faMinus}
                      className={styles.food_infoContainer_button}
                      onClick={() => handleRemoveFood(food.name)}
                    />
                  </p>
                  <div className={styles.food_infoContainer}></div>
                </div>;
              }
            )}
          </>
        )}
      </div>
    </>
  );
};

export default FoodSection;
