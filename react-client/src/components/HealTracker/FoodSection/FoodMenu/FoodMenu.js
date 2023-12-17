import { useState, useContext } from "react";
import { useNavigate, useParams, Outlet } from "react-router-dom";

import styles from "./FoodMenu.module.css";
import * as PathCreator from "../../../../util/PathCreator";
import { FoodContext } from "../../../../context/FoodContext";

export const FoodMenu = () => {
  const { recordId, storageId } = useParams();
  const { allFoods } = useContext(FoodContext);
  const navigate = useNavigate();
  const [searchTerm, setSearchTerm] = useState("");

  const onClose = () => {
    navigate(PathCreator.storagePath(recordId, storageId));
  };

  if (allFoods === undefined) {
    return <div id="preloader"></div>;
  }

  return (
    <>
      <div className={styles.overlay}>
        <button className={styles.closeButton} onClick={onClose}>
          X
        </button>
        <div className={styles.container}>
          <div className={styles.container_CloseButton}>
            <h1 className={styles.foodMenuTitle}>List Of Foods</h1>
            <input
              type="text"
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
            />
            <div className={styles.container_foodDetails}>
              {allFoods
                .filter((food) =>
                  food.name.toLowerCase().includes(searchTerm.toLowerCase())
                )
                .map((food, index) => {
                  return (
                    <div
                      className={styles.container_foodDetails_food}
                      key={index}
                      onClick={() =>
                        navigate(
                          PathCreator.storagePath(recordId, storageId) +
                            "/foodMenu/" +
                            food.name
                        )
                      }
                    >
                      <p className={styles.container_foodDetails_food_info}>
                        {food.name}
                      </p>
                    </div>
                  );
                })}
            </div>
          </div>
        </div>
      </div>
      <Outlet />
    </>
  );
};

export default FoodMenu;
