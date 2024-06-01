import store from "../store/index.js";
import axios from "../axios.js";

export const getAllCustomFoodS = (page, size) => {

    const authHeader = store.getters.authHeader;

    return axios.get('/custom/food/short', {
        headers: {
            'Authorization': authHeader
        },
        params: {
            page: page,
            size: size
        }
    })
        .then(response => {
            return response.data;
        })
        .catch(error => {
            if (error.response && error.response.data.message) {
                throw new Error(error.response.data.message);
            }
            throw new Error('Something went wrong with custom food fetching');
        });
}

export const getCustomFoodById = (id) => {

    const authHeader = store.getters.authHeader;

    return axios.get(`/custom/food/${id}`, {
        headers: {
            'Authorization': authHeader
        },
    })
        .then(response => {
            return response.data;
        })
        .catch(error => {
            if (error.response && error.response.data.message) {
                throw new Error(error.response.data.message);
            }
            throw new Error('Something went wrong with custom food fetching');
        });
}

export const createCustomFood = (customFood) => {

        const authHeader = store.getters.authHeader;

        return axios.post('/custom/food', customFood, {
            headers: {
                'Authorization': authHeader
            },
        })
            .then(response => {
                return response.data;
            })
            .catch(error => {
                if (error.response && error.response.data.message) {
                    throw new Error(error.response.data.message);
                }
                throw new Error('Something went wrong with custom food creation');
            });
}

export const updateCustomFood = (id , customFood) => {

        const authHeader = store.getters.authHeader;

        return axios.put(`/custom/food/${id}`, customFood, {
            headers: {
                'Authorization': authHeader
            },
        })
            .then(response => {
                return response.data;
            })
            .catch(error => {
                if (error.response && error.response.data.message) {
                    throw new Error(error.response.data.message);
                }
                throw new Error('Something went wrong with custom food updating');
            });
}

export const deleteCustomFood = (id) => {

        const authHeader = store.getters.authHeader;

        return axios.delete(`/custom/food/${id}`, {
            headers: {
                'Authorization': authHeader
            },
        })
            .then(response => {
                return response.data;
            })
            .catch(error => {
                if (error.response && error.response.data.message) {
                    throw new Error(error.response.data.message);
                }
                throw new Error('Something went wrong with custom food deleting');
            });
}