import { useState } from "react";
import { checkLength , isValidEmail} from "../util/Validator";
export const useErrorForm = (initValues) => {
    
  const [errors, setErrors] = useState(initValues);

  const onBluerError = ({ name, value }) => {
    if(errors[name].length === 0){
      
      if(name === 'username'){
        if(checkLength(value , 4)) {setErrors((state) => ({ ...state, 'username': 'Size must be at least 4 characters !' }));};
      }else if (name === 'password'){
        if(checkLength(value , 5)) {setErrors((state) => ({ ...state, 'password': 'Size must be at least 5 characters !' }));};
      }else if (name === 'email'){
        if(!isValidEmail(value)) {setErrors((state) => ({ ...state, 'email': 'Must be a valid email !' }));};
      }
    }
  };

  const onChangeError = (e) => {
    const { name } = e.target;
    setErrors((state) => ({ ...state, [name]: "" }));
  };

  return { errors, onChangeError, onBluerError };
};