import { useEffect, useState, useContext } from "react";
import { useNavigate, useParams, useLocation } from "react-router-dom";

import FoodItemBluePrint from "./FoodItemBluePrint";
import api from "../../../util/api";
import { AuthContext } from "../../../context/UserAuth";
import { NotificationContext } from "../../../context/Notification";

import * as PathCreator from "../../../util/PathCreator";

const FoodSectionItem = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const queryParams = new URLSearchParams(location.search);

  const isCustom = queryParams.get("isCustom") === "true";
  const { user } = useContext(AuthContext);
  const { setSuccessfulMessage, setFailedMessage } =
    useContext(NotificationContext);
  const userToken = user.tokenInfo.token;
  const { recordId, storageId, foodName } = useParams();

  const [food, setFood] = useState(null);

  useEffect(() => {
    const getFood = async () => {
      try {
        const food = await api.get(
          `/storage/${storageId}/getFood?recordId=${recordId}&isCustom=${isCustom}&foodName=${foodName}`,
          {
            headers: { Authorization: `Bearer ${userToken}` },
          }
        );
        setFood(food.data);
      } catch (error) {
        setFailedMessage({
          message:
            "Something went wrong with food addition. Please try again later!",
          flag: true,
        });
        navigate(PathCreator.storagePath(recordId, storageId));
      }
    };
    getFood();
  }, [
    recordId,
    storageId,
    foodName,
    userToken,
    setFailedMessage,
    navigate,
    isCustom,
  ]);

  const handleEditFood = async (food) => {
    try {
      await api.patch(
        `/storage/${storageId}/changeFood?recordId=${recordId}`,
        food,
        {
          headers: { Authorization: `Bearer ${userToken}` },
        }
      );
      setSuccessfulMessage({
        message:
          food.size +
          " " +
          food.measurement +
          " of " +
          food.name +
          " edited successfully!",
        flag: true,
      });
      navigate(PathCreator.storagePath(recordId, storageId));
    } catch (error) {
      setFailedMessage({
        message:
          "Something went wrong with food addition. Please try again later!",
        flag: true,
      });
      navigate(PathCreator.storagePath(recordId, storageId));
    }
  };
  const handleDeleteFood = async (food) => {
    if(window.confirm("Are you sure you want to delete " + food.name + " ?") === false){
      return;
    }
    try {
      await api.patch(
        `/storage/${storageId}/removeFood?recordId=${recordId}&foodName=${food.name}&isCustom=${isCustom}`,
        {},
        {
          headers: { Authorization: `Bearer ${userToken}` },
        }
      );
      setSuccessfulMessage({
        message: food.name + " deleted successfully!",
        flag: true,
      });
      navigate(PathCreator.storagePath(recordId, storageId));
    } catch (error) {
      setFailedMessage({
        message:
          "Something went wrong with food addition. Please try again later!",
        flag: true,
      });
      navigate(PathCreator.storagePath(recordId, storageId));
    }
  };
  const onClose = () => {
    navigate(PathCreator.storagePath(recordId, storageId));
  };

  if (!food) {
    return <div id="preloader"></div>;
  }
  return (
    <FoodItemBluePrint
      foodInfo={food}
      handleEditFood={handleEditFood}
      handleDeleteFood={handleDeleteFood}
      onClose={onClose}
    />
  );
};
export default FoodSectionItem;
