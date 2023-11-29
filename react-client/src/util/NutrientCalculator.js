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
    C: getPercentageForVitaminC(C, Gender),
    D: getPercentageForVitaminD(D, Gender),
    A: getPercentageForVitaminA(A, Gender),
    E: getPercentageForVitaminE(E, Gender),
    B6: getPercentageForVitaminB6(B6, Gender),
    B9: getPercentageForVitaminB9(B9, Gender),
    B12: getPercentageForVitaminB12(B12, Gender),
    Zinc: getPercentageForZinc(Zinc, Gender),
    Selenium: getPercentageForSelenium(Selenium, Gender),
    Iron: getPercentageForIron(Iron),
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
    C: getPercentageForVitaminC(C, Gender),
    D: getPercentageForVitaminD(D, Gender),
    A: getPercentageForVitaminA(A, Gender),
    E: getPercentageForVitaminE(E, Gender),
    B9: getPercentageForVitaminB9(B9, Gender),
    B12: getPercentageForVitaminB12(B12, Gender),
    Zinc: getPercentageForZinc(Zinc, Gender),
    Calcium: getPercentageForCalcium(Calcium, Gender),
    Iron: getPercentageForIron(Iron),
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
    E: getPercentageForVitaminE(E, Gender),
    C: getPercentageForVitaminC(C, Gender),
    B6: getPercentageForVitaminB6(B6, Gender),
    B12: getPercentageForVitaminB12(B12, Gender),
    Iron: getPercentageForIron(Iron, Gender),
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
    D: getPercentageForVitaminD(D, Gender),
    K: getPercentageForVitaminK(K, Gender),
    Calcium: getPercentageForCalcium(Calcium, Gender),
    Phosphorus: getPercentageForPhosphorus(Phosphorus, Gender),
    Magnesium: getPercentageForMagnesium(Magnesium, Gender),
    Protein,
    name
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
    B6: getPercentageForVitaminB6(B6, Gender),
    B12: getPercentageForVitaminB12(B12, Gender),
    Iron: getPercentageForIron(Iron, Gender),
    Potassium: getPercentageForPotassium(Potassium, Gender),
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
    C: getPercentageForVitaminC(C, Gender),
    E: getPercentageForVitaminB12(E, Gender),
    D: getPercentageForVitaminD(D, Gender),
    Fiber: getPercentageForFiber(Fiber, Gender),
    Protein,
    name,
  };
};
