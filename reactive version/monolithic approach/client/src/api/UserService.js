import axios from '../axios';

export const login = (username, password) => {
    const token = btoa(`${username}:${password}`);

    return axios.get('/user/details', {
        headers: {
            'Authorization': `Basic ${token}`
        }
    })
        .then(response => {
            if (response.status === 200) {
                return {username, password};
            } else {
                throw new Error('Login failed');
            }
        })
        .catch(error => {
            if (error.response && error.response.status === 401) {
                throw new Error('Login failed');
            } else {
                throw new Error(error.response.data);
            }
        });
}

export const register = (username, password, email) => {

    const data = {
        username,
        password,
        email
    };

    return axios.post('/user', data)
        .then(response => {
            if (response.status === 201) {
                return data;
            } else {
                throw new Error('Register failed');
            }
        })
        .catch(error => {
            throw new Error(error.response.data);
        });
}