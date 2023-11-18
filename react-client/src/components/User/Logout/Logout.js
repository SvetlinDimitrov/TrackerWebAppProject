import { useContext ,useEffect} from "react"
import { useNavigate } from "react-router-dom";

import { AuthContext } from "../../../context/UserAuth"
import { RecordContext } from "../../../context/RecordContext";
const Logout = () => {
    const { logout } = useContext(AuthContext);
    const { clearRecords } = useContext(RecordContext);
    const navigate = useNavigate();

    useEffect(() => {
        logout();
        clearRecords();
        navigate('/');
    },[logout , navigate , clearRecords])
    
}

export default Logout;