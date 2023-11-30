export const basicPath = () => {
  return "/health-tracker";
};

export const recordPath = (recordId) => {
  return "/health-tracker/record/" + recordId;
};

export const storagePath = (recordId, storageId) => {
  return "/health-tracker/record/" + recordId + "/storage/" + storageId;
};

export const basicFoodPath = (
  recordId,
  storageId,
  foodName,
  foodSize,
  fromFoodMenu
) => {
  return (
    "/health-tracker/record/" +
    recordId +
    "/storage/" +
    storageId +
    "/foodItem/" +
    foodName +
    "?foodSize=" +
    foodSize +
    "&fromFoodMenu=" +
    fromFoodMenu
  );
};
