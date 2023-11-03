import { useState , useContext } from "react";
import { useNavigate } from "react-router-dom";

import { useForm } from "../../hooks/useForm";
import { useErrorForm } from "../../hooks/useErrorForm";
import { AuthContext } from "../../context/UserAuth";
import api from "../../util/api";

import React from "react";
import styles from "./LoginRegister.module.css";

const keys = {
  email: "email",
  password: "password",
};

const initValues = {
  [keys.email]: "",
  [keys.password]: "",
};

const Login = () => {
  const [error, setError] = useState("");
  const navigate = useNavigate();
  const { loginUser } = useContext(AuthContext)

  const submitHandler = async (values) => {
    try {
      if (Object.values(errors).every((error) => error === "") && error === '') {
        
        const response = await api.post("/auth/login", values);
        loginUser(response.data.token);
        navigate("/");
        
      }
    } catch (error) {
      Object.keys(values).forEach((key) =>
        onBluerError({ name: key, value: values[key] })
      );
      setError(error.response.data.errorMessages[0]);
    }
  };

  const { values, onChange, onSubmit } = useForm(initValues, submitHandler);
  const { errors, onChangeError, onBluerError } = useErrorForm(initValues);

  return (
    <div className={styles.logRegContainer}>
      <h2 className={styles.h2}>Login</h2>
      <form
        className={styles.form}
        method="POST"
        onSubmit={(e) => {
          onSubmit(e);
        }}
      >
        <input
          className={styles.input}
          type="text"
          autoComplete={keys.email}
          name={keys.email}
          placeholder={keys.email}
          value={values[keys.email]}
          onChange={(e) => {
            onChange(e);
            onChangeError(e);
            setError("");
          }}
          onBlur={(e) => onBluerError(e.target)}
        />

        {errors[keys.email].length !== 0 && <p>{errors[keys.email]}</p>}

        <input
          className={styles.input}
          type="password"
          autoComplete={keys.password}
          name={keys.password}
          placeholder={keys.password}
          value={values[keys.password]}
          onChange={(e) => {
            onChange(e);
            onChangeError(e);
            setError("");
          }}
          onBlur={(e) => onBluerError(e.target)}
        />

        {errors[keys.password].length !== 0 && <p>{errors[keys.password]}</p>}
        
        {Object.values(errors).every((error) => error === "") &&
          error !== "" && <p>{error}</p>}

        <button className={styles.button}>Login</button>
      </form>
    </div>
  );
};

export default Login;
