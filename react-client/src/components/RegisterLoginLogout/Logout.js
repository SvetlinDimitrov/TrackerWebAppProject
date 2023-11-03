import { useContext ,useEffect} from "react"
import { useNavigate } from "react-router-dom";

import { AuthContext } from "../../context/UserAuth"

const Logout = () => {
    const { logout } = useContext(AuthContext);
    const navigate = useNavigate();

    useEffect(() => {
        logout();
        navigate('/');
    },[logout , navigate])
    
}

export default Logout;