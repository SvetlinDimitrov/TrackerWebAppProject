import { faMinus, faPlus } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { useContext, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

import { NutritionRecordContext } from "../../../context/NutritionRecordContext";
import { NotificationContext } from "../../../context/Notification";
import { AuthContext } from "../../../context/UserAuth";
import { calculatedPrecentedValues } from "../../../util/RecordUtils";
import api from "../../../util/api";
import styles from "./NutritionRecord.module.css";
import { Gauge, PipeChar } from "./Tools";

const NutritionRecordTemplate = () => {
  const { userToken } = useContext(AuthContext);
  const { setFailedMessage, setSuccessfulMessage } = useContext(NotificationContext);
  const { allRecords , setAllRecord } = useContext(NutritionRecordContext);
  const navigate = useNavigate();

  const [recordIndex, setRecordIndex] = useState(-1);
  const [show, setShow] = useState("");
  const [categoryShow, setCategoryShow] = useState("");

  const types = ["Vitamin", "Electrolyte", "Macronutrient", "Calories"];
  const categories = ["Vitamin", "Electrolyte", "Macronutrient"];

  useEffect(() => {
    const fetchAllRecords = async () => {
      try {
        const response = await api.get("/record/all", {
          headers: { Authorization: `Bearer ${userToken}` },
        });
        setAllRecord(response.data);
      } catch (error) {
        setFailedMessage({
          message:
            "You need to be fully registered. Please go to settings and finish your register!",
          flag: true,
        });
        navigate("/");
      }
    };

    fetchAllRecords();
  }, [navigate, userToken, setFailedMessage]);

  const selectRecordIndex = (index) => {
    setRecordIndex(+index);
  };
  const handleOnClick = (name) => {
    setShow((prevShow) => (prevShow === name ? "" : name));
  };

  const recordCreationHandle = async (e) => {
    e.preventDefault();

    const name = e.target.name.value;

    if (name.length < 2) {
      setFailedMessage({
        message: "Name must be at least 2 character long!",
        flag: true,
      });
      navigate("/");
      return;
    }
    try {
      await api.post(
        `/record?name=${encodeURIComponent(name)}`,
        {},
        { headers: { Authorization: `Bearer ${userToken}` } }
      );
      const response = await api.get("/record/all", {
        headers: { Authorization: `Bearer ${userToken}` },
      });
      setAllRecord(response.data);

      setSuccessfulMessage({
        message: "Record created successfully!",
        flag: true,
      });
    } catch (error) {
      setFailedMessage({
        message:
          "Something went wrong with record creation. Please try again later!",
        flag: true,
      });
      navigate("/");
    }
    navigate("/");
  };

  if (!allRecords) {
    return <div id="preloader"></div>;
  }

  return (
    <>
      <div className={styles.body}>
        <div className={styles.chooseCreate_Body_RecordContainer}>
          <div className={styles.chooseCreate_RecordContainer}>
            <p className={styles.chooseCreate_RecordContainer_paragraph}>
              Select the record name to get additional information
            </p>
            <select
              onChange={(e) => selectRecordIndex(parseInt(e.target.value, 10))}
              defaultValue="-1"
              className={styles.choose_RecordContainer_selectValue}
            >
              <option value="-1">Choose Record</option>
              {allRecords.map((record, index) => {
                return (
                  <option value={index} key={index}>
                    {record.name.substring(0, 70)}
                  </option>
                );
              })}
            </select>
          </div>
        </div>
        {recordIndex === -1 && (
          <div className={styles.chooseCreate_Body_RecordContainer}>
            <div className={styles.chooseCreate_RecordContainer}>
              <p className={styles.chooseCreate_RecordContainer_paragraph}>
                Crete record with name :
              </p>
              <form
                id={styles.create_RecordContainer_form}
                onSubmit={(e) => recordCreationHandle(e)}
              >
                <input
                  type="text"
                  name="name"
                  placeholder="Enter name"
                  id={styles.create_RecordContainer_inputValue}
                />
                <button type="submit" id={styles.create_RecordContainer_button}>
                  add
                </button>
              </form>
            </div>
          </div>
        )}
        {recordIndex !== -1 && (
          <div>
            <h1 className={styles.h1}>
              Record: {allRecords[recordIndex].name}
            </h1>
            <div className={styles.containerGauge}>
              {types.map((type) => (
                <Gauge
                  key={type}
                  width={200}
                  height={200}
                  type={type}
                  data={allRecords[recordIndex]}
                />
              ))}
              ;
            </div>
            <h2 className={styles.h1}>Detailed Information</h2>
            <div className={styles.containerGauge}>
              {categories.map((category) => (
                <div
                  key={category}
                  className={styles.category}
                  onClick={() => setCategoryShow(category)}
                >
                  {category}
                </div>
              ))}
            </div>
            {categoryShow && (
              <div className={styles.containerInfo}>
                {calculatedPrecentedValues(
                  allRecords[recordIndex],
                  categoryShow
                ).map((d) => {
                  return (
                    <div>
                      <div
                        className={styles.infoContainer}
                        onClick={() => handleOnClick(d.name)}
                      >
                        <span>{d.name}</span>
                        <span>Completed: {d.precented.toFixed(2)}%</span>
                        <FontAwesomeIcon
                          icon={show === d.name ? faMinus : faPlus}
                        />
                      </div>
                      {show === d.name && (
                        <PipeChar width={300} height={30} data={d} />
                      )}
                    </div>
                  );
                })}
              </div>
            )}
          </div>
        )}
      </div>
    </>
  );
};

export default NutritionRecordTemplate;
