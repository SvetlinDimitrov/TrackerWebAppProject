import React, { createContext, useState } from "react";

export const RecordContext = createContext();

const RecordContextProvider = ({ children }) => {

  const [allRecords, setAllRecord] = useState(() => {
    const records = localStorage.getItem('records');

    if(records !== null && records !== undefined){
      return JSON.parse(records);
    }

    return undefined;
  });

  const setRecords = (records) =>{

    setAllRecord(records);
    localStorage.setItem('records' , JSON.stringify(records));

  }

  const clearRecords = () => {
    setAllRecord(undefined);
    localStorage.removeItem('records');
  }

  const recordInfo = {
    allRecords,
    setRecords,
    clearRecords,
  };

  return (
    <RecordContext.Provider value={recordInfo}>
      {children}
    </RecordContext.Provider>
  );
};

export default RecordContextProvider;
