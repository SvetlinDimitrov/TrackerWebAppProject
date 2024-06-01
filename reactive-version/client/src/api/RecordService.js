import store from "../store/index.js";
import axios from "../axios.js";

export const getRecord = (data) => {

    const authHeader = store.getters.authHeader;

    return axios.post('/record', data, {
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
            throw new Error('Record creation failed');
        });
}
