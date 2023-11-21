export const calculatedPrecentedValues = (rawData, type) => {
  return rawData.dailyIntakeViews
    .filter((item) => item.nutrientType === type)
    .map((item) =>
      (item.dailyConsumed / item.upperBoundIntake) * 100 > 100
        ? {
            name: item.nutrientName,
            precented: 100,
            consumed: item.dailyConsumed,
            max: item.upperBoundIntake,
            type: item.nutrientType,
            measurement: item.measurement,
          }
        : {
            name: item.nutrientName,
            precented: (item.dailyConsumed / item.upperBoundIntake) * 100,
            consumed: item.dailyConsumed,
            max: item.upperBoundIntake,
            type: item.nutrientType,
            measurement: item.measurement,
          }
    );
};

export const calcAverageValue = (rawData, type) => {
  const precentedValues = rawData.dailyIntakeViews
    .filter((item) => item.nutrientType === type)
    .map((item) =>
      (item.dailyConsumed / item.upperBoundIntake) * 100 > 100
        ? 100
        : (item.dailyConsumed / item.upperBoundIntake) * 100
    );

  return (
    precentedValues.reduce((sum, value) => sum + value, 0) /
    precentedValues.length
  );
};

export const getRecordById = (allRecords, id) => {
  return allRecords.find((record) => record.id === id);
};

export const getStorageById = (record, storageId) => {
  return record.storageViews.find((storage) => {
    return storage.id === +storageId;
  });
};

export const getTotalFoodCaloriesFromStorage = (storage) => {
  return storage.foods.reduce((total, food) => total + food.calories, 0);
};
