import { Link } from "react-router-dom";
import { AuthContext } from "../../context/UserAuth";
import { useContext } from "react";

const Header = () => {
  const { user } = useContext(AuthContext);

  return (
    <>
      <section id="topbar" className="topbar d-flex align-items-center">
        <div className="container d-flex justify-content-center justify-content-md-between">
          <div className="contact-info d-flex align-items-center">
            <i className="bi bi-envelope d-flex align-items-center">
              <a href="mailto:contact@example.com">contact@example.com</a>
            </i>
            <i className="bi bi-phone d-flex align-items-center ms-4">
              <span>+1 5589 55488 55</span>
            </i>
          </div>
          <div className="social-links d-none d-md-flex align-items-center">
            <a href="#" className="twitter">
              <i className="bi bi-twitter"></i>
            </a>
            <a href="#" className="facebook">
              <i className="bi bi-facebook"></i>
            </a>
            <a href="#" className="instagram">
              <i className="bi bi-instagram"></i>
            </a>
            <a href="#" className="linkedin">
              <i className="bi bi-linkedin"></i>
            </a>
          </div>
        </div>
      </section>

      <header id="header" className="header d-flex align-items-center">
        <div className="container-fluid container-xl d-flex align-items-center justify-content-between">
          <a href="index.html" className="logo d-flex align-items-center">
            <img src="assets/img/logo.png" alt="" />
          </a>
          <h1>
            Impact<span>.</span>
          </h1>

          <nav id="navbar" className="navbar">
            <ul>
              <li>
                <Link to={"/"}>Home</Link>
              </li>
              {user === undefined || user.tokenInfo === undefined ? (
                <>
                  <li>
                    <Link to={"/register"}>Register</Link>
                  </li>
                  <li>
                    <Link to={"/login"}>Login</Link>
                  </li>
                </>
              ) : (
                <>
                  <li>
                    <Link to={"/logout"}>Logout</Link>
                  </li>

                  <li>
                    <Link to={"/editUser"}>EditUser</Link>
                  </li>
                </>
              )}

              <li className="dropdown">
                <a href="#">
                  <span>Services</span>{" "}
                  <i className="bi bi-chevron-down dropdown-indicator"></i>
                </a>
                <ul>
                  <li>
                    <Link to={"/nutrientInfo"}>NutrientInfo</Link>
                  </li>

                  <li>
                    <Link to={"/foodSection"}>FoodSection</Link>
                  </li>
                  <li>
                    <Link to={"/health-tracker"}>HealthTracker</Link>
                  </li>
                </ul>
              </li>
            </ul>
          </nav>
          <i className="mobile-nav-toggle mobile-nav-show bi bi-list"></i>
          <i className="mobile-nav-toggle mobile-nav-hide d-none bi bi-x"></i>
        </div>
      </header>
    </>
  );
};

export default Header;
