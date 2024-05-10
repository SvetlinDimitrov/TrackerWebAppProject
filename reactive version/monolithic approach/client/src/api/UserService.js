import axios from '../axios';
import store from '../store';

export const login = (email, password) => {
    const token = btoa(`${email}:${password}`);

    return axios.get('/user/details', {
        headers: {
            'Authorization': `Basic ${token}`
        }
    })
        .then(response => {
            if (response.status === 200) {
                return response.data;
            } else {
                throw new Error('Login failed');
            }
        })
        .catch(error => {
            if (error.response && error.response.status === 401) {
                throw new Error('Login failed');
            } else {
                throw new Error(error.response.data.message);
            }
        });
}

export const register = (data) => {
    return axios.post('/user', data)
        .then(response => {
            if (response.status === 201) {
                return data;
            } else {
                throw new Error('Register failed');
            }
        })
        .catch(error => {
            throw new Error(error.response.data.message);
        });
}

export const modifyUserDetails = (data) => {

    const authHeader = store.getters.authHeader;

    return axios.patch('/user/details', data, {
        headers: {
            'Authorization': authHeader
        }
    })
        .then(response => {
            return response.data;
        })
        .catch(error => {
            if (error.response && error.response.data.message) {
                throw new Error(error.response.data.message);
            }
            throw new Error('Something went wrong modifying user details');
        });
}

export const deleteUser = () => {

    const authHeader = store.getters.authHeader;

    return axios.delete('/user', {
        headers: {
            'Authorization': authHeader
        }
    })
        .catch(error => {
            if (error.response && error.response.data.message) {
                throw new Error(error.response.data.message);
            }
            throw new Error('User deletion failed');
        });
}

export const getUserDetails = () => {

    const authHeader = store.getters.authHeader;

    return axios.get('/user/details', {
        headers: {
            'Authorization': authHeader
        }
    })
        .then(response => {
            return response.data;
        })
        .catch(error => {
            if (error.response && error.response.data.message) {
                throw new Error(error.response.data.message);
            }
            throw new Error('Something went wrong fetching user details');
        });
}