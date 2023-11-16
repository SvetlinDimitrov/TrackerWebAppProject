import React, { createContext, useState } from "react";

export const NutritionRecordContext = createContext();

const NutritionRecordProvider = ({ children }) => {
  const [allRecords, setAllRecord] = useState();

  const recordInfo = {
    allRecords,
    setAllRecord,
  };

  return (
    <NutritionRecordContext.Provider value={recordInfo}>
      {children}
    </NutritionRecordContext.Provider>
  );
};

export default NutritionRecordProvider;
