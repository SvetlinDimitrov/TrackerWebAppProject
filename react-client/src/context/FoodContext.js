import React, { createContext, useState } from "react";
import api from "../util/api";

export const FoodContext = createContext();

const FoodContextProvider = ({ children }) => {
  const [allFoods, setAllFoods] = useState([]);
  const [sortedFoods, setSortedFoods] = useState(undefined);

  const foodInfo = {
    allFoods,
    sortedFoods,
  };

  if (allFoods.length === 0) {
    const getFoods = async () => {
      try {
        const getFoodsData = await api.get("/food");
        setAllFoods(getFoodsData.data);
        if (getFoodsData.data.length !== 0) {
          const sortedFoodsByVitaminA = getFoodsData.data
            .sort((a, b) => b.a - a.a)
            .slice(0, 50);

          const sortedFoodsByVitaminD = getFoodsData.data
            .sort((a, b) => b.d - a.d)
            .slice(0, 50);
          const sortedFoodsByVitaminE = getFoodsData.data
            .sort((a, b) => b.e - a.e)
            .slice(0, 50);
          const sortedFoodsByVitaminB5 = getFoodsData.data
            .sort((a, b) => b.b5 - a.b5)
            .slice(0, 50);
          const sortedFoodsByVitaminB2 = getFoodsData.data
            .sort((a, b) => b.b2 - a.b2)
            .slice(0, 50);
          const sortedFoodsByVitaminB3 = getFoodsData.data
            .sort((a, b) => b.b3 - a.b3)
            .slice(0, 50);
          const sortedFoodsByVitaminB1 = getFoodsData.data
            .sort((a, b) => b.b1 - a.b1)
            .slice(0, 50);
          const sortedFoodsByVitaminB6 = getFoodsData.data
            .sort((a, b) => b.b6 - a.b6)
            .slice(0, 50);
            const sortedFoodsByVitaminB7 = getFoodsData.data
            .sort((a, b) => b.b7 - a.b7)
            .slice(0, 50);
          const sortedFoodsByVitaminB9 = getFoodsData.data
            .sort((a, b) => b.b9 - a.b9)
            .slice(0, 50);
          const sortedFoodsByVitaminB12 = getFoodsData.data
            .sort((a, b) => b.b12 - a.b12)
            .slice(0, 50);
          const sortedFoodsByVitaminK = getFoodsData.data
            .sort((a, b) => b.k - a.k)
            .slice(0, 50);
          const sortedFoodsByVitaminC = getFoodsData.data
            .sort((a, b) => b.c - a.c)
            .slice(0, 50);
          const sortedFoodsByMagnesium = getFoodsData.data
            .sort((a, b) => b.magnesium - a.magnesium)
            .slice(0, 50);
          const sortedFoodsByPhosphorus = getFoodsData.data
            .sort((a, b) => b.phosphorus - a.phosphorus)
            .slice(0, 50);
          const sortedFoodsBySodium = getFoodsData.data
            .sort((a, b) => b.sodium - a.sodium)
            .slice(0, 50);
          const sortedFoodsByPotassium = getFoodsData.data
            .sort((a, b) => b.potassium - a.potassium)
            .slice(0, 50);
          const sortedFoodsByCalcium = getFoodsData.data
            .sort((a, b) => b.calcium - a.calcium)
            .slice(0, 50);
          const sortedFoodsByChloride = getFoodsData.data
            .sort((a, b) => b.chloride - a.chloride)
            .slice(0, 50);
          const sortedFoodsByChromium = getFoodsData.data
            .sort((a, b) => b.chromium - a.chromium)
            .slice(0, 50);
          const sortedFoodsByZinc = getFoodsData.data
            .sort((a, b) => b.zinc - a.zinc)
            .slice(0, 50);
          const sortedFoodsByCopper = getFoodsData.data
            .sort((a, b) => b.copper - a.copper)
            .slice(0, 50);
          const sortedFoodsByIodine = getFoodsData.data
            .sort((a, b) => b.iodine - a.iodine)
            .slice(0, 50);
          const sortedFoodsByManganese = getFoodsData.data
            .sort((a, b) => b.manganese - a.manganese)
            .slice(0, 50);
          const sortedFoodsByIron = getFoodsData.data
            .sort((a, b) => b.iron - a.iron)
            .slice(0, 50);
          const sortedFoodsByFluoride = getFoodsData.data
            .sort((a, b) => b.fluoride - a.fluoride)
            .slice(0, 50);
          const sortedFoodsByMolybdenum = getFoodsData.data
            .sort((a, b) => b.molybdenum - a.molybdenum)
            .slice(0, 50);
          const sortedFoodsBySelenium = getFoodsData.data
            .sort((a, b) => b.selenium - a.selenium)
            .slice(0, 50);
          const sortedFoodsBySaturatedFat = getFoodsData.data
            .sort((a, b) => b.saturatedFat - a.saturatedFat)
            .slice(0, 50);
          const sortedFoodsByProtein = getFoodsData.data
            .sort((a, b) => b.protein - a.protein)
            .slice(0, 50);
          const sortedFoodsByCarbohydrates = getFoodsData.data
            .sort((a, b) => b.carbohydrates - a.carbohydrates)
            .slice(0, 50);
          const sortedFoodsByFat = getFoodsData.data
            .sort((a, b) => b.fat - a.fat)
            .slice(0, 50);
          const sortedFoodsBySugar = getFoodsData.data
            .sort((a, b) => b.sugar - a.sugar)
            .slice(0, 50);
            const sortedFoodsByFiber = getFoodsData.data
            .sort((a, b) => b.fiber - a.fiber)
            .slice(0, 50);
          const sortedFoodsByTransFat = getFoodsData.data
            .sort((a, b) => b.transFat - a.transFat)
            .slice(0, 50);
          const sortedFoodsByMonounsaturatedFat = getFoodsData.data
            .sort((a, b) => b.monounsaturatedFat - a.monounsaturatedFat)
            .slice(0, 50);
          const sortedFoodsByPolyunsaturatedFat = getFoodsData.data
            .sort((a, b) => b.polyunsaturatedFat - a.polyunsaturatedFat)
            .slice(0, 50);
          setSortedFoods({
            sortedFoodsByVitaminA,
            sortedFoodsByVitaminD,
            sortedFoodsByVitaminE,
            sortedFoodsByVitaminB5,
            sortedFoodsByVitaminB1,
            sortedFoodsByVitaminB2,
            sortedFoodsByVitaminB3,
            sortedFoodsByVitaminB6,
            sortedFoodsByVitaminB7,
            sortedFoodsByVitaminB9,
            sortedFoodsByVitaminB12,
            sortedFoodsByVitaminK,
            sortedFoodsByVitaminC,
            sortedFoodsByMagnesium,
            sortedFoodsByPhosphorus,
            sortedFoodsBySodium,
            sortedFoodsByPotassium,
            sortedFoodsByCalcium,
            sortedFoodsByChloride,
            sortedFoodsByChromium,
            sortedFoodsByZinc,
            sortedFoodsByCopper,
            sortedFoodsByIodine,
            sortedFoodsByManganese,
            sortedFoodsByIron,
            sortedFoodsByFluoride,
            sortedFoodsByMolybdenum,
            sortedFoodsBySelenium,
            sortedFoodsBySaturatedFat,
            sortedFoodsByProtein,
            sortedFoodsByCarbohydrates,
            sortedFoodsByFat,
            sortedFoodsBySugar,
            sortedFoodsByTransFat,
            sortedFoodsByMonounsaturatedFat,
            sortedFoodsByPolyunsaturatedFat,
            sortedFoodsByFiber
          });
        }
      } catch (error) {
        console.log(error);
      }
    };

    getFoods();
  }

  if (allFoods.length === 0 || sortedFoods === undefined) {
    return <div id="preloader"></div>;
  }
  return (
    <FoodContext.Provider value={foodInfo}>{children}</FoodContext.Provider>
  );
};

export default FoodContextProvider;
