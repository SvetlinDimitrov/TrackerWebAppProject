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
  if (type === "Saturated Fat") {
    data = allFoods.map((food) => ({
      data: food.saturatedFat,
      dataNames: food.name,
      typeData: food.saturatedFat,
    }));
  }
  if (type === "Sugar") {
    data = allFoods.map((food) => ({
      data: food.sugar,
      dataNames: food.name,
      typeData: food.sugar,
    }));
  }
  if (type === "Monounsaturated Fat") {
    data = allFoods.map((food) => ({
      data: food.monounsaturatedFat,
      dataNames: food.name,
      typeData: food.monounsaturatedFat,
    }));
  }
  if (type === "Polyunsaturated Fat") {
    data = allFoods.map((food) => ({
      data: food.polyunsaturatedFat,
      dataNames: food.name,
      typeData: food.polyunsaturatedFat,
    }));
  }
  if (type === "Trans Fat") {
    data = allFoods.map((food) => ({
      data: food.transFat,
      dataNames: food.name,
      typeData: food.transFat,
    }));
  }

  if (sort === "AO") {
    data.sort((a, b) => a.data - b.data);
  }
  if (sort === "DO") {
    data.sort((a, b) => b.data - a.data);
  }

  return data;
};

export const featureNutritionDate = (
  allFoods,
  user,
  type,
  sort,
  dataLength
) => {
  let data = {
    immuneFunction: immuneSystem(allFoods, user),
    growthAndDevelopment: growthAndDevelopmentText(allFoods, user),
    cognitiveFunctionAndBrainHealth: brainHealthText(allFoods, user),
    boneHealth: boneHealthText(allFoods, user),
    physicalPerformanceAndFitness: fitnessText(allFoods, user),
    agingAndLongevity: agingText(allFoods, user),
  };
  return data[type];
};

const getPercentageForVitaminA = (nutrient, gender) => {
  if (gender === "MALE") {
    return (nutrient / 900) * 100;
  }
  return (nutrient / 700) * 100;
};

const getPercentageForVitaminD = (nutrient) => {
  return (nutrient / 800) * 100;
};

const getPercentageForVitaminE = (nutrient) => {
  return (nutrient / 15) * 100;
};

const getPercentageForVitaminK = (nutrient, gender) => {
  if (gender === "MALE") {
    return (nutrient / 120) * 100;
  }
  return (nutrient / 90) * 100;
};

const getPercentageForVitaminC = (nutrient, gender) => {
  if (gender === "MALE") {
    return (nutrient / 90) * 100;
  }
  return (nutrient / 75) * 100;
};

const getPercentageForVitaminB6 = (nutrient, gender) => {
  if (gender === "MALE") {
    return (nutrient / 1.7) * 100;
  }
  return (nutrient / 1.5) * 100;
};

const getPercentageForVitaminB9 = (nutrient) => {
  return (nutrient / 400) * 100;
};
const getPercentageForVitaminB12 = (nutrient) => {
  return (nutrient / 2.4) * 100;
};

const getPercentageForCalcium = (nutrient) => {
  return (nutrient / 1300) * 100;
};
const getPercentageForPhosphorus = (nutrient) => {
  return (nutrient / 1250) * 100;
};
const getPercentageForMagnesium = (nutrient, gender) => {
  if (gender === "MALE") {
    return (nutrient / 420) * 100;
  }
  return (nutrient / 320) * 100;
};

const getPercentageForPotassium = (nutrient) => {
  return (nutrient / 3400) * 100;
};

const getPercentageForIron = (nutrient) => {
  return (nutrient / 11) * 100;
};
const getPercentageForZinc = (nutrient, gender) => {
  if (gender === "MALE") {
    return (nutrient / 11) * 100;
  }
  return (nutrient / 8) * 100;
};

const getPercentageForSelenium = (nutrient) => {
  return (nutrient / 400) * 100;
};

const getPercentageForFiber = (nutrient) => {
  return (nutrient / 38) * 100;
};

const immuneSystem = (allFoods, user) => {
  const notSortedData = allFoods.map((food) =>
    getAllPercentageForImmuneSystem(
      food.c,
      food.d,
      food.a,
      food.e,
      food.b6,
      food.b9,
      food.b12,
      food.zinc,
      food.selenium,
      food.iron,
      food.protein,
      food.fat,
      user.gender,
      food.name
    )
  );
  let processedData = notSortedData.map((item) => {
    const average = (
      (item.C + item.D + item.A + item.E,
      item.B6,
      item.B9,
      item.B12,
      item.Zinc,
      item.Selenium,
      item.Iron) / 10
    ).toFixed(2);
    const typeData = `Vitamin C: ${item.C.toFixed(
      2
    )}%\nVitamin D: ${item.D.toFixed(2)}%\nVitamin A: ${item.A.toFixed(
      2
    )}%\nVitamin E: ${item.E.toFixed(2)}%\nVitamin B6: ${item.B6.toFixed(
      2
    )}%\nVitamin B9: ${item.B9.toFixed(2)}%\nVitamin B12: ${item.B12.toFixed(
      2
    )}%\nZinc: ${item.Zinc.toFixed(2)}%\nSelenium : ${item.Selenium.toFixed(
      2
    )}%\nIron: ${item.Iron.toFixed(2)}%\nProtein: ${item.Protein.toFixed(
      2
    )}g\nFat: ${item.Fat.toFixed(2)}g\nTotal %: ${average}`;

    return { data: average, dataNames: item.name, typeData, item };
  });

  processedData = processedData.sort((a, b) => {
    if (b.data === a.data) {
      if (b.item.Protein === a.item.Protein) {
        return b.item.Fat - a.itemFat;
      }
      return b.item.Protein - a.item.Protein;
    }
    return b.data - a.data;
  });

  return {
    text: "To boost your immune system, focus on a diet rich in vitamin C (citrus, peppers), vitamin D (fish, dairy), zinc (meat, nuts), vitamin A (sweet potatoes, spinach), vitamin E (nuts, spinach), protein (meat, beans), iron (red meat, lentils), selenium (Brazil nuts, seafood), folate (leafy greens, citrus), vitamin B6 (poultry, bananas), and omega-3 fatty acids (fatty fish, flaxseeds). Additionally, stay hydrated, get enough sleep, manage stress, and exercise regularly for overall health. Consult a healthcare professional for personalized advice.",
    data: processedData,
  };
};
const growthAndDevelopmentText = (allFoods, user) => {
  const notSortedData = allFoods.map((food) =>
    getAllPercentageForGrowthAndDevelopmentText(
      food.c,
      food.d,
      food.a,
      food.e,
      food.b9,
      food.b12,
      food.zinc,
      food.calcium,
      food.iron,
      food.protein,
      food.fat,
      user.gender,
      food.name
    )
  );
  let processedData = notSortedData.map((i) => {
    const average = (
      (i.C + i.D + i.A + i.E, i.B9, i.B12, i.Zinc, i.Calcium, i.Iron) / 9
    ).toFixed(2);
    const dataNames = i.name;
    const typeData = `Vitamin C: ${i.C.toFixed(2)}%\nVitamin D: ${i.D.toFixed(
      2
    )}%\nVitamin A: ${i.A.toFixed(2)}%\nVitamin E: ${i.E.toFixed(
      2
    )}%\nVitamin B9: ${i.B9.toFixed(2)}%\nVitamin B12: ${i.B12.toFixed(
      2
    )}%\nZinc: ${i.Zinc.toFixed(2)}%\nCalcium : ${i.Calcium.toFixed(
      2
    )}%\nIron: ${i.Iron.toFixed(2)}%\nProtein: ${i.Protein.toFixed(
      2
    )}g\nFat: ${i.Fat.toFixed(2)}g\nTotal %: ${average}`;
    return { ...i, data: average, dataNames, typeData, i };
  });

  processedData = processedData.sort((a, b) => {
    if (b.data === a.data) {
      if (b.Protein === a.Protein) {
        return b.Fat - a.Fat;
      }
      return b.Protein - a.Protein;
    }
    return b.data - a.data;
  });

  return {
    text: "Optimal growth and development require a diet rich in protein (meat, beans), calcium (dairy, leafy greens), iron (red meat, lentils), zinc (meat, nuts), vitamin D (fish, fortified dairy), vitamin A (sweet potatoes, spinach), vitamin C (citrus, peppers), vitamin E (nuts, broccoli), folate (leafy greens, citrus), vitamin B12 (animal products), and omega-3 fatty acids (fatty fish, flaxseeds). Include a variety of nutrient-dense foods for a well-rounded approach. Consider consulting a healthcare professional for personalized guidance.",
    data: processedData,
  };
};
const brainHealthText = (allFoods, user) => {
  const notSortedData = allFoods.map((food) =>
    getAllPercentageForCognitiveFunctionAndBrainHealth(
      food.e,
      food.c,
      food.b6,
      food.b12,
      food.iron,
      food.fat,
      user.gender,
      food.name
    )
  );
  let processedData = notSortedData.map((item) => {
    const average = (
      (item.E + item.C + item.B6 + item.B12, item.Iron) / 5
    ).toFixed(2);
    const typeData = `Vitamin C: ${item.C.toFixed(
      2
    )}%\nVitamin E: ${item.E.toFixed(2)}%\nVitamin B6: ${item.B6.toFixed(
      2
    )}%\nVitamin B12: ${item.B12.toFixed(2)}%\nIron: ${item.Iron.toFixed(
      2
    )}%\nFat: ${item.Fat.toFixed(2)}g\nTotal %: ${average}`;

    return { data: average, dataNames: item.name, typeData, item };
  });

  processedData = processedData.sort((a, b) => {
    if (b.data === a.data) {
      return b.item.Fat - a.item.Fat;
    }
    return b.data - a.data;
  });

  return {
    text: "To optimize cognitive function and brain health, prioritize nutrients like omega-3 fatty acids (fatty fish, walnuts), antioxidants such as vitamin E (nuts, spinach), and vitamin C (citrus fruits, berries). Additionally, focus on B-vitamins, including B6 (poultry, bananas) and B12 (fish, meat), which support nerve function. Include sources of choline (eggs, broccoli) for neurotransmitter production and ensure an adequate intake of iron (red meat, beans) for oxygen transport to the brain. Lastly, maintain hydration and consider consulting a healthcare professional for personalized advice on a brain-boosting diet.",
    data: processedData,
  };
};
const boneHealthText = (allFoods, user) => {
  const notSortedData = allFoods.map((food) =>
    getAllPercentageForBoneHealth(
      food.d,
      food.k,
      food.calcium,
      food.phosphorus,
      food.magnesium,
      food.protein,
      user.gender,
      food.name
    )
  );
  let processedData = notSortedData.map((item) => {
    const average = (
      (item.D + item.K + item.Phosphorus + item.Magnesium + item.Calcium) /
      5
    ).toFixed(2);
    const typeData = `Vitamin D: ${item.D.toFixed(
      2
    )}%\nVitamin K: ${item.K.toFixed(2)}%\nCalcium: ${item.Calcium.toFixed(
      2
    )}%\nPhosphorus: ${item.Phosphorus.toFixed(
      2
    )}%\nMagnesium: ${item.Magnesium.toFixed(
      2
    )}%\nProtein: ${item.Protein.toFixed(2)}g\nTotal %: ${average}`;

    return { data: average, dataNames: item.name, typeData, item };
  });

  processedData = processedData.sort((a, b) => {
    if (b.data === a.data) {
      return b.item.Protein - a.item.Protein;
    }
    return b.data - a.data;
  });

  return {
    text: "For bone health, prioritize calcium-rich foods like dairy and leafy greens, along with vitamin D sources such as fatty fish and fortified dairy to enhance calcium absorption. Include foods high in phosphorus like meat and dairy for bone structure. Vitamin K from leafy greens aids in bone mineralization. Magnesium, found in nuts and whole grains, supports bone density. Protein (meat, beans) is essential for bone maintenance. Ensure a well-balanced diet, and consider consulting a healthcare professional for personalized guidance on maintaining strong and healthy bones.",
    data: processedData,
  };
};
const fitnessText = (allFoods, user) => {
  const notSortedData = allFoods.map((food) =>
    getAllPercentageForPhysicalPerformance(
      food.b6,
      food.b12,
      food.iron,
      food.potassium,
      food.protein,
      food.fat,
      food.carbohydrates,
      user.gender,
      food.name
    )
  );
  let processedData = notSortedData.map((item) => {
    const average = (
      (item.B6 + item.B12 + item.Potassium + item.Iron) /
      4
    ).toFixed(2);
    const typeData = `Vitamin B6: ${item.B6.toFixed(
      2
    )}%\nVitamin B12: ${item.B12.toFixed(2)}%\nIron: ${item.Iron.toFixed(
      2
    )}%\nPotassium: ${item.Potassium.toFixed(
      2
    )}%\nProtein: ${item.Protein.toFixed(
      2
    )}g\nCarbohydrates: ${item.Carbohydrates.toFixed(
      2
    )}g\nFat: ${item.Fat.toFixed(2)}g\nTotal %: ${average}`;

    return { data: average, dataNames: item.name, typeData, item };
  });

  processedData = processedData.sort((a, b) => {
    if (b.data === a.data) {
      if (b.item.Protein === a.item.Protein) {
        if (b.item.Fat === a.item.Fat) {
          return b.item.Carbohydrates - a.item.Carbohydrates;
        }
        return b.item.Fat - a.item.Fat;
      }
      return b.item.Protein - a.item.Protein;
    }
    return b.data - a.data;
  });

  return {
    text: "To enhance physical performance and fitness, focus on protein-rich foods like meat and beans for muscle repair. Carbohydrates from whole grains and fruits provide energy for workouts. Stay hydrated with water and electrolytes. Include iron-rich foods like red meat to support oxygen transport during exercise. Omega-3 fatty acids from fatty fish and nuts reduce inflammation. Vitamins like B6 (poultry, bananas) and B12 (fish, meat) aid in energy metabolism. Potassium-rich foods like bananas and oranges help prevent muscle cramps. Maintain a balanced diet, consider individual needs, and consult fitness or healthcare professionals for personalized advice.",
    data: processedData,
  };
};
const agingText = (allFoods, user) => {
  const notSortedData = allFoods.map((food) =>
    getAllPercentageForAging(
      food.c,
      food.e,
      food.d,
      food.fiber,
      food.protein,
      user.gender,
      food.name
    )
  );
  let processedData = notSortedData.map((item) => {
    const average = ((item.C + item.D + item.E + item.Fiber) / 4).toFixed(2);
    const typeData = `Vitamin C: ${item.C.toFixed(
      2
    )}%\nVitamin E: ${item.E.toFixed(2)}%\nVitamin D: ${item.D.toFixed(
      2
    )}%\nFiber: ${item.Fiber.toFixed(2)}%\nProtein: ${item.Protein.toFixed(
      2
    )}g\nTotal %: ${average}`;

    return { data: average, dataNames: item.name, typeData, item };
  });

  processedData = processedData.sort((a, b) => {
    if (b.data === a.data) {
      return b.item.Protein - a.item.Protein;
    }
    return b.data - a.data;
  });

  return {
    text: "For aging and longevity, prioritize nutrients that support overall health. Antioxidants like vitamin C (citrus fruits, berries) and vitamin E (nuts, spinach) combat oxidative stress. Omega-3 fatty acids from fatty fish and flaxseeds promote heart health and reduce inflammation. Vitamin D (fatty fish, fortified dairy) aids in bone health and immune function. Ensure an adequate intake of protein (meat, beans) for muscle maintenance. Include foods rich in fiber (whole grains, fruits) for digestive health. Stay hydrated and consider incorporating anti-inflammatory spices like turmeric. Consult with healthcare professionals for personalized advice on nutrition and lifestyle choices for aging well.",
    data: processedData,
  };
};
const getAllPercentageForImmuneSystem = (
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
const getAllPercentageForGrowthAndDevelopmentText = (
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
const getAllPercentageForCognitiveFunctionAndBrainHealth = (
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
const getAllPercentageForBoneHealth = (
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
const getAllPercentageForPhysicalPerformance = (
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
const getAllPercentageForAging = (C, E, D, Fiber, Protein, Gender, name) => {
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
