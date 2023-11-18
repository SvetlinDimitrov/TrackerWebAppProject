import { useContext, useState } from "react";
import { useNavigate } from "react-router-dom";

import { RecordContext } from "../../../context/RecordContext";
import { AuthContext } from "../../../context/UserAuth";
import { useForm } from "../../../hooks/useForm";
import api from "../../../util/api";

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
  const { loginUser } = useContext(AuthContext);
  const { setRecords } = useContext(RecordContext);

  const submitHandler = async (values) => {
    try {
      if (error === "") {
        const userTokenResponse = await api.post("/auth/login", values);

        const userToken = userTokenResponse.data.token;
        const newUserTokenInfo = userTokenResponse.data;

        const userDataResponse = await api.get("/auth/details", {
          headers: { Authorization: `Bearer ${userToken}` },
        });

        const userData = {
          userData: userDataResponse.data,
          tokenInfo: newUserTokenInfo,
        };
        loginUser(userData);

        if (userDataResponse.data.userDetails === "COMPLETED") {
          const allRecordsResponse = await api.get("/record/all", {
            headers: { Authorization: `Bearer ${userToken}` },
          });

          const allRecords = allRecordsResponse.data;
          setRecords(allRecords);
        }
        navigate("/");
      }
    } catch (error) {
      setError(error.response.data.errorMessages[0]);
    }
  };

  const { values, onChange, onSubmit } = useForm(initValues, submitHandler);

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
          type="email"
          autoComplete={keys.email}
          name={keys.email}
          placeholder={keys.email}
          value={values[keys.email]}
          onChange={(e) => {
            onChange(e);
            setError("");
          }}
          required
          email
        />

        <input
          className={styles.input}
          type="password"
          autoComplete={keys.password}
          name={keys.password}
          placeholder={keys.password}
          value={values[keys.password]}
          onChange={(e) => {
            onChange(e);
            setError("");
          }}
          required
        />

        {error !== "" && <p>{error}</p>}

        <button className={styles.button}>Login</button>
      </form>
    </div>
  );
};

export default Login;
