import { useContext } from "react";
import { useNavigate , useParams} from "react-router-dom";

import * as PathCreator from "../../../util/PathCreator";
import api from "../../../util/api";
import { NotificationContext } from "../../../context/Notification";
import { AuthContext } from "../../../context/UserAuth";
import styles from "./DeleteStorage.module.css";

const DeleteStorage = () => {
  const { setFailedMessage, setSuccessfulMessage } =
    useContext(NotificationContext);
  const { recordId , storageId} = useParams();
  const { user } = useContext(AuthContext);
  const userToken = user.tokenInfo.token;
  const navigate = useNavigate();

  const onClose = () => {
    navigate(PathCreator.recordPath(recordId));
  };

  const handleDeletion = async () => {
    const confirmation = window.confirm(
      "Are you sure you want to delete this record?"
    );
    if (confirmation) {
      try {
        await api.delete(`/record/${recordId}/storage/${storageId}`, {
          headers: { Authorization: `Bearer ${userToken}` },
        });
        setSuccessfulMessage({
          message:
            "Storage deletion was successful!",
          flag: true,
        });
      } catch (error) {
        setFailedMessage({
          message:
            "Something went wrong with storage deletion. Please try again later!",
          flag: true,
        });
      }
    } 
    navigate(PathCreator.recordPath(recordId));
  };
  return (
    <div className={styles.overlay}>
      <div className={styles.container}>
        <button className={styles.closeButton} onClick={onClose}>
          X
        </button>
        <h2 className={styles.container_header}>Delete Storage</h2>
        <button
          className={styles.container_deleteButton}
          onClick={() => handleDeletion()}
        >
          Delete 
        </button>
      </div>
    </div>
  );
};

export default DeleteStorage;
