import { Route, Routes } from "react-router-dom";

import UserAuthProvider from "./context/UserAuth";
import UserAuthGuard from "./context/UserAuthGuard";

import AchievementDetails from "./components/AchievementTracker/AchievementDetails";
import AchievementTracker from "./components/AchievementTracker/AchievementTracker";
import AchievementUpdate from "./components/AchievementTracker/AchievementUpdate";
import CreateAchievement from "./components/AchievementTracker/CreateAchievement";
import SelectAchievement from "./components/AchievementTracker/SelectAchievement";
import Error from "./components/Error/Error";
import Footer from "./components/Footer/Footer";
import Header from "./components/Header/Header";
import CreateCustomFood from "./components/HealTracker/FoodSection/CreateCustomFood";
import CustomFoodSection from "./components/HealTracker/FoodSection/CustomFoodSection";
import FoodItem from "./components/HealTracker/FoodSection/FoodItem";
import FoodMenu from "./components/HealTracker/FoodSection/FoodMenu";
import FoodSection from "./components/HealTracker/FoodSection/FoodSection";
import NutrientFeatureTemplate from "./components/HealTracker/Nutrients/NutrientFeatureTemplate";
import NutritionInfo from "./components/HealTracker/Nutrients/NutritionInfo";
import NutritionTemplate from "./components/HealTracker/Nutrients/NutritionTemplate";
import CreateRecord from "./components/HealTracker/Record/CreateRecord";
import DeleteRecord from "./components/HealTracker/Record/DeleteRecord";
import RecordHolder from "./components/HealTracker/Record/RecordHolder";
import SelectRecord from "./components/HealTracker/Record/SelectRecord";
import CreateStorage from "./components/HealTracker/Storage/CreateStorage";
import SelectStorage from "./components/HealTracker/Storage/SelectStorage";
import StorageHolder from "./components/HealTracker/Storage/StorageHolder";
import Home from "./components/HomePage/Home";
import EditUser from "./components/User/EditUser/EditUser";
import Login from "./components/User/RegisterLogin/Login";
import Register from "./components/User/RegisterLogin/Register";
import Settings from "./components/User/Settings/Settings";
import FoodContextProvider from "./context/FoodContext";
import NotificationProvider from "./context/Notification";
function App() {
  return (
    <>
      <UserAuthProvider>
        <NotificationProvider>
          <FoodContextProvider>
            <Header />
            <Routes>
              <Route path="/" element={<Home />} />
              <Route path="/login" element={<Login />} />
              <Route path="/register" element={<Register />} />
              <Route path="/nutrientInfo" element={<NutritionInfo />} />
              <Route path="*" element={<Error />} />

              <Route element={<UserAuthGuard />}>
                <Route path="/nutrientInfo" element={<NutritionInfo />}>
                  <Route
                    path=":nutrition/:nutritionType"
                    element={<NutritionTemplate />}
                  />
                  <Route
                    path="feature/:featureType"
                    element={<NutrientFeatureTemplate />}
                  />
                </Route>
                <Route path="/settings" element={<Settings />} />
                <Route path="/settings/editUser" element={<EditUser />} />
                <Route path="/achievements" element={<AchievementTracker />}>
                  <Route path="select" element={<SelectAchievement />} />
                  <Route path="create" element={<CreateAchievement />} />
                  <Route path=":achId" element={<AchievementDetails />} >
                    <Route path="addProgress/:achName" element={<AchievementUpdate />} />
                    </Route>
                </Route>
                <Route path="/health-tracker" element={<RecordHolder />}>
                  <Route path="selectRecord" element={<SelectRecord />} />
                  <Route path="createRecord" element={<CreateRecord />} />
                  <Route path="record/:recordId" element={<StorageHolder />}>
                    <Route path="deleteRecord" element={<DeleteRecord />} />
                    <Route path="selectStorage" element={<SelectStorage />} />
                    <Route path="createStorage" element={<CreateStorage />} />
                    <Route path="storage/:storageId" element={<FoodSection />}>
                      <Route path="foodMenu" element={<FoodMenu />} />
                      <Route path="foodItem/:foodName" element={<FoodItem />} />
                      <Route path="customFood" element={<CustomFoodSection />}>
                        <Route
                          path="create"
                          element={<CreateCustomFood />}
                        ></Route>
                      </Route>
                    </Route>
                  </Route>
                </Route>
              </Route>
            </Routes>
            <Footer />
          </FoodContextProvider>
        </NotificationProvider>
      </UserAuthProvider>
    </>
  );
}

export default App;
