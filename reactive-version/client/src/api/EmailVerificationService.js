import axios from "../axios.js";

export const sendEmail = (payload) => {
    return axios.post('/verify/send-email', {...payload})

        .then(response => {
            return response.data;
        })
        .catch(error => {
            if (error.response && error.response.status === 400) {
                throw new Error(error.response.data.message);
            } else if (error.response && error.response.status === 429) {
                throw new Error('To many registrations was made. Please try again tomorrow');
            } else {
                throw new Error('Something went wrong sending email');
            }
        });
}

export const verifyEmail = (token) => {

    return axios.post('/verify/email', {token})
        .catch(error => {
            if (error.response && error.response.status === 400) {
                throw new Error(error.response.data.message);
            } else if (error.response && error.response.status === 429) {
                throw new Error('To many registrations was made. Please try again tomorrow');
            } else {
                throw new Error('Something went wrong sending email');
            }
        });
}

export const sendEmailForNewPasswordTokenLink = (email) => {

    return axios.post('/verify/forgot-password', email)
        .catch(error => {
            if (error.response && error.response.status === 400) {
                throw new Error(error.response.data.message);
            } else if (error.response && error.response.status === 429) {
                throw new Error('To many registrations was made. Please try again tomorrow');
            } else {
                throw new Error('Something went wrong sending email');
            }
        });
}