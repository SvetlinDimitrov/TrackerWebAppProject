import React, { createContext, useState } from "react";

export const NutritionRecordContext = createContext();

const NutritionRecordProvider = ({ children }) => {
  const [record, setData] = useState({
    vitaminData: null,
    vitaminDataPrecented: null,
    vitaminAveragePrecented: null,
    electrolyteData: null,
    electrolyteDataPrecented: null,
    electrolyteAveragePrecented: null,
    macronutrientData: null,
    macronutrientDataPrecented: null,
    macronutrientAveragePrecented: null,
    index: null,
  });

  const setRecord = (rawRecord, index) => {
    const vitaminData = filteredDailyIntake(rawRecord, index, "Vitamin");
    const vitaminDataPrecented = calculatedPrecentedValues(vitaminData);
    const vitaminAveragePrecented = averageValue(vitaminDataPrecented);

    const electrolyteData = filteredDailyIntake(
      rawRecord,
      index,
      "Electrolyte"
    );
    const electrolyteDataPrecented = calculatedPrecentedValues(electrolyteData);
    const electrolyteAveragePrecented = averageValue(electrolyteDataPrecented);

    const macronutrientData = filteredDailyIntake(
      rawRecord,
      index,
      "Macronutrient"
    );
    const macronutrientDataPrecented =
      calculatedPrecentedValues(macronutrientData);
    const macronutrientAveragePrecented = averageValue(
      macronutrientDataPrecented
    );

    setData({
      vitaminData,
      vitaminDataPrecented,
      vitaminAveragePrecented,
      electrolyteData,
      electrolyteDataPrecented,
      electrolyteAveragePrecented,
      macronutrientData,
      macronutrientDataPrecented,
      macronutrientAveragePrecented,
      index,
    });
  };

  const recordInfo = {
    record,
    setRecord,
  };

  return (
    <NutritionRecordContext.Provider value={recordInfo}>
      {children}
    </NutritionRecordContext.Provider>
  );
};
const filteredDailyIntake = (rawRecord, index, type) => {
  return rawRecord[index].dailyIntakeViews.filter(
    (item) => item.nutrientType === type
  );
};

const calculatedPrecentedValues = (filteredDailyIntake) => {
  return filteredDailyIntake.map((item) =>
    (item.dailyConsumed / item.upperBoundIntake) * 100 > 100
      ? {
          name: item.nutrientName,
          precented: 100,
          consumed: item.dailyConsumed,
          max: item.upperBoundIntake,
          type: item.nutrientType,
          measurement: item.measurement
        }
      : {
          name: item.nutrientName,
          precented: (item.dailyConsumed / item.upperBoundIntake) * 100,
          consumed: item.dailyConsumed,
          max: item.upperBoundIntake,
          type: item.nutrientType,
          measurement: item.measurement
        }
  );
};
const averageValue = (calculatedValues) => {
  const precentedValues = calculatedValues.map((item) => item.precented);
  return (
    precentedValues.reduce((sum, value) => sum + value, 0) /
    precentedValues.length
  );
};

export default NutritionRecordProvider;
