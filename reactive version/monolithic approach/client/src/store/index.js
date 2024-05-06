import { createStore } from 'vuex';
import {deleteUser, login as loginUser, register} from '../api/UserService';

const store = createStore({
    state() {
        return {
            user: JSON.parse(localStorage.getItem('user')),
        };
    },
    mutations: {
        setUser(state, user) {
            state.user = user;
        },
        removeUser(state) {
            state.user = null;
        },
    },
    actions: {
        logout({ commit }) {
            localStorage.removeItem('user');
            commit('removeUser');
        },
        login({ commit }, { email, password }) {
            return loginUser(email, password)
                .then(user => {
                    localStorage.setItem('user', JSON.stringify(user));
                    commit('setUser', user);
                });
        },
        register({ commit }, data) {
            return register(data)
                .then(user => {
                    localStorage.setItem('user', JSON.stringify(user));
                    commit('setUser', user);
                });
        },
        deleteUser({ commit }) {
            return deleteUser()
                .then(() => {
                    localStorage.removeItem('user');
                    commit('removeUser');
                });
        },
    },
    getters: {
        user: state => state.user
    },
    modules: {

    }
});

export default store;