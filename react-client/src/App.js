import { Routes, Route } from "react-router-dom";

import UserAuthProvider from "./context/UserAuth";
import UserAuthGuard from "./context/UserAuthGuard";

import Logout from "./components/User/Logout/Logout";
import Header from "./components/Header/Header";
import Home from "./components/HomePage/Home";
import Footer from "./components/Footer/Footer";
import Login from "./components/User/RegisterLogin/Login";
import Register from "./components/User/RegisterLogin/Register";
import Error from "./components/Error/Error";
import NutrientTemplate from "./components/HealTracker/Nutrients/NutrientTemplate";
import EditUser from "./components/User/EditUser/EditUser";
import NotificationProvider from "./context/Notification";
import FoodContextProvider from "./context/FoodContext";
import SelectRecord from "./components/HealTracker/Record/SelectRecord";
import NutritionInfo from "./components/HealTracker/Nutrients/NutritionInfo";
import NutrientFeatureTemplate from "./components/HealTracker/Nutrients/NutrientFeatureTemplate";
import RecordHolder from "./components/HealTracker/Record/RecordHolder";
import CreateRecord from "./components/HealTracker/Record/CreateRecord";
import DeleteRecord from "./components/HealTracker/Record/DeleteRecord";
import StorageHolder from "./components/HealTracker/Storage/StorageHolder";
import SelectStorage from "./components/HealTracker/Storage/SelectStorage";
import CreateStorage from "./components/HealTracker/Storage/CreateStorage";
import DeleteStorage from "./components/HealTracker/Storage/DeleteStorage";
import FoodSection from "./components/HealTracker/FoodSection/FoodSection";
import FoodMenu from "./components/HealTracker/FoodSection/FoodMenu";
import FoodItem from "./components/HealTracker/FoodSection/FoodItem";

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
              <Route path="/nutrientInfo" element={<NutritionInfo />}>
                <Route
                  path=":nutrition/:nutritionType"
                  element={<NutrientTemplate />}
                />
                <Route
                  path="feature/:featureType"
                  element={<NutrientFeatureTemplate />}
                />
              </Route>
              <Route path="*" element={<Error />} />
              <Route path="/editUser" element={<EditUser />} />
              <Route path="/logout" element={<Logout />} />
              <Route element={<UserAuthGuard />}>
                <Route path="/health-tracker" element={<RecordHolder />}>
                  <Route path="selectRecord" element={<SelectRecord />} />
                  <Route path="createRecord" element={<CreateRecord />} />
                  <Route path="record/:recordId" element={<StorageHolder />}>
                    <Route path="deleteRecord" element={<DeleteRecord />} />
                    <Route path="selectStorage" element={<SelectStorage />} />
                    <Route path="createStorage" element={<CreateStorage />} />
                    <Route path="storage/:storageId" element={<FoodSection />}>
                      <Route path="deleteStorage" element={<DeleteStorage />} />
                      <Route path="foodMenu" element={<FoodMenu />} />
                      <Route path="foodItem/:foodName" element={<FoodItem />} />
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
