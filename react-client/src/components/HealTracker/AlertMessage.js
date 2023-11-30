import React, { useContext } from "react";

import { NotificationContext } from "../../context/Notification";
import SuccessMessage from "../Notifications/SuccessfulMessage";
import FailedMessage from "../Notifications/FailedMessage";

const AlertMessage = () => {
  const {
    setFailedMessage,
    successfulMessage,
    failedMessage,
    setSuccessfulMessage,
  } = useContext(NotificationContext);

  return (
    <>
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
    </>
  );
};

export default AlertMessage;
