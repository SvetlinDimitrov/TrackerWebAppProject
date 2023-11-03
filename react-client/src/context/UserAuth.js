import { createContext, useState } from "react";

export const AuthContext = createContext();

const UserAuthProvider = ({ children }) => {
  const [userToken, setUserToken] = useState(()=>{

    const userToken = localStorage.getItem('userToken');

    if(userToken !== null && userToken !== undefined){
      return JSON.parse(userToken);
    }

    return '';
  });

  const loginUser = (userToken) =>{
    setUserToken(userToken);
    localStorage.setItem('userToken' , JSON.stringify(userToken));
  }
  const logout = () => {
    setUserToken('');
    localStorage.removeItem('userToken');
  }

  const authUser = { userToken , loginUser ,logout };
  
  return (
    <AuthContext.Provider value={authUser}>{children}</AuthContext.Provider>
  );
};
export default UserAuthProvider;