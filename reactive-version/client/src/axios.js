import axios from 'axios';
import store from './store/index.js';
import { useToast } from 'primevue/usetoast';

const instance = axios.create({
    baseURL: import.meta.env.VITE_API_URL,
    withCredentials: true
});

// instance.interceptors.response.use(
//     response => {
//         return response;
//     },
//     async error => {
//         if (error.response && error.response.status === 401) {
//             await store.dispatch('logout');
//         }
//
//         return Promise.reject(error);
//     }
// );

export default instance;