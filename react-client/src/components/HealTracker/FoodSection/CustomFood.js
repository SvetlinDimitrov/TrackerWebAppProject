// CustomFood.js
import React, { useState } from "react";
import styles from "./CustomFood.module.css";
import { group } from "d3";
const groups = {
  // define which properties belong to which group
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
    "size",
    "calories",
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
const CustomFood = () => {
  const [foodItem, setFoodItem] = useState({
    name: "",
    size: 100,
    calories: 0,
    chromium: 0,
    fluoride: 0,
    iodine: 0,
    selenium: 0,
    molybdenum: 0,
    polyunsaturatedFat: 0,
    monounsaturatedFat: 0,
    fat: 0,
    saturatedFat: 0,
    protein: 0,
    fiber: 0,
    carbohydrates: 0,
    transFat: 0,
    sugar: 0,
    b9: 0,
    phosphorus: 0,
    calcium: 0,
    iron: 0,
    chloride: 0,
    zinc: 0,
    b7: 0,
    manganese: 0,
    sodium: 0,
    copper: 0,
    b3: 0,
    b12: 0,
    potassium: 0,
    b5: 0,
    b6: 0,
    magnesium: 0,
    d: 0,
    k: 0,
    b1: 0,
    b2: 0,
    e: 0,
    a: 0,
    c: 0,
  });

  const [activeGroup, setActiveGroup] = useState("");

  const handleChange = (event) => {
    setFoodItem({ ...foodItem, [event.target.name]: event.target.value });
  };

  const handleSubmit = (event) => {
    event.preventDefault();
    console.log(foodItem);
  };

  return (
    <div className={styles.container}>
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
                  {key}
                </label>
                <input
                  className={styles.input}
                  type={key === "name" ? "text" : "number"}
                  id={key}
                  name={key}
                  value={foodItem[key]}
                  onChange={handleChange}
                  required={key === "name" || key === "calories"}
                  min={key !== "name" ? 0 : undefined}
                  minLength={key === "name" ? 3 : undefined}
                />
              </div>
            )
        )}
        <button className={styles.submitButton} type="submit">
          Create Food
        </button>
      </form>
    </div>
  );
};
export default CustomFood;
