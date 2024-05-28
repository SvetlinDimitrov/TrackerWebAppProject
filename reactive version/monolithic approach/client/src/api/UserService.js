import axios from '../axios';
import store from '../store';

export const login = (email, password) => {

    return axios.post('/user/login', {email, password})
        .then(response => {
            return response.data;
        })
        .catch(error => {
            if (error.response.data?.message) {
                throw new Error(error.response.data.message);
            }
            throw new Error('Login failed');
        });
}

export const register = (data) => {

    return axios.post('/user', data)
        .then(response => {
            return response.data;
        })
        .catch(error => {
            if (error.response.data?.message) {
                throw new Error(error.response.data.message);
            }
            throw new Error('Register failed');
        });
}

export const modifyUserDetails = (data) => {

    const authHeader = store.getters.authHeader;

    return axios.patch('/user/details', data, {headers: {'Authorization': authHeader}})
        .then(response => {
            return response.data;
        })
        .catch(error => {
            if (error.response.data?.message) {
                throw new Error(error.response.data.message);
            }
            throw new Error('Something went wrong modifying user details');
        });
}

export const deleteUser = () => {

    const authHeader = store.getters.authHeader;

    return axios.delete('/user', {headers: {'Authorization': authHeader}})
        .catch(error => {
            if (error.response.data?.message) {
                throw new Error(error.response.data.message);
            }
            throw new Error('User deletion failed');
        });
}

export const resetUserPassword = (data) => {

    return axios.patch('/user/reset-password', data)
        .catch(error => {
            if (error.response.data?.message) {
                throw new Error(error.response.data.message);
            }
            throw new Error('Something went wrong resetting user password');
        });
}