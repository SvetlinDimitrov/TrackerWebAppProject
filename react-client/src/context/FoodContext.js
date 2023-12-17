import React, { createContext, useState } from "react";
import allFoods from "../data/food.json";

export const FoodContext = createContext();

const FoodContextProvider = ({ children }) => {
  const [nutrient, setNutrient] = useState({});

  return (
    <FoodContext.Provider value={{ allFoods, nutrient, setNutrient }}>
      {children}
    </FoodContext.Provider>
  );
};

export default FoodContextProvider;
