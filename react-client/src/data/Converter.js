const fs = require("fs");
const { chain } = require("stream-chain");
const { parser } = require("stream-json");
const { streamArray } = require("stream-json/streamers/StreamArray");

const sourceFiles = [
  "./foodSources/foundationDownload.json",
  "./foodSources/FoodData_Central_survey_food_json_2022-10-28.json",
  // "./foodSources/brandedDownload.json",
];
const outputFiles = [
  "./foodData/foundationFoods.json",
  "./foodData/surveyFoods.json",
  // "./foodData/brandedFoods.json",
];

// const brandedDownload = require();

function convertToNewFormat(data) {
  // Create a new object with the desired format
  const findAndModifyNutrient = (data, name, unit, amount) => {
    const dataToModify = data.find((nutrient) => nutrient.name === name);
    dataToModify.unit = unit;
    dataToModify.amount = amount;
  };
  let newFormat = {
    foodClass: data.foodClass,
    description: data.description,
    calories: {
      name: "Energy",
      amount: 0.0,
      unit: "",
    },
    measurement: "grams",
    size: 100,
    vitaminNutrients: [
      {
        name: "Vitamin A",
        amount: 0.0,
        unit: "µg",
      },
      {
        name: "Vitamin D (D2 + D3)",
        amount: 0.0,
        unit: "µg",
      },
      {
        name: "Vitamin E",
        amount: 0.0,
        unit: "mg",
      },
      {
        name: "Vitamin K",
        amount: 0.0,
        unit: "µg",
      },
      {
        name: "Vitamin C",
        amount: 0.0,
        unit: "mg",
      },
      {
        name: "Vitamin B1 (Thiamin)",
        amount: 0.0,
        unit: "mg",
      },
      {
        name: "Vitamin B2 (Riboflavin)",
        amount: 0.0,
        unit: "mg",
      },
      {
        name: "Vitamin B3 (Niacin)",
        amount: 0.0,
        unit: "mg",
      },
      {
        name: "Vitamin B5 (Pantothenic acid)",
        amount: 0.0,
        unit: "mg",
      },
      {
        name: "Vitamin B6",
        amount: 0.0,
        unit: "mg",
      },
      {
        name: "Vitamin B7 (Biotin)",
        amount: 0.0,
        unit: "µg",
      },
      {
        name: "Vitamin B9 (Folate)",
        amount: 0.0,
        unit: "µg",
      },
      {
        name: "Vitamin B12",
        amount: 0.0,
        unit: "µg",
      },
    ],
    mineralNutrients: [
      {
        name: "Calcium , Ca",
        amount: 0.0,
        unit: "mg",
      },
      {
        name: "Phosphorus , P",
        amount: 0.0,
        unit: "mg",
      },
      {
        name: "Magnesium , Mg",
        amount: 0.0,
        unit: "mg",
      },
      {
        name: "Sodium , Na",
        amount: 0.0,
        unit: "mg",
      },
      {
        name: "Potassium , K",
        amount: 0.0,
        unit: "mg",
      },
      {
        name: "Iron , Fe",
        amount: 0.0,
        unit: "mg",
      },
      {
        name: "Zinc , Zn",
        amount: 0.0,
        unit: "mg",
      },
      {
        name: "Copper , Cu",
        amount: 0.0,
        unit: "mg",
      },
      {
        name: "Manganese , Mn",
        amount: 0.0,
        unit: "mg",
      },
      {
        name: "Iodine , I",
        amount: 0.0,
        unit: "µg",
      },
      {
        name: "Selenium , Se",
        amount: 0.0,
        unit: "µg",
      },
      {
        name: "Molybdenum , Mo",
        amount: 0.0,
        unit: "µg",
      },
    ],
    macronutrients: [
      {
        name: "Carbohydrates",
        amount: 0.0,
        unit: "g",
      },
      {
        name: "Protein",
        amount: 0.0,
        unit: "g",
      },
      {
        name: "Fat",
        amount: 0.0,
        unit: "g",
      },
      {
        name: "Fiber",
        amount: 0.0,
        unit: "g",
      },
      {
        name: "Trans Fat",
        amount: 0.0,
        unit: "g",
      },
      {
        name: "Saturated Fat",
        amount: 0.0,
        unit: "g",
      },
      {
        name: "Sugar",
        amount: 0.0,
        unit: "g",
      },
      {
        name: "Polyunsaturated Fat",
        amount: 0.0,
        unit: "g",
      },
      {
        name: "Monounsaturated Fat",
        amount: 0.0,
        unit: "g",
      },
    ],
    foodPortions: [],
    foodGroups: undefined,
    brandedInfo: undefined,
  };
  // Setting food groups
  if (data.foodClass === "Branded") {
    newFormat.foodGroups = data.brandedFoodCategory;
  } else if (data.foodClass === "FinalFood") {
    newFormat.foodGroups = data.foodCategory.description;
  }

  // Iterate over the foodNutrients array
  for (let nutrient of data.foodNutrients) {
    if (nutrient.nutrient.name.includes("Energy")) {
      if (nutrient.nutrient.unitName === "kcal") {
        newFormat.calories.unit = "kcal";
        newFormat.calories.amount = nutrient.amount;
      } else if (nutrient.nutrient.unitName === "kJ") {
        newFormat.calories.unit = "kcal";
        newFormat.calories.amount = nutrient.amount * 0.239006;
      }
    } else if (
      nutrient.nutrient.name === "Vitamin A, RAE" &&
      nutrient.nutrient.unitName === "µg"
    ) {
      findAndModifyNutrient(
        newFormat.vitaminNutrients,
        "Vitamin A",
        "µg",
        nutrient.amount
      );
    } else if (
      nutrient.nutrient.name === "Vitamin D (D2 + D3)" &&
      nutrient.nutrient.unitName === "µg"
    ) {
      findAndModifyNutrient(
        newFormat.vitaminNutrients,
        "Vitamin D (D2 + D3)",
        "µg",
        nutrient.amount
      );
    } else if (
      nutrient.nutrient.name === "Vitamin E (alpha-tocopherol)" &&
      nutrient.nutrient.unitName === "mg"
    ) {
      findAndModifyNutrient(
        newFormat.vitaminNutrients,
        "Vitamin E",
        "mg",
        nutrient.amount
      );
    } else if (
      nutrient.nutrient.name === "Vitamin K (phylloquinone)" &&
      nutrient.nutrient.unitName === "µg"
    ) {
      findAndModifyNutrient(
        newFormat.vitaminNutrients,
        "Vitamin K",
        "µg",
        nutrient.amount
      );
    } else if (
      nutrient.nutrient.name === "Vitamin C, total ascorbic acid" &&
      nutrient.nutrient.unitName === "mg"
    ) {
      findAndModifyNutrient(
        newFormat.vitaminNutrients,
        "Vitamin C",
        "mg",
        nutrient.amount
      );
    } else if (
      nutrient.nutrient.name === "Thiamin" &&
      nutrient.nutrient.unitName === "mg"
    ) {
      findAndModifyNutrient(
        newFormat.vitaminNutrients,
        "Vitamin B1 (Thiamin)",
        "mg",
        nutrient.amount
      );
    } else if (
      nutrient.nutrient.name === "Riboflavin" &&
      nutrient.nutrient.unitName === "mg"
    ) {
      findAndModifyNutrient(
        newFormat.vitaminNutrients,
        "Vitamin B2 (Riboflavin)",
        "mg",
        nutrient.amount
      );
    } else if (
      nutrient.nutrient.name === "Niacin" &&
      nutrient.nutrient.unitName === "mg"
    ) {
      findAndModifyNutrient(
        newFormat.vitaminNutrients,
        "Vitamin B3 (Niacin)",
        "mg",
        nutrient.amount
      );
    } else if (
      nutrient.nutrient.name === "Pantothenic acid" &&
      nutrient.nutrient.unitName === "mg"
    ) {
      findAndModifyNutrient(
        newFormat.vitaminNutrients,
        "Vitamin B5 (Pantothenic acid)",
        "mg",
        nutrient.amount
      );
    } else if (
      nutrient.nutrient.name === "Vitamin B-6" &&
      nutrient.nutrient.unitName === "mg"
    ) {
      findAndModifyNutrient(
        newFormat.vitaminNutrients,
        "Vitamin B6",
        "mg",
        nutrient.amount
      );
    } else if (
      nutrient.nutrient.name === "Biotin" &&
      nutrient.nutrient.unitName === "µg"
    ) {
      findAndModifyNutrient(
        newFormat.vitaminNutrients,
        "Vitamin B7 (Biotin)",
        "µg",
        nutrient.amount
      );
    } else if (
      nutrient.nutrient.name === "Folate, total" &&
      nutrient.nutrient.unitName === "µg"
    ) {
      findAndModifyNutrient(
        newFormat.vitaminNutrients,
        "Vitamin B9 (Folate)",
        "µg",
        nutrient.amount
      );
    } else if (
      nutrient.nutrient.name === "Vitamin B-12" &&
      nutrient.nutrient.unitName === "µg"
    ) {
      findAndModifyNutrient(
        newFormat.vitaminNutrients,
        "Vitamin B12",
        "µg",
        nutrient.amount
      );
    } else if (
      nutrient.nutrient.name === "Calcium, Ca" &&
      nutrient.nutrient.unitName === "mg"
    ) {
      findAndModifyNutrient(
        newFormat.mineralNutrients,
        "Calcium , Ca",
        "mg",
        nutrient.amount
      );
    } else if (
      nutrient.nutrient.name === "Phosphorus, P" &&
      nutrient.nutrient.unitName === "mg"
    ) {
      findAndModifyNutrient(
        newFormat.mineralNutrients,
        "Phosphorus , P",
        "mg",
        nutrient.amount
      );
    } else if (
      nutrient.nutrient.name === "Magnesium, Mg" &&
      nutrient.nutrient.unitName === "mg"
    ) {
      findAndModifyNutrient(
        newFormat.mineralNutrients,
        "Magnesium , Mg",
        "mg",
        nutrient.amount
      );
    } else if (
      nutrient.nutrient.name === "Sodium, Na" &&
      nutrient.nutrient.unitName === "mg"
    ) {
      findAndModifyNutrient(
        newFormat.mineralNutrients,
        "Sodium , Na",
        "mg",
        nutrient.amount
      );
    } else if (
      nutrient.nutrient.name === "Potassium, K" &&
      nutrient.nutrient.unitName === "mg"
    ) {
      findAndModifyNutrient(
        newFormat.mineralNutrients,
        "Potassium , K",
        "mg",
        nutrient.amount
      );
    } else if (
      nutrient.nutrient.name === "Iron, Fe" &&
      nutrient.nutrient.unitName === "mg"
    ) {
      findAndModifyNutrient(
        newFormat.mineralNutrients,
        "Iron , Fe",
        "mg",
        nutrient.amount
      );
    } else if (
      nutrient.nutrient.name === "Zinc, Zn" &&
      nutrient.nutrient.unitName === "mg"
    ) {
      findAndModifyNutrient(
        newFormat.mineralNutrients,
        "Zinc , Zn",
        "mg",
        nutrient.amount
      );
    } else if (
      nutrient.nutrient.name === "Copper, Cu" &&
      nutrient.nutrient.unitName === "mg"
    ) {
      findAndModifyNutrient(
        newFormat.mineralNutrients,
        "Copper , Cu",
        "mg",
        nutrient.amount
      );
    } else if (
      nutrient.nutrient.name === "Manganese, Mn" &&
      nutrient.nutrient.unitName === "mg"
    ) {
      findAndModifyNutrient(
        newFormat.mineralNutrients,
        "Manganese , Mn",
        "mg",
        nutrient.amount
      );
    } else if (
      nutrient.nutrient.name === "Iodine, I" &&
      nutrient.nutrient.unitName === "µg"
    ) {
      findAndModifyNutrient(
        newFormat.mineralNutrients,
        "Iodine , I",
        "µg",
        nutrient.amount
      );
    } else if (
      nutrient.nutrient.name === "Selenium, Se" &&
      nutrient.nutrient.unitName === "µg"
    ) {
      findAndModifyNutrient(
        newFormat.mineralNutrients,
        "Selenium , Se",
        "µg",
        nutrient.amount
      );
    } else if (
      nutrient.nutrient.name === "Molybdenum, Mo" &&
      nutrient.nutrient.unitName === "µg"
    ) {
      findAndModifyNutrient(
        newFormat.mineralNutrients,
        "Molybdenum , Mo",
        "µg",
        nutrient.amount
      );
    } else if (
      nutrient.nutrient.name === "Carbohydrate, by difference" &&
      nutrient.nutrient.unitName === "g"
    ) {
      findAndModifyNutrient(
        newFormat.macronutrients,
        "Carbohydrates",
        "g",
        nutrient.amount
      );
    } else if (
      nutrient.nutrient.name === "Protein" &&
      nutrient.nutrient.unitName === "g"
    ) {
      findAndModifyNutrient(
        newFormat.macronutrients,
        "Protein",
        "g",
        nutrient.amount
      );
    } else if (
      nutrient.nutrient.name === "Total lipid (fat)" &&
      nutrient.nutrient.unitName === "g"
    ) {
      findAndModifyNutrient(
        newFormat.macronutrients,
        "Fat",
        "g",
        nutrient.amount
      );
    } else if (
      nutrient.nutrient.name === "Fiber, total dietary" &&
      nutrient.nutrient.unitName === "g"
    ) {
      findAndModifyNutrient(
        newFormat.macronutrients,
        "Fiber",
        "g",
        nutrient.amount
      );
    } else if (
      nutrient.nutrient.name === "Fatty acids, total trans" &&
      nutrient.nutrient.unitName === "g"
    ) {
      findAndModifyNutrient(
        newFormat.macronutrients,
        "Trans Fat",
        "g",
        nutrient.amount
      );
    } else if (
      nutrient.nutrient.name === "Fatty acids, total saturated" &&
      nutrient.nutrient.unitName === "g"
    ) {
      findAndModifyNutrient(
        newFormat.macronutrients,
        "Saturated Fat",
        "g",
        nutrient.amount
      );
    } else if (
      nutrient.nutrient.name === "Sugars, Total" &&
      nutrient.nutrient.unitName === "g"
    ) {
      findAndModifyNutrient(
        newFormat.macronutrients,
        "Sugar",
        "g",
        nutrient.amount
      );
    } else if (
      nutrient.nutrient.name === "Fatty acids, total polyunsaturated" &&
      nutrient.nutrient.unitName === "g"
    ) {
      findAndModifyNutrient(
        newFormat.macronutrients,
        "Polyunsaturated Fat",
        "g",
        nutrient.amount
      );
    } else if (
      nutrient.nutrient.name === "Fatty acids, total monounsaturated" &&
      nutrient.nutrient.unitName === "g"
    ) {
      findAndModifyNutrient(
        newFormat.macronutrients,
        "Monounsaturated Fat",
        "g",
        nutrient.amount
      );
    }
  }

  if (data.foodPortions) {
    if (data.foodClass === "FinalFood") {
      for (let portion of data.foodPortions) {
        newFormat.foodPortions.push({
          name: portion.measureUnit.name,
          modifier: portion.modifier,
          gramWeight: portion.gramWeight,
          amount: portion.amount,
        });
      }
    }
    if (data.foodClass === "Survey") {
      for (let portion of data.foodPortions) {
        newFormat.foodPortions.push({
          name: portion.portionDescription,
          modifier: undefined,
          gramWeight: portion.gramWeight,
          amount: undefined,
        });
      }
    }
  }

  if (data.foodClass === "Branded") {
    brandedInfo = {
      ingredients: data.ingredients,
      brandOwner: data.brandOwner,
      marketCountry: data.marketCountry,
      servingSize: data.servingSize,
      servingSizeUnit: data.servingSizeUnit,
    };
  }
  if (newFormat.calories.amount !== 0 && newFormat.calories.name === "Energy") {
    return newFormat;
  }
}

sourceFiles.forEach((sourceFile, index) => {
  const outputFile = outputFiles[index];
  const outputFileStream = fs.createWriteStream(outputFile);

  // Write the opening square bracket to the file
  outputFileStream.write("[");

  let isFirstObject = true;

  const pipeline = chain([
    fs.createReadStream(sourceFile),
    parser(),
    streamArray(),
    (data) => {
      const value = data.value;
      const newValue = convertToNewFormat(value);
      return newValue;
    },
    (data) => {
      let jsonText = JSON.stringify(data);
      if (!isFirstObject) {
        jsonText = "," + jsonText; // Add a comma before each JSON object, except the first one
      }
      isFirstObject = false;
      return jsonText + "\n"; // Add a newline character after each JSON object
    },
    outputFileStream,
  ]);

  pipeline.on("error", console.error);

  // When the pipeline has finished, write the closing square bracket to the file
  pipeline.on("finish", () => {
    outputFileStream.end("]");
  });
});
