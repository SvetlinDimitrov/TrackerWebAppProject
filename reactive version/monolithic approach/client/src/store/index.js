import { createStore } from 'vuex';
import { login as loginUser } from '../api/UserService';

const store = createStore({
    state() {
        return {
            user: JSON.parse(localStorage.getItem('user')) || { username: '', password: '' },
        };
    },
    mutations: {
        setUser(state, user) {
            state.user = user;
        },
    },
    actions: {
        login({ commit }, { username, password }) {
            return loginUser(username, password)
                .then(user => {
                    localStorage.setItem('user', JSON.stringify(user));
                    commit('setUser', user);
                });
        },
    },
    getters: {
        isLoggedIn: state => !!state.user && !!state.user.username,
    },
    modules: {

    }
});

export default store;