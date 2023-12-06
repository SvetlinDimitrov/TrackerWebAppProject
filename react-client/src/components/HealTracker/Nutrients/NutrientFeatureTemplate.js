import { useContext, useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { FoodContext } from "../../../context/FoodContext";
import { AuthContext } from "../../../context/UserAuth";
import * as nutrientCalculations from "../../../util/NutrientCalculator";
import { BarChart2 } from "../../../util/Tools";
import styles from "./NutrientFeatureTemplate.module.css";

const NutrientFeatureTemplate = () => {
  const { user } = useContext(AuthContext);
  const { allFoods } = useContext(FoodContext);
  const { featureType } = useParams();
  const [data, setData] = useState();

  useEffect(() => {
    if (featureType === "immuneFunction") {
      setData(immuneSystem(allFoods, user));
    } else if (featureType === "growthAndDevelopment") {
      setData(growthAndDevelopmentText(allFoods, user));
    } else if (featureType === "cognitiveFunctionAndBrainHealth") {
      setData(brainHealthText(allFoods, user));
    } else if (featureType === "boneHealth") {
      setData(boneHealthText(allFoods, user));
    } else if (featureType === "physicalPerformanceAndFitness") {
      setData(fitnessText(allFoods, user));
    } else if (featureType === "agingAndLongevity") {
      setData(agingText(allFoods, user));
    }
  }, [allFoods, user, featureType]);

  if (data === undefined) {
    return <div id="preloader"></div>;
  }
  return (
    <>
      <div className={styles.container}>
        <div className={styles.body_container_barChar}>
          <p>{data.text}</p>
          <div className={styles.BarChart2_container}>
            <BarChart2 info={data.data} height={500} dataLength={30} />
          </div>
        </div>
      </div>
    </>
  );
};

export default NutrientFeatureTemplate;

const immuneSystem = (allFoods, user) => {
  const notSortedData = allFoods.map((food) =>
    nutrientCalculations.getAllPercentageForImmuneSystem(
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
    nutrientCalculations.getAllPercentageForGrowthAndDevelopmentText(
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
    return { ...i, data: average, dataNames, typeData, typeData };
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
    nutrientCalculations.getAllPercentageForCognitiveFunctionAndBrainHealth(
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
    nutrientCalculations.getAllPercentageForBoneHealth(
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
    const average = ((item.D + item.K + item.Phosphorus + item.Magnesium + item.Calcium) / 5).toFixed(2);
    const typeData = `Vitamin D: ${item.D.toFixed(
      2
    )}%\nVitamin K: ${item.K.toFixed(2)}%\nCalcium: ${item.Calcium.toFixed(
      2
    )}%\nPhosphorus: ${item.Phosphorus.toFixed(2)}%\nMagnesium: ${item.Magnesium.toFixed(
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
    nutrientCalculations.getAllPercentageForPhysicalPerformance(
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
    const average = ((item.B6 + item.B12 + item.Potassium + item.Iron) / 4).toFixed(2);
    const typeData = `Vitamin B6: ${item.B6.toFixed(
      2
    )}%\nVitamin B12: ${item.B12.toFixed(2)}%\nIron: ${item.Iron.toFixed(
      2
    )}%\nPotassium: ${item.Potassium.toFixed(2)}%\nProtein: ${item.Protein.toFixed(
      2
    )}g\nCarbohydrates: ${item.Carbohydrates.toFixed(2)}g\nFat: ${item.Fat.toFixed(
      2
    )}g\nTotal %: ${average}`;

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
    nutrientCalculations.getAllPercentageForAging(
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