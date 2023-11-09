import { Routes, Route } from "react-router-dom";

import UserAuthProvider from "./context/UserAuth";
import UserAuthGuard from "./context/UserAuthGuard";

import NutrientInfo from "./components/HealTracker/Nutrients/NutrientInfo";
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
import NutritionRecordProvider from "./context/NutritionRecordContext";
import NutritionRecordTemplate from "./components/HealTracker/Record/NutritionRecordTemplate";
function App() {
  return (
    <>
      <UserAuthProvider>
        <NotificationProvider>
          <NutritionRecordProvider>
            <Header />
            <Routes>
              <Route path="/" element={<Home />} />
              <Route path="/login" element={<Login />} />
              <Route path="/register" element={<Register />} />
              <Route path="/nutrientInfo" element={<NutrientInfo />}>
                <Route
                  path=":nutrition/:nutritionType"
                  element={<NutrientTemplate />}
                />
              </Route>
              <Route path="*" element={<Error />} />
              <Route element={<UserAuthGuard />}>
                <Route path="/logout" element={<Logout />} />
                <Route path="/editUser" element={<EditUser />} />
                <Route path="/nutritionRecord" element={<NutritionRecordTemplate />} />
              </Route>
            </Routes>
            <Footer />
          </NutritionRecordProvider>
        </NotificationProvider>
      </UserAuthProvider>
    </>
  );
}

export default App;
