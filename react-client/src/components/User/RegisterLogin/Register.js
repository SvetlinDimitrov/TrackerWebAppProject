import { useState } from "react";
import { useNavigate } from "react-router-dom";

import { useForm } from "../../../hooks/useForm";
import { useErrorForm } from "../../../hooks/useErrorForm";
import api from "../../../util/api";
import React from "react";
import styles from "./LoginRegister.module.css";

const keys = {
  username: "username",
  password: "password",
  email: "email",
  confirmPassword: "confirmPassword",
};

const initValues = {
  [keys.username]: "",
  [keys.password]: "",
  [keys.email]: "",
  [keys.confirmPassword]: "",
};

const Register = () => {
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const submitHandler = async (values) => {
    try {
      if (Object.values(errors).every((error) => error === "")) {
        if (values.password !== values.confirmPassword) {
          setError("Passwords should match !");
        } else {
          await api.post("/auth/register", values);
          navigate("/login");
        }
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
    <div className={styles.body}>
      <div className={styles.logRegContainer}>
        <h2 className={styles.h2}>Register</h2>
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
            autoComplete={keys.username}
            name={keys.username}
            placeholder={keys.username}
            value={values[keys.username]}
            onChange={(e) => {
              onChange(e);
              onChangeError(e);
            }}
            onBlur={(e) => onBluerError(e.target)}
          />

          {errors[keys.username].length !== 0 && <p>{errors[keys.username]}</p>}

          <input
            className={styles.input}
            type="email"
            autoComplete={keys.email}
            name={keys.email}
            placeholder={keys.email}
            value={values[keys.email]}
            onChange={(e) => {
              onChange(e);
              onChangeError(e);
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
            }}
            onBlur={(e) => onBluerError(e.target)}
          />

          {errors[keys.password].length !== 0 && <p>{errors[keys.password]}</p>}

          <input
            className={styles.input}
            type="password"
            autoComplete={keys.confirmPassword}
            name={keys.confirmPassword}
            placeholder={keys.confirmPassword}
            value={values[keys.confirmPassword]}
            onChange={(e) => {
              onChange(e);
              onChangeError(e);
            }}
            onBlur={(e) => onBluerError(e.target)}
          />

          {errors[keys.confirmPassword].length !== 0 && (
            <p>{errors[keys.confirmPassword]}</p>
          )}

          {Object.values(errors).every((error) => error === "") &&
            error !== "" && <p>{error}</p>}

          <button className={styles.button} type="submit">
            Register
          </button>
        </form>
      </div>
    </div>
  );
};

export default Register;
