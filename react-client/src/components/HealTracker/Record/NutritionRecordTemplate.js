import { faMinus, faPlus } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { useContext, useState , useEffect} from "react";
import { useParams, useNavigate } from "react-router-dom";
import { AuthContext } from "../../../context/UserAuth";

import { calculatedPrecentedValues } from "../../../util/RecordUtils";
import api from "../../../util/api";
import styles from "./NutritionRecord.module.css";
import { Gauge, PipeChar } from "./Tools";

const NutritionRecordTemplate = () => {
  const { recordId } = useParams();
  const { user } = useContext(AuthContext);
  const [record , setRecord] = useState(undefined);
  const [categoryShow, setCategoryShow] = useState(undefined);
  const [show, setShow] = useState("");
  const types = ["Vitamin", "Electrolyte", "Macronutrient", "Calories"];
  const categories = ["Vitamin", "Electrolyte", "Macronutrient"];
  const userToken = user.tokenInfo.token;

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await api.get(`/record/${recordId}`, {
          headers: { Authorization: `Bearer ${userToken}` },
        });
        setRecord(response.data);
      } catch (error) {
      }
    };
    fetchData();
  }, []);

  const handleOnClick = (name) => {
    setShow((prevShow) => (prevShow === name ? "" : name));
  };

  // const recordCreationHandle = async (e) => {
  //   e.preventDefault();

  //   const name = e.target.name.value;

  //   if (name.length < 2) {
  //     setFailedMessage({
  //       message: "Name must be at least 2 character long!",
  //       flag: true,
  //     });
  //     navigate("/");
  //     return;
  //   }
  //   try {
  //     await api.post(
  //       `/record?name=${encodeURIComponent(name)}`,
  //       {},
  //       { headers: { Authorization: `Bearer ${userToken}` } }
  //     );
  //     const allRecordsResponse = await api.get("/record/all", {
  //       headers: { Authorization: `Bearer ${userToken}` },
  //     });

  //     const allRecords = allRecordsResponse.data;

  //     setRecords(allRecords);

  //     setSuccessfulMessage({
  //       message: "Record created successfully!",
  //       flag: true,
  //     });
  //   } catch (error) {
  //     setFailedMessage({
  //       message:
  //         "Something went wrong with record creation. Please try again later!",
  //       flag: true,
  //     });
  //     navigate("/");
  //   }
  //   navigate("/");
  // };

  if (record === undefined) {
    return <div id="preloader"></div>;
  }

  return (
    <>
      <div className={styles.body}>
        {/* {recordId === -1 && (
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
        )} */}
        <div>
          <h1 className={styles.h1}>Record: {record.name}</h1>
          <div className={styles.containerGauge}>
            {types.map((type) => (
              <Gauge
                key={type}
                width={200}
                height={200}
                type={type}
                data={record}
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
              {calculatedPrecentedValues(record, categoryShow).map((d) => {
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
        )
      </div>
    </>
  );
};

export default NutritionRecordTemplate;
