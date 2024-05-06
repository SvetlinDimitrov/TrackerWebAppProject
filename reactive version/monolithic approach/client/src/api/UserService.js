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
                return {email, password};
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

    const user = store.getters.user;
    const token = btoa(`${user.email}:${user.password}`);

    return axios.patch('/user/details', data , {
        headers: {
            'Authorization': `Basic ${token}`
        }
    })
        .then(response => {
            const statusFirstDigit = response.status.toString()[0];
            if (statusFirstDigit === '4' || statusFirstDigit === '5') {
                throw new Error('Edit user details failed');
            }
        })
        .catch(error => {
            throw new Error(error.response.data.message);
        });
}

export const deleteUser = () => {

    const user = store.getters.user;
    const token = btoa(`${user.email}:${user.password}`);

    return axios.delete('/user' , {
        headers: {
            'Authorization': `Basic ${token}`
        }
    })
        .then(response => {
            const statusFirstDigit = response.status.toString()[0];
            if (statusFirstDigit === '4' || statusFirstDigit === '5') {
                throw new Error('Deleting user failed');
            }
        })
        .catch(error => {
            throw new Error(error.response.data.message);
        });
}

export const getUserDetails = () => {

    const user = store.getters.user;
    const token = btoa(`${user.email}:${user.password}`);

    return axios.get('/user/details' , {
        headers: {
            'Authorization': `Basic ${token}`
        }
    })
        .then(response => {
            const statusFirstDigit = response.status.toString()[0];
            if (statusFirstDigit === '4' || statusFirstDigit === '5') {
                throw new Error('Edit user details failed');
            }
            return response.data;
        })
        .catch(error => {
            throw new Error(error.response.data.message);
        });
}