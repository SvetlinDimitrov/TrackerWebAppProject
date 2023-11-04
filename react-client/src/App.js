import { Routes, Route } from "react-router-dom";

import UserAuthProvider from "./context/UserAuth";
import UserAuthGuard from "./context/UserAuthGuard";

import NutrientInfo from "./components/Nutrients/NutrientInfo";
import Logout from "./components/RegisterLoginLogout/Logout";
import Header from "./components/Header/Header";
import Home from "./components/HomePage/Home";
import Footer from "./components/Footer/Footer";
import Login from "./components/RegisterLoginLogout/Login";
import Register from "./components/RegisterLoginLogout/Register";
import Error from "./components/Error/Error";
import NutrientTemplate from "./components/Nutrients/NutrientTemplate";
import EditUser from "./components/EditUser/EditUser";
import NotificationProvider from "./context/Notification";

function App() {
  return (
    <>
      <UserAuthProvider>
        <NotificationProvider>
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
            </Route>
          </Routes>
          <Footer />
        </NotificationProvider>
      </UserAuthProvider>
    </>
  );
}

export default App;
