import React, { createContext, useState, useEffect } from "react";
import api from "../util/api";

export const FoodContext = createContext();

const FoodContextProvider = ({ children }) => {
  const [allFoods, setAllFoods] = useState([]);
  const [nutrient, setNutrient] = useState({});

  useEffect(() => {
    const getFoods = async () => {
      try {
        const getFoodsData = await api.get("/food");
        setAllFoods(getFoodsData.data);
      } catch (error) {
        console.log(error);
      }
    };
    if (allFoods.length === 0) {
      getFoods();
    }
  }, [allFoods.length]);

  if (allFoods.length === 0) {
    return <div id="preloader"></div>;
  }
  return (
    <FoodContext.Provider value={{allFoods , nutrient , setNutrient}}>{children}</FoodContext.Provider>
  );
};

export default FoodContextProvider;
