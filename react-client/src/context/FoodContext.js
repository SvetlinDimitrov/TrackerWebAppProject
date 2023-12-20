import React, { createContext, useState, useEffect } from "react";
export const FoodContext = createContext();

const FoodContextProvider = ({ children }) => {
  const [nutrient, setNutrient] = useState({});
  const [allFoods, setAllFoods] = useState();

  useEffect(() => {
    Promise.all([
      fetch("http://localhost:3001/data/foundationFoods.json").then(
        (response) => response.json()
      ),
      fetch("http://localhost:3001/data/surveyFoods.json").then((response) =>
        response.json()
      ),
    ])
      .then(([foundationFoods, surveyFoods]) => {
        setAllFoods([
          {
            type: "FinalFood",
            groups: new Set(foundationFoods.map((food) => food.foodGroups)),
            data: foundationFoods,
          },
          { type: "Survey", groups: undefined, data: surveyFoods },
        ]);
      })
      .catch((error) => console.error("Error fetching data:", error));
  }, []);

  const convertComplexFoodIntoSimpleFood = (food) => {
    return {
      id: food.id,
      name: food.description,
      calories: food.calories.amount,
      measurement: "grams",
      size: 100,
      a: food.vitaminNutrients.find((vitamin) => vitamin.name === "Vitamin A")
        .amount,
      d: food.vitaminNutrients.find(
        (vitamin) => vitamin.name === "Vitamin D (D2 + D3)"
      ).amount,
      e: food.vitaminNutrients.find((vitamin) => vitamin.name === "Vitamin E")
        .amount,
      k: food.vitaminNutrients.find((vitamin) => vitamin.name === "Vitamin K")
        .amount,
      c: food.vitaminNutrients.find((vitamin) => vitamin.name === "Vitamin C")
        .amount,
      b1: food.vitaminNutrients.find(
        (vitamin) => vitamin.name === "Vitamin B1 (Thiamin)"
      ).amount,
      b2: food.vitaminNutrients.find(
        (vitamin) => vitamin.name === "Vitamin B2 (Riboflavin)"
      ).amount,
      b3: food.vitaminNutrients.find(
        (vitamin) => vitamin.name === "Vitamin B3 (Niacin)"
      ).amount,
      b5: food.vitaminNutrients.find(
        (vitamin) => vitamin.name === "Vitamin B5 (Pantothenic acid)"
      ).amount,
      b6: food.vitaminNutrients.find((vitamin) => vitamin.name === "Vitamin B6")
        .amount,
      b7: food.vitaminNutrients.find(
        (vitamin) => vitamin.name === "Vitamin B7 (Biotin)"
      ).amount,
      b9: food.vitaminNutrients.find(
        (vitamin) => vitamin.name === "Vitamin B9 (Folate)"
      ).amount,
      b12: food.vitaminNutrients.find(
        (vitamin) => vitamin.name === "Vitamin B12"
      ).amount,
      calcium: food.mineralNutrients.find(
        (mineral) => mineral.name === "Calcium , Ca"
      ).amount,
      phosphorus: food.mineralNutrients.find(
        (mineral) => mineral.name === "Phosphorus , P"
      ).amount,
      magnesium: food.mineralNutrients.find(
        (mineral) => mineral.name === "Magnesium , Mg"
      ).amount,
      sodium: food.mineralNutrients.find(
        (mineral) => mineral.name === "Sodium , Na"
      ).amount,
      potassium: food.mineralNutrients.find(
        (mineral) => mineral.name === "Potassium , K"
      ).amount,
      chloride: 0.0,
      iron: food.mineralNutrients.find(
        (mineral) => mineral.name === "Iron , Fe"
      ).amount,
      zinc: food.mineralNutrients.find(
        (mineral) => mineral.name === "Zinc , Zn"
      ).amount,
      copper: food.mineralNutrients.find(
        (mineral) => mineral.name === "Copper , Cu"
      ).amount,
      manganese: food.mineralNutrients.find(
        (mineral) => mineral.name === "Manganese , Mn"
      ).amount,
      iodine: food.mineralNutrients.find(
        (mineral) => mineral.name === "Iodine , I"
      ).amount,
      selenium: food.mineralNutrients.find(
        (mineral) => mineral.name === "Selenium , Se"
      ).amount,
      fluoride: 0.0,
      chromium: 0.0,
      molybdenum: food.mineralNutrients.find(
        (mineral) => mineral.name === "Molybdenum , Mo"
      ).amount,
      carbohydrates: food.macronutrients.find(
        (macronutrient) => macronutrient.name === "Carbohydrates"
      ).amount,
      protein: food.macronutrients.find(
        (macronutrient) => macronutrient.name === "Protein"
      ).amount,
      fat: food.macronutrients.find(
        (macronutrient) => macronutrient.name === "Fat"
      ).amount,
      fiber: food.macronutrients.find(
        (macronutrient) => macronutrient.name === "Fiber"
      ).amount,
      transFat: food.macronutrients.find(
        (macronutrient) => macronutrient.name === "Trans Fat"
      ).amount,
      saturatedFat: food.macronutrients.find(
        (macronutrient) => macronutrient.name === "Saturated Fat"
      ).amount,
      sugar: food.macronutrients.find(
        (macronutrient) => macronutrient.name === "Sugar"
      ).amount,
      polyunsaturatedFat: food.macronutrients.find(
        (macronutrient) => macronutrient.name === "Polyunsaturated Fat"
      ).amount,
      monounsaturatedFat: food.macronutrients.find(
        (macronutrient) => macronutrient.name === "Monounsaturated Fat"
      ).amount,
    };
  };
  const convertSimpleFoodIntroComplexFood = (food) => {
    return {
      id: food.id,
      foodClass: undefined,
      description: food.name,
      measurement: "grams",
      size: food.size,
      calories: {
        name: "Energy",
        amount: food.calories,
        unit: "kcal",
      },
      vitaminNutrients: [
        {
          name: "Vitamin A",
          amount: food.a,
          unit: "mcg",
        },
        {
          name: "Vitamin D (D2 + D3)",
          amount: food.d,
          unit: "mcg",
        },
        {
          name: "Vitamin E",
          amount: food.e,
          unit: "mg",
        },
        {
          name: "Vitamin K",
          amount: food.k,
          unit: "mcg",
        },
        {
          name: "Vitamin C",
          amount: food.c,
          unit: "mg",
        },
        {
          name: "Vitamin B1 (Thiamin)",
          amount: food.b1,
          unit: "mg",
        },
        {
          name: "Vitamin B2 (Riboflavin)",
          amount: food.b2,
          unit: "mg",
        },
        {
          name: "Vitamin B3 (Niacin)",
          amount: food.b3,
          unit: "mg",
        },
        {
          name: "Vitamin B5 (Pantothenic acid)",
          amount: food.b5,
          unit: "mg",
        },
        {
          name: "Vitamin B6",
          amount: food.b6,
          unit: "mg",
        },
        {
          name: "Vitamin B7 (Biotin)",
          amount: food.b7,
          unit: "mcg",
        },
        {
          name: "Vitamin B9 (Folate)",
          amount: food.b9,
          unit: "mcg",
        },
        {
          name: "Vitamin B12",
          amount: food.b12,
          unit: "mcg",
        },
      ],
      mineralNutrients: [
        {
          name: "Calcium , Ca",
          amount: food.calcium,
          unit: "mg",
        },
        {
          name: "Phosphorus , P",
          amount: food.phosphorus,
          unit: "mg",
        },
        {
          name: "Magnesium , Mg",
          amount: food.magnesium,
          unit: "mg",
        },
        {
          name: "Sodium , Na",
          amount: food.sodium,
          unit: "mg",
        },
        {
          name: "Potassium , K",
          amount: food.potassium,
          unit: "mg",
        },
        {
          name: "Iron , Fe",
          amount: food.iron,
          unit: "mg",
        },
        {
          name: "Zinc , Zn",
          amount: food.zinc,
          unit: "mg",
        },
        {
          name: "Copper , Cu",
          amount: food.copper,
          unit: "mg",
        },
        {
          name: "Manganese , Mn",
          amount: food.manganese,
          unit: "mg",
        },
        {
          name: "Iodine , I",
          amount: food.iodine,
          unit: "mcg",
        },
        {
          name: "Selenium , Se",
          amount: food.selenium,
          unit: "mcg",
        },
        {
          name: "Molybdenum , Mo",
          amount: food.molybdenum,
          unit: "mcg",
        },
      ],
      macronutrients: [
        {
          name: "Carbohydrates",
          amount: food.carbohydrates,
          unit: "g",
        },
        {
          name: "Protein",
          amount: food.protein,
          unit: "g",
        },
        {
          name: "Fat",
          amount: food.fat,
          unit: "g",
        },
        {
          name: "Fiber",
          amount: food.fiber,
          unit: "g",
        },
        {
          name: "Trans Fat",
          amount: food.transFat,
          unit: "g",
        },
        {
          name: "Saturated Fat",
          amount: food.saturatedFat,
          unit: "g",
        },
        {
          name: "Sugar",
          amount: food.sugar,
          unit: "g",
        },
        {
          name: "Polyunsaturated Fat",
          amount: food.polyunsaturatedFat,
          unit: "g",
        },
        {
          name: "Monounsaturated Fat",
          amount: food.monounsaturatedFat,
          unit: "g",
        },
      ],
      foodPortions: [],
      foodGroups: undefined,
      brandedInfo: undefined,
    };
  };

  if (!allFoods) {
    return <div id="preloader"></div>;
  }

  return (
    <FoodContext.Provider
      value={{
        allFoods,
        nutrient,
        setNutrient,
        convertComplexFoodIntoSimpleFood,
        convertSimpleFoodIntroComplexFood,
      }}
    >
      {children}
    </FoodContext.Provider>
  );
};

export default FoodContextProvider;
