import store from "../store/index.js";
import axios from "../axios.js";

export const getAllFoodsBySearchWord = (word) => {

    const authHeader = store.getters.authHeader;

    return axios.get('/food_db_api/search', {
        headers: {
            'Authorization': authHeader
        },
        params: {
            foodName: word
        }
    })
        .then(response => {
            return response.data;
        })
        .catch(error => {
            if (error.response && error.response.data.message) {
                throw new Error(error.response.data.message);
            }
            throw new Error('Invalid food search request');
        });
}

export const getCommonFoodByName = (word) => {

    const authHeader = store.getters.authHeader;

    return axios.get(`/food_db_api/search/common/${word}`, {
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
            throw new Error('Invalid common food request');
        });
}

export const getBrandedFoodById = (id) => {

    const authHeader = store.getters.authHeader;

    return axios.get(`/food_db_api/search/branded/${id}`, {
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
            throw new Error('Invalid branded food request');
        });
}