import { useState, useMemo , useEffect} from "react";
import { useNavigate, useParams, Outlet } from "react-router-dom";

import styles from "./FoodMenu.module.css";
import * as PathCreator from "../../../../util/PathCreator";

export const FoodMenu = () => {
  const { recordId, storageId } = useParams();
  // const { allFoods } = useContext(FoodContext);
  const [allFoods, setAllFoods] = useState(undefined);
  const navigate = useNavigate();
  const [searchTerm, setSearchTerm] = useState("");

  function useDebounce(value, delay) {
    const [debouncedValue, setDebouncedValue] = useState(value);

    useEffect(() => {
      const handler = setTimeout(() => {
        setDebouncedValue(value);
      }, delay);

      return () => {
        clearTimeout(handler);
      };
    }, [value, delay]);

    return debouncedValue;
  }

  const debouncedSearchTerm = useDebounce(searchTerm, 500);

  const filteredFoods = useMemo(() => {
    return allFoods
      .map((food) => food.data)
      .flat()
      .filter((food) =>
        food.description
          .toLowerCase()
          .includes(debouncedSearchTerm.toLowerCase())
      );
  }, [allFoods, debouncedSearchTerm]);

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
              {filteredFoods.map((food, index) => {
                return (
                  <div
                    className={styles.container_foodDetails_food}
                    key={index}
                    onClick={() =>
                      navigate(
                        PathCreator.storagePath(recordId, storageId) +
                          "/foodMenu/" +
                          food.description
                      )
                    }
                  >
                    <p className={styles.container_foodDetails_food_info}>
                      {food.description}
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
