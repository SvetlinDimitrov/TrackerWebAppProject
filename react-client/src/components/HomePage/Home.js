import React, { useContext } from "react";

import AboutUsSection from "./AboutUsSection";
import AskQuestions from "./AskQuestions";
import ClientSection from "./ClientsSection";
import HeroSection from "./HeroSection";
import OurServices from "./OurServices";


import { NotificationContext } from "../../context/Notification";

import FailedMessage from "../Notifications/FailedMessage";
import SuccessMessage from "../Notifications/SuccessfulMessage";
import stylesNotification from "../Notifications/SuccessfulMessage.module.css";

const Home = () => {
  const {
    failedMessage,
    setFailedMessage,
    successfulMessage,
    setSuccessfulMessage,
  } = useContext(NotificationContext);
  
  return (
    <div>
      {(successfulMessage.flag || failedMessage.flag) && (
        <div className={stylesNotification.overlay}></div>
      )}
      {successfulMessage.flag && (
        <SuccessMessage
          message={successfulMessage.message}
          onClose={() => {
            setSuccessfulMessage(false);
          }}
        />
      )}
      {failedMessage.flag && (
        <FailedMessage
          message={failedMessage.message}
          onClose={() => {
            setFailedMessage(false);
          }}
        />
      )}
      <HeroSection />
      <main id="main">
        <AboutUsSection />
        <ClientSection />
        <OurServices />
        <AskQuestions />
      </main>
    </div>
  );
};

export default Home;
