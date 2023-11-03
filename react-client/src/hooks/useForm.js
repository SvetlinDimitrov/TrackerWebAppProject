import {useState} from 'react';


export const useForm = (initValue , handleOnSubmit) => {
    const [values , setValues] = useState(initValue);
    

    const onChange = (e) =>{
        setValues(state => ({...state , [e.target.name]: e.target.value} ))
    }
    const onSubmit = (e) =>{
        e.preventDefault();
        handleOnSubmit(values);
    }

   
    return {values , onChange , onSubmit};
}