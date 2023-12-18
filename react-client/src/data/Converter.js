const data = require("./foodSources/foundationDownload.json");
const fs = require("fs");

function convertToNewFormat(data) {
  // Create a new object with the desired format

  let newFormat = {
    name: data.description,
    calories: 0,
    measurement: "grams",
    size: 100,
    a: 0.0,
    d: 0.0,
    e: 0.0,
    k: 0.0,
    c: 0.0,
    b1: 0.0,
    b2: 0.0,
    b3: 0.0,
    b5: 0.0,
    b6: 0.0,
    b7: 0.0,
    b9: 0.0,
    b12: 0.0,
    calcium: 0.0,
    phosphorus: 0.0,
    magnesium: 0.0,
    sodium: 0.0,
    potassium: 0.0,
    iron: 0.0,
    zinc: 0.0,
    copper: 0.0,
    manganese: 0.0,
    iodine: 0.0,
    selenium: 0.0,
    molybdenum: 0.0,
    carbohydrates: 0.0,
    protein: 0.0,
    fat: 0.0,
    fiber: 0.0,
    transFat: 0.0,
    saturatedFat: 0.0,
    sugar: 0.0,
    polyunsaturatedFat: 0.0,
    monounsaturatedFat: 0.0,
  };

  // Iterate over the foodNutrients array
  for (let nutrient of data.foodNutrients) {
    // Check if the nutrient name includes the desired string
    if (
      nutrient.nutrient.name === "Energy" &&
      nutrient.nutrient.unitName === "kcal"
    ) {
      newFormat.calories = nutrient.amount;
    } else if (
      nutrient.nutrient.name === "Vitamin A, RAE" &&
      nutrient.nutrient.unitName === "µg"
    ) {
      newFormat.a = nutrient.amount;
    } else if (
      nutrient.nutrient.name === "Vitamin D (D2 + D3)" &&
      nutrient.nutrient.unitName === "µg"
    ) {
      newFormat.d = nutrient.amount;
    } else if (
      nutrient.nutrient.name === "Vitamin E (alpha-tocopherol)" &&
      nutrient.nutrient.unitName === "mg"
    ) {
      newFormat.e = nutrient.amount;
    } else if (
      nutrient.nutrient.name === "Vitamin K (phylloquinone)" &&
      nutrient.nutrient.unitName === "µg"
    ) {
      newFormat.k = nutrient.amount;
    } else if (
      nutrient.nutrient.name === "Vitamin C, total ascorbic acid" &&
      nutrient.nutrient.unitName === "mg"
    ) {
      newFormat.c = nutrient.amount;
    } else if (
      nutrient.nutrient.name === "Thiamin" &&
      nutrient.nutrient.unitName === "mg"
    ) {
      newFormat.b1 = nutrient.amount;
    } else if (
      nutrient.nutrient.name === "Riboflavin" &&
      nutrient.nutrient.unitName === "mg"
    ) {
      newFormat.b2 = nutrient.amount;
    } else if (
      nutrient.nutrient.name === "Niacin" &&
      nutrient.nutrient.unitName === "mg"
    ) {
      newFormat.b3 = nutrient.amount;
    } else if (
      nutrient.nutrient.name === "Pantothenic acid" &&
      nutrient.nutrient.unitName === "mg"
    ) {
      newFormat.b5 = nutrient.amount;
    } else if (
      nutrient.nutrient.name === "Vitamin B-6" &&
      nutrient.nutrient.unitName === "mg"
    ) {
      newFormat.b6 = nutrient.amount;
    } else if (
      nutrient.nutrient.name === "Biotin" &&
      nutrient.nutrient.unitName === "µg"
    ) {
      newFormat.b7 = nutrient.amount;
    } else if (
      nutrient.nutrient.name === "Folate, total" &&
      nutrient.nutrient.unitName === "µg"
    ) {
      newFormat.b9 = nutrient.amount;
    } else if (
      nutrient.nutrient.name === "Vitamin B-12" &&
      nutrient.nutrient.unitName === "µg"
    ) {
      newFormat.b12 = nutrient.amount;
    } else if (
      nutrient.nutrient.name === "Calcium, Ca" &&
      nutrient.nutrient.unitName === "mg"
    ) {
      newFormat.calcium = nutrient.amount;
    } else if (
      nutrient.nutrient.name === "Phosphorus, P" &&
      nutrient.nutrient.unitName === "mg"
    ) {
      newFormat.phosphorus = nutrient.amount;
    } else if (
      nutrient.nutrient.name === "Magnesium, Mg" &&
      nutrient.nutrient.unitName === "mg"
    ) {
      newFormat.magnesium = nutrient.amount;
    } else if (
      nutrient.nutrient.name === "Sodium, Na" &&
      nutrient.nutrient.unitName === "mg"
    ) {
      newFormat.sodium = nutrient.amount;
    } else if (
      nutrient.nutrient.name === "Potassium, K" &&
      nutrient.nutrient.unitName === "mg"
    ) {
      newFormat.potassium = nutrient.amount;
    } else if (
      nutrient.nutrient.name === "Iron, Fe" &&
      nutrient.nutrient.unitName === "mg"
    ) {
      newFormat.iron = nutrient.amount;
    } else if (
      nutrient.nutrient.name === "Zinc, Zn" &&
      nutrient.nutrient.unitName === "mg"
    ) {
      newFormat.zinc = nutrient.amount;
    } else if (
      nutrient.nutrient.name === "Copper, Cu" &&
      nutrient.nutrient.unitName === "mg"
    ) {
      newFormat.copper = nutrient.amount;
    } else if (
      nutrient.nutrient.name === "Manganese, Mn" &&
      nutrient.nutrient.unitName === "mg"
    ) {
      newFormat.manganese = nutrient.amount;
    } else if (
      nutrient.nutrient.name === "Iodine, I" &&
      nutrient.nutrient.unitName === "µg"
    ) {
      newFormat.iodine = nutrient.amount;
    } else if (
      nutrient.nutrient.name === "Selenium, Se" &&
      nutrient.nutrient.unitName === "µg"
    ) {
      newFormat.selenium = nutrient.amount;
    } else if (
      nutrient.nutrient.name === "Molybdenum, Mo" &&
      nutrient.nutrient.unitName === "µg"
    ) {
      newFormat.molybdenum = nutrient.amount;
    } else if (
      nutrient.nutrient.name === "Carbohydrate, by difference" &&
      nutrient.nutrient.unitName === "g"
    ) {
      newFormat.carbohydrates = nutrient.amount;
    } else if (
      nutrient.nutrient.name === "Protein" &&
      nutrient.nutrient.unitName === "g"
    ) {
      newFormat.protein = nutrient.amount;
    } else if (
      nutrient.nutrient.name === "Total lipid (fat)" &&
      nutrient.nutrient.unitName === "g"
    ) {
      newFormat.fat = nutrient.amount;
    } else if (
      nutrient.nutrient.name === "Fiber, total dietary" &&
      nutrient.nutrient.unitName === "g"
    ) {
      newFormat.fiber = nutrient.amount;
    } else if (
      nutrient.nutrient.name === "Fatty acids, total trans" &&
      nutrient.nutrient.unitName === "g"
    ) {
      newFormat.transFat = nutrient.amount;
    } else if (
      nutrient.nutrient.name === "Fatty acids, total saturated" &&
      nutrient.nutrient.unitName === "g"
    ) {
      newFormat.saturatedFat = nutrient.amount;
    } else if (
      nutrient.nutrient.name === "Sugars, Total" &&
      nutrient.nutrient.unitName === "g"
    ) {
      newFormat.sugar = nutrient.amount;
    } else if (
      nutrient.nutrient.name === "Fatty acids, total polyunsaturated" &&
      nutrient.nutrient.unitName === "g"
    ) {
      newFormat.polyunsaturatedFat = nutrient.amount;
    } else if (
      nutrient.nutrient.name === "Fatty acids, total monounsaturated" &&
      nutrient.nutrient.unitName === "g"
    ) {
      newFormat.monounsaturatedFat = nutrient.amount;
    }
  }

  return newFormat;
}
const newArrayData = [];

for (let i = 0; i < data.FoundationFoods.length; i++) {
    newArrayData.push(convertToNewFormat(data.FoundationFoods[i]));
  }

fs.writeFile("foods.json", JSON.stringify(newArrayData, null, 2), (err) => {
  if (err) throw err;
  console.log("Data written to file");
});
