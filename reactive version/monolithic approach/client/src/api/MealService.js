import store from "../store/index.js";
import axios from "../axios.js";

export const createMeal = (data) => {

    const authHeader = store.getters.authHeader;

    return axios.post('/meals', data, {
        headers: {
            'Authorization': authHeader
        }
    })
        .then(response => {
            return response.data;
        })
        .catch(error => {
            if (error.response && error.response.data.message) {
                return error.response.data.message;
            }
            throw new Error('Meal creation failed');
        });
}

export const modifyMeal = (data , id) => {

    const authHeader = store.getters.authHeader;

    return axios.patch(`/meals/${id}`, data, {
        headers: {
            'Authorization': authHeader
        }
    })
        .then(response => {
            return response.data;
        })
        .catch(error => {
            if (error.response && error.response.data.message) {
                return error.response.data.message;
            }
            throw new Error('Meal changes failed');
        });
}

export const getMealById = (mealId) => {

    const authHeader = store.getters.authHeader;

    return axios.get(`/meals/${mealId}`, {
        headers: {
            'Authorization': authHeader
        }
    })
        .then(response => {
            return response.data;
        })
        .catch(error => {
            if (error.response && error.response.data.message) {
                return error.response.data.message;
            }
            throw new Error('Fetching meal failed');
        });
}

export const getAllMeals = () => {

    const authHeader = store.getters.authHeader;

    return axios.get(`/meals`, {
        headers: {
            'Authorization': authHeader
        }
    })
        .then(response => {
            return response.data;
        })
        .catch(error => {
            if (error.response && error.response.data.message) {
                return error.response.data.message;
            }
            throw new Error('Fetching meals failed');
        });
}

export const deleteMealById = (mealId) => {

    const authHeader = store.getters.authHeader;

    return axios.delete(`/meals/${mealId}`, {
        headers: {
            'Authorization': authHeader
        }
    })
        .catch(error => {
            if (error.response && error.response.data.message) {
                return error.response.data.message;
            }
            throw new Error('Deleting meal failed');
        });
}