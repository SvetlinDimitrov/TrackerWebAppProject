import React, { createContext, useState, useEffect } from "react";
import api from "../util/api";
import data from "../data/food.json";

export const FoodContext = createContext();

const FoodContextProvider = ({ children }) => {
  const [allFoods, setAllFoods] = useState([]);
  const [nutrient, setNutrient] = useState({});

  useEffect(() => {
    setAllFoods(data);
  }, [allFoods.length]);

  if (allFoods.length === 0) {
    return <div id="preloader"></div>;
  }
  return (
    <FoodContext.Provider value={{allFoods , nutrient , setNutrient}}>{children}</FoodContext.Provider>
  );
};

export default FoodContextProvider;
