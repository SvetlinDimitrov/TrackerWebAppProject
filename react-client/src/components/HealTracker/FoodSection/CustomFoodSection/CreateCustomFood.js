import React, { useState, useContext } from "react";
import { useParams, useNavigate } from "react-router-dom";
import styles from "./CreateCustomFood.module.css";

import { AuthContext } from "../../../../context/UserAuth";
import { NotificationContext } from "../../../../context/Notification";
import api from "../../../../util/api";
import * as PathCreator from "../../../../util/PathCreator";

const groups = {
  vitamin: [
    "a",
    "b",
    "c",
    "d",
    "e",
    "k",
    "b1",
    "b2",
    "b3",
    "b5",
    "b6",
    "b7",
    "b9",
    "b12",
  ],
  macro: [
    "fat",
    "saturatedFat",
    "transFat",
    "monounsaturatedFat",
    "polyunsaturatedFat",
    "carbohydrates",
    "sugar",
    "fiber",
    "protein",
  ],
  mineral: [
    "calcium",
    "iron",
    "magnesium",
    "phosphorus",
    "potassium",
    "sodium",
    "zinc",
    "copper",
    "manganese",
    "selenium",
    "chromium",
    "molybdenum",
    "iodine",
    "fluoride",
    "chloride",
  ],
};
const CreateCustomFood = () => {
  const [foodItem, setFoodItem] = useState({
    name: "",
    size: "100.00",
    calories: "0.00",
    chromium: "0.00",
    fluoride: "0.00",
    iodine: "0.00",
    selenium: "0.00",
    molybdenum: "0.00",
    polyunsaturatedFat: "0.00",
    monounsaturatedFat: "0.00",
    fat: "0.00",
    saturatedFat: "0.00",
    protein: "0.00",
    fiber: "0.00",
    carbohydrates: "0.00",
    transFat: "0.00",
    sugar: "0.00",
    b9: "0.00",
    phosphorus: "0.00",
    calcium: "0.00",
    iron: "0.00",
    chloride: "0.00",
    zinc: "0.00",
    b7: "0.00",
    manganese: "0.00",
    sodium: "0.00",
    copper: "0.00",
    b3: "0.00",
    b12: "0.00",
    potassium: "0.00",
    b5: "0.00",
    b6: "0.00",
    magnesium: "0.00",
    d: "0.00",
    k: "0.00",
    b1: "0.00",
    b2: "0.00",
    e: "0.00",
    a: "0.00",
    c: "0.00",
  });
  const { user } = useContext(AuthContext);
  const { setSuccessfulMessage, setFailedMessage } =
    useContext(NotificationContext);
  const { recordId, storageId } = useParams();
  const [activeGroup, setActiveGroup] = useState("");
  const navigate = useNavigate();
  const userToken = user.tokenInfo.token;

  const handleChange = (event) => {
    setFoodItem({ ...foodItem, [event.target.name]: event.target.value });
  };
  const handleSubmit = async (event) => {
    event.preventDefault();
    try {
      await api.post("food", foodItem, {
        headers: { Authorization: `Bearer ${userToken}` },
      });
      setSuccessfulMessage({
        message: "FOOD: " + foodItem.name + " was created successfully!",
        flag: true,
      });
    } catch (error) {
      console.log(error.response.data);
      if (
        error.response.status === 400 &&
        error.response.data.message ===
          "Food with name " + foodItem.name + " already exists."
      ) {
        setFailedMessage({
          message: "Food with name " + foodItem.name + " already exists.",
          flag: true,
        });
      } else {
        setFailedMessage({
          message:
            "Something went wrong with record creation. Please try again later!",
          flag: true,
        });
      }
    }
    navigate(PathCreator.customFoodPath(recordId, storageId));
  };
  const onClose = () => {
    navigate(PathCreator.customFoodPath(recordId, storageId));
  };

  return (
    <div className={styles.overlay}>
      <button className={styles.closeButton} onClick={onClose}>
        X
      </button>
      <div className={styles.popupForm}>
        <h1 className={styles.title}>Create Custom Food</h1>
        <div className={styles.buttonGroup}>
          <button
            className={styles.groupButton}
            onClick={() =>
              setActiveGroup((group) => (group === "vitamin" ? "" : "vitamin"))
            }
          >
            Vitamin
          </button>
          <button
            className={styles.groupButton}
            onClick={() =>
              setActiveGroup((group) => (group === "macro" ? "" : "macro"))
            }
          >
            Macro
          </button>
          <button
            className={styles.groupButton}
            onClick={() =>
              setActiveGroup((group) => (group === "mineral" ? "" : "mineral"))
            }
          >
            Mineral
          </button>
        </div>
        <form className={styles.form} onSubmit={handleSubmit}>
          {Object.keys(foodItem).map(
            (key) =>
              ((activeGroup && groups[activeGroup].includes(key)) ||
                ["name", "size", "calories"].includes(key)) && (
                <div key={key} className={styles.inputGroup}>
                  <label className={styles.label} htmlFor={key}>
                    {key.charAt(0).toUpperCase() + key.slice(1)}
                  </label>
                  <input
                    className={styles.input}
                    type={key === "name" ? "Text" : "Number"}
                    id={key}
                    name={key}
                    value={foodItem[key]}
                    onChange={handleChange}
                    required={key === "name" || key === "calories"}
                    min={key !== "name" ? 0 : undefined}
                    minLength={key === "name" ? 3 : undefined}
                    max={key !== "name" ? 1000000 : undefined}
                  />
                </div>
              )
          )}
          <button className={styles.submitButton} type="submit">
            Create Food
          </button>
        </form>
      </div>
    </div>
  );
};
export default CreateCustomFood;
