export const getPercentageForVitaminA = (nutrient, gender) => {
  if (gender === "MALE") {
    return (nutrient / 900) * 100;
  }
  return (nutrient / 700) * 100;
};

export const getPercentageForVitaminD = (nutrient) => {
  return (nutrient / 800) * 100;
};

export const getPercentageForVitaminE = (nutrient) => {
  return (nutrient / 15) * 100;
};

export const getPercentageForVitaminK = (nutrient, gender) => {
  if (gender === "MALE") {
    return (nutrient / 120) * 100;
  }
  return (nutrient / 90) * 100;
};

export const getPercentageForVitaminC = (nutrient, gender) => {
  if (gender === "MALE") {
    return (nutrient / 90) * 100;
  }
  return (nutrient / 75) * 100;
};
export const getPercentageForVitaminB1 = (nutrient, gender) => {
  if (gender === "MALE") {
    return (nutrient / 1.2) * 100;
  }
  return (nutrient / 1.1) * 100;
};
export const getPercentageForVitaminB2 = (nutrient, gender) => {
  if (gender === "MALE") {
    return (nutrient / 1.3) * 100;
  }
  return (nutrient / 1.1) * 100;
};
export const getPercentageForVitaminB3 = (nutrient, gender) => {
  if (gender === "MALE") {
    return (nutrient / 16) * 100;
  }
  return (nutrient / 14) * 100;
};
export const getPercentageForVitaminB5 = (nutrient) => {
  return (nutrient / 5) * 100;
};
export const getPercentageForVitaminB6 = (nutrient, gender) => {
  if (gender === "MALE") {
    return (nutrient / 1.7) * 100;
  }
  return (nutrient / 1.5) * 100;
};
export const getPercentageForVitaminB7 = (nutrient) => {
  return (nutrient / 30) * 100;
};
export const getPercentageForVitaminB9 = (nutrient) => {
  return (nutrient / 400) * 100;
};
export const getPercentageForVitaminB12 = (nutrient) => {
  return (nutrient / 2.4) * 100;
};

export const getPercentageForCalcium = (nutrient) => {
  return (nutrient / 1300) * 100;
};
export const getPercentageForPhosphorus = (nutrient) => {
  return (nutrient / 1250) * 100;
};
export const getPercentageForMagnesium = (nutrient, gender) => {
  if (gender === "MALE") {
    return (nutrient / 420) * 100;
  }
  return (nutrient / 320) * 100;
};
export const getPercentageForSodium = (nutrient) => {
  return (nutrient / 2300) * 100;
};
export const getPercentageForPotassium = (nutrient) => {
  return (nutrient / 3400) * 100;
};

export const getPercentageForChloride = (nutrient) => {
  return (nutrient / 3600) * 100;
};

export const getPercentageForIron = (nutrient) => {
  return (nutrient / 11) * 100;
};
export const getPercentageForZinc = (nutrient, gender) => {
  if (gender === "MALE") {
    return (nutrient / 11) * 100;
  }
  return (nutrient / 8) * 100;
};
export const getPercentageForCopper = (nutrient) => {
  return (nutrient / 2.3) * 100;
};
export const getPercentageForManganese = (nutrient) => {
  return (nutrient / 11) * 100;
};

export const getPercentageForIodine = (nutrient) => {
  return (nutrient / 290) * 100;
};
export const getPercentageForSelenium = (nutrient) => {
  return (nutrient / 400) * 100;
};
export const getPercentageForFluoride = (nutrient) => {
  return (nutrient / 4) * 100;
};
export const getPercentageForChromium = (nutrient, gender) => {
  if (gender === "MALE") {
    return (nutrient / 35) * 100;
  }
  return (nutrient / 25) * 100;
};
export const getPercentageForMolybdenum = (nutrient) => {
  return (nutrient / 2000) * 100;
};
export const getPercentageForFiber = (nutrient) => {
  return (nutrient / 38) * 100;
};

export const getAllPercentageForImmuneSystem = (
  C,
  D,
  A,
  E,
  B6,
  B9,
  B12,
  Zinc,
  Selenium,
  Iron,
  Protein,
  Fat,
  Gender,
  name
) => {
  return {
    C:
      getPercentageForVitaminC(C, Gender) > 100
        ? 100
        : getPercentageForVitaminC(C, Gender),
    D:
      getPercentageForVitaminD(D, Gender) > 100
        ? 100
        : getPercentageForVitaminD(D, Gender),
    A:
      getPercentageForVitaminA(A, Gender) > 100
        ? 100
        : getPercentageForVitaminA(A, Gender),
    E:
      getPercentageForVitaminE(E, Gender) > 100
        ? 100
        : getPercentageForVitaminE(E, Gender),
    B6:
      getPercentageForVitaminB6(B6, Gender) > 100
        ? 100
        : getPercentageForVitaminB6(B6, Gender),
    B9:
      getPercentageForVitaminB9(B9, Gender) > 100
        ? 100
        : getPercentageForVitaminB9(B9, Gender),
    B12:
      getPercentageForVitaminB12(B12, Gender) > 100
        ? 100
        : getPercentageForVitaminB12(B12, Gender),
    Zinc:
      getPercentageForZinc(Zinc, Gender) > 100
        ? 100
        : getPercentageForZinc(Zinc, Gender),
    Selenium:
      getPercentageForSelenium(Selenium, Gender) > 100
        ? 100
        : getPercentageForSelenium(Selenium, Gender),
    Iron: getPercentageForIron(Iron) > 100 ? 100 : getPercentageForIron(Iron),
    Protein,
    Fat,
    name,
  };
};
export const getAllPercentageForGrowthAndDevelopmentText = (
  C,
  D,
  A,
  E,
  B9,
  B12,
  Zinc,
  Calcium,
  Iron,
  Protein,
  Fat,
  Gender,
  name
) => {
  return {
    C:
      getPercentageForVitaminC(C, Gender) > 100
        ? 100
        : getPercentageForVitaminC(C, Gender),
    D:
      getPercentageForVitaminD(D, Gender) > 100
        ? 100
        : getPercentageForVitaminD(D, Gender),
    A:
      getPercentageForVitaminA(A, Gender) > 100
        ? 100
        : getPercentageForVitaminA(A, Gender),
    E:
      getPercentageForVitaminE(E, Gender) > 100
        ? 100
        : getPercentageForVitaminE(E, Gender),
    B9:
      getPercentageForVitaminB9(B9, Gender) > 100
        ? 100
        : getPercentageForVitaminB9(B9, Gender),
    B12:
      getPercentageForVitaminB12(B12, Gender) > 100
        ? 100
        : getPercentageForVitaminB12(B12, Gender),
    Zinc:
      getPercentageForZinc(Zinc, Gender) > 100
        ? 100
        : getPercentageForZinc(Zinc, Gender),
    Calcium:
      getPercentageForCalcium(Calcium, Gender) > 100
        ? 100
        : getPercentageForCalcium(Calcium, Gender),
    Iron: getPercentageForIron(Iron) > 100 ? 100 : getPercentageForIron(Iron),
    Protein,
    Fat,
    name,
  };
};
export const getAllPercentageForCognitiveFunctionAndBrainHealth = (
  E,
  C,
  B6,
  B12,
  Iron,
  Fat,
  Gender,
  name
) => {
  return {
    E:
      getPercentageForVitaminE(E, Gender) > 100
        ? 100
        : getPercentageForVitaminE(E, Gender),
    C:
      getPercentageForVitaminC(C, Gender) > 100
        ? 100
        : getPercentageForVitaminC(C, Gender),
    B6:
      getPercentageForVitaminB6(B6, Gender) > 100
        ? 100
        : getPercentageForVitaminB6(B6, Gender),
    B12:
      getPercentageForVitaminB12(B12, Gender) > 100
        ? 100
        : getPercentageForVitaminB12(B12, Gender),
    Iron:
      getPercentageForIron(Iron, Gender) > 100
        ? 100
        : getPercentageForIron(Iron, Gender),
    Fat,
    name,
  };
};
export const getAllPercentageForBoneHealth = (
  D,
  K,
  Calcium,
  Phosphorus,
  Magnesium,
  Protein,
  Gender,
  name
) => {
  return {
    D:
      getPercentageForVitaminD(D, Gender) > 100
        ? 100
        : getPercentageForVitaminD(D, Gender),
    K:
      getPercentageForVitaminK(K, Gender) > 100
        ? 100
        : getPercentageForVitaminK(K, Gender),
    Calcium:
      getPercentageForCalcium(Calcium, Gender) > 100
        ? 100
        : getPercentageForCalcium(Calcium, Gender),
    Phosphorus:
      getPercentageForPhosphorus(Phosphorus, Gender) > 100
        ? 100
        : getPercentageForPhosphorus(Phosphorus, Gender),
    Magnesium:
      getPercentageForMagnesium(Magnesium, Gender) > 100
        ? 100
        : getPercentageForMagnesium(Magnesium, Gender),

    Protein,
    name,
  };
};
export const getAllPercentageForPhysicalPerformance = (
  B6,
  B12,
  Iron,
  Potassium,
  Protein,
  Fat,
  Carbohydrates,
  Gender,
  name
) => {
  return {
    B6:
      getPercentageForVitaminB6(B6, Gender) > 100
        ? 100
        : getPercentageForVitaminB6(B6, Gender),
    B12:
      getPercentageForVitaminB12(B12, Gender) > 100
        ? 100
        : getPercentageForVitaminB12(B12, Gender),
    Iron:
      getPercentageForIron(Iron, Gender) > 100
        ? 100
        : getPercentageForIron(Iron, Gender),
    Potassium:
      getPercentageForPotassium(Potassium, Gender) > 100
        ? 100
        : getPercentageForPotassium(Potassium, Gender),
    Protein,
    Fat,
    Carbohydrates,
    name,
  };
};
export const getAllPercentageForAging = (
  C,
  E,
  D,
  Fiber,
  Protein,
  Gender,
  name
) => {
  return {
    C:
      getPercentageForVitaminC(C, Gender) > 100
        ? 100
        : getPercentageForVitaminC(C, Gender),
    E:
      getPercentageForVitaminB12(E, Gender) > 100
        ? 100
        : getPercentageForVitaminB12(E, Gender),
    D:
      getPercentageForVitaminD(D, Gender) > 100
        ? 100
        : getPercentageForVitaminD(D, Gender),
    Fiber:
      getPercentageForFiber(Fiber, Gender) > 100
        ? 100
        : getPercentageForFiber(Fiber, Gender),
    Protein,
    name,
  };
};

export const sortedFoodsByNutrientType = (allFoods, type, sort, dataLength) => {
  let data;
  if (type === "A") {
    data = allFoods.map((food) => ({
      data: food.a,
      dataNames: food.name,
      typeData: food.a,
    }));
  }
  if (type === "B") {
    data = allFoods.map((food) => ({
      data: food.b,
      dataNames: food.name,
      typeData: food.b,
    }));
  }
  if (type === "C") {
    data = allFoods.map((food) => ({
      data: food.c,
      dataNames: food.name,
      typeData: food.c,
    }));
  }
  if (type === "D") {
    data = allFoods.map((food) => ({
      data: food.d,
      dataNames: food.name,
      typeData: food.d,
    }));
  }
  if (type === "E") {
    data = allFoods.map((food) => ({
      data: food.e,
      dataNames: food.name,
      typeData: food.e,
    }));
  }
  if (type === "K") {
    data = allFoods.map((food) => ({
      data: food.k,
      dataNames: food.name,
      typeData: food.k,
    }));
  }
  if (type === "B1") {
    data = allFoods.map((food) => ({
      data: food.b1,
      dataNames: food.name,
      typeData: food.b1,
    }));
  }
  if (type === "B2") {
    data = allFoods.map((food) => ({
      data: food.b2,
      dataNames: food.name,
      typeData: food.b2,
    }));
  }
  if (type === "B3") {
    data = allFoods.map((food) => ({
      data: food.b3,
      dataNames: food.name,
      typeData: food.b3,
    }));
  }
  if (type === "B5") {
    data = allFoods.map((food) => ({
      data: food.b5,
      dataNames: food.name,
      typeData: food.b5,
    }));
  }
  if (type === "B6") {
    data = allFoods.map((food) => ({
      data: food.b6,
      dataNames: food.name,
      typeData: food.b6,
    }));
  }
  if (type === "B7") {
    data = allFoods.map((food) => ({
      data: food.b7,
      dataNames: food.name,
      typeData: food.b7,
    }));
  }
  if (type === "B9") {
    data = allFoods.map((food) => ({
      data: food.b9,
      dataNames: food.name,
      typeData: food.b9,
    }));
  }
  if (type === "B12") {
    data = allFoods.map((food) => ({
      data: food.b12,
      dataNames: food.name,
      typeData: food.b12,
    }));
  }
  if (type === "Calcium") {
    data = allFoods.map((food) => ({
      data: food.calcium,
      dataNames: food.name,
      typeData: food.calcium,
    }));
  }
  if (type === "Phosphorus") {
    data = allFoods.map((food) => ({
      data: food.phosphorus,
      dataNames: food.name,
      typeData: food.phosphorus,
    }));
  }
  if (type === "Magnesium") {
    data = allFoods.map((food) => ({
      data: food.magnesium,
      dataNames: food.name,
      typeData: food.magnesium,
    }));
  }
  if (type === "Sodium") {
    data = allFoods.map((food) => ({
      data: food.sodium,
      dataNames: food.name,
      typeData: food.sodium,
    }));
  }
  if (type === "Potassium") {
    data = allFoods.map((food) => ({
      data: food.potassium,
      dataNames: food.name,
      typeData: food.potassium,
    }));
  }
  if (type === "Chloride") {
    data = allFoods.map((food) => ({
      data: food.chloride,
      dataNames: food.name,
      typeData: food.chloride,
    }));
  }
  if (type === "Iron") {
    data = allFoods.map((food) => ({
      data: food.iron,
      dataNames: food.name,
      typeData: food.iron,
    }));
  }
  if (type === "Zinc") {
    data = allFoods.map((food) => ({
      data: food.zinc,
      dataNames: food.name,
      typeData: food.zinc,
    }));
  }
  if (type === "Copper") {
    data = allFoods.map((food) => ({
      data: food.copper,
      dataNames: food.name,
      typeData: food.copper,
    }));
  }
  if (type === "Manganese") {
    data = allFoods.map((food) => ({
      data: food.manganese,
      dataNames: food.name,
      typeData: food.manganese,
    }));
  }
  if (type === "Iodine") {
    data = allFoods.map((food) => ({
      data: food.iodine,
      dataNames: food.name,
      typeData: food.iodine,
    }));
  }
  if (type === "Selenium") {
    data = allFoods.map((food) => ({
      data: food.selenium,
      dataNames: food.name,
      typeData: food.selenium,
    }));
  }
  if (type === "Fluoride") {
    data = allFoods.map((food) => ({
      data: food.fluoride,
      dataNames: food.name,
      typeData: food.fluoride,
    }));
  }
  if (type === "Chromium") {
    data = allFoods.map((food) => ({
      data: food.chromium,
      dataNames: food.name,
      typeData: food.chromium,
    }));
  }
  if (type === "Molybdenum") {
    data = allFoods.map((food) => ({
      data: food.molybdenum,
      dataNames: food.name,
      typeData: food.molybdenum,
    }));
  }
  if (type === "Fiber") {
    data = allFoods.map((food) => ({
      data: food.fiber,
      dataNames: food.name,
      typeData: food.fiber,
    }));
  }
  if (type === "Protein") {
    data = allFoods.map((food) => ({
      data: food.protein,
      dataNames: food.name,
      typeData: food.protein,
    }));
  }
  if (type === "Fat") {
    data = allFoods.map((food) => ({
      data: food.fat,
      dataNames: food.name,
      typeData: food.fat,
    }));
  }
  if (type === "Carbohydrates") {
    data = allFoods.map((food) => ({
      data: food.carbohydrates,
      dataNames: food.name,
      typeData: food.carbohydrates,
    }));
  }
  if (type === "Saturated Fat"){
    data = allFoods.map((food) => ({
      data: food.saturatedFat,
      dataNames: food.name,
      typeData: food.saturatedFat,
    }));
  }
  if (type === "Sugar"){
    data = allFoods.map((food) => ({
      data: food.sugar,
      dataNames: food.name,
      typeData: food.sugar,
    }));
  }
  if(type === "Monounsaturated Fat"){
    data = allFoods.map((food) => ({
      data: food.monounsaturatedFat,
      dataNames: food.name,
      typeData: food.monounsaturatedFat,
    }));
  }
  if(type === "Polyunsaturated Fat"){
    data = allFoods.map((food) => ({
      data: food.polyunsaturatedFat,
      dataNames: food.name,
      typeData: food.polyunsaturatedFat,
    }));
  }
  if(type === "Trans Fat"){
    data = allFoods.map((food) => ({
      data: food.transFat,
      dataNames: food.name,
      typeData: food.transFat,
    }));
  }

  if(sort === "AO"){
    data.sort((a, b) => a.data - b.data);
  }
  if(sort === "DO"){
    data.sort((a, b) => b.data - a.data);
  }
  
  return data;
};
