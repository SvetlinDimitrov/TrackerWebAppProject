import carbohydrate from "./info/macros/Carbohydrate.js";
import fiber from "./info/macros/Fiber.js";
import cholesterol from "./info/macros/Cholesterol.js";
import fat from "./info/macros/Fat.js";
import omega3 from "./info/macros/Omega3.js";
import omega6 from "./info/macros/Omega6.js";
import protein from "./info/macros/Protein.js";
import saturatedFat from "./info/macros/Saturated Fat.js";
import sugar from "./info/macros/Sugar.js";
import transFat from "./info/macros/Trans Fat.js";
import water from "./info/macros/Water.js";
import calcium from "./info/minerals/Calcium.js";
import chloride from "./info/minerals/Chloride.js";
import chromium from "./info/minerals/Chromium.js";
import copper from "./info/minerals/Copper.js";
import fluoride from "./info/minerals/Fluoride.js";
import iodine from "./info/minerals/Iodine.js";
import iron from "./info/minerals/Iron.js";
import magnesium from "./info/minerals/Magnesium.js";
import manganese from "./info/minerals/Manganese.js";
import molybdenum from "./info/minerals/Molybdenum.js";
import phosphorus from "./info/minerals/Phosphorus.js";
import potassium from "./info/minerals/Potassium.js";
import selenium from "./info/minerals/Selenium.js";
import sodium from "./info/minerals/Sodium.js";
import zinc from "./info/minerals/Zinc.js";
import choline from "./info/vitamins/Choline.js";
import vitaminA from "./info/vitamins/VitaminA.js";
import vitaminB1 from "./info/vitamins/VitaminB1.js";
import vitaminB2 from "./info/vitamins/vitaminB2.js";
import vitaminB3 from "./info/vitamins/VitaminB3.js";
import vitaminB5 from "./info/vitamins/VitaminB5.js";
import vitaminB6 from "./info/vitamins/VitaminB6.js";
import vitaminB7 from "./info/vitamins/VitaminB7.js";
import vitaminB9 from "./info/vitamins/VitaminB9.js";
import vitaminB12 from "./info/vitamins/VitaminB12.js";
import vitaminC from "./info/vitamins/VitaminC.js";
import vitaminD from "./info/vitamins/VitaminD.js";
import vitaminE from "./info/vitamins/VitaminE.js";
import vitaminK from "./info/vitamins/VitaminK.js";

const nutritionUnits = {
    "Carbohydrate": carbohydrate,
    "Protein": protein,
    "Fat": fat,
    "Fiber": fiber,
    "Sugar": sugar,
    "Linoleic Acid": omega6,
    "α-Linolenic Acid": omega3,
    "Cholesterol": cholesterol,
    "Water": water,
    "Saturated Fat": saturatedFat,
    "Trans Fat": transFat,
    "Vitamin A": vitaminA,
    "Vitamin D (D2 + D3)": vitaminD,
    "Vitamin E": vitaminE,
    "Vitamin K": vitaminK,
    "Vitamin C": vitaminC,
    "Vitamin B1 (Thiamin)": vitaminB1,
    "Vitamin B2 (Riboflavin)": vitaminB2,
    "Vitamin B3 (Niacin)": vitaminB3,
    "Vitamin B5 (Pantothenic acid)": vitaminB5,
    "Vitamin B6": vitaminB6,
    "Vitamin B7 (Biotin)": vitaminB7,
    "Vitamin B9 (Folate)": vitaminB9,
    "Vitamin B12": vitaminB12,
    "Choline": choline,
    "Calcium , Ca": calcium,
    "Chromium , Cr": chromium,
    "Phosphorus , P": phosphorus,
    "Fluoride": fluoride,
    "Chloride": chloride,
    "Magnesium , Mg": magnesium,
    "Sodium , Na": sodium,
    "Potassium , K": potassium,
    "Iron , Fe": iron,
    "Zinc , Zn": zinc,
    "Copper , Cu": copper,
    "Manganese , Mn": manganese,
    "Iodine , I": iodine,
    "Selenium , Se": selenium,
    "Molybdenum , Mo": molybdenum
};

export { nutritionUnits };