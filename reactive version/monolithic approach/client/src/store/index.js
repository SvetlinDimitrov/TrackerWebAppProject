import {createStore} from 'vuex';
import {deleteUser, login as loginUser, modifyUserDetails, register} from '../api/UserService';
import {getRecord} from "../api/RecordService.js";
import {createMeal, deleteMealById, getAllMeals, modifyMeal} from "../api/MealService.js";

const store = createStore({
    state() {
        return {
            user: JSON.parse(localStorage.getItem('user')),
            userDetails: null,
            record: null,
            recordSettingData: {},
            meals: {},
        };
    },
    mutations: {
        setUser(state, user) {
            state.user = user;
        },
        removeUser(state) {
            state.user = null;
        },
        setUserDetails(state, userDetails) {
            state.userDetails = userDetails;
        },
        removeUserDetails(state) {
            state.userDetails = null;
        },
        setRecord(state, record) {
            state.record = record
        },
        removeRecord(state) {
            state.record = null;
        },
        setMeals(state, meals) {
            meals.forEach(meal => {
                state.meals[meal.id] = meal;
            });
        },
        setNewMealById(state , meal) {
            state.meals[meal.id] = meal;
        },
        removeMealById(state , mealId) {
            delete state.meals[mealId];
        }
    },
    actions: {
        fetchDataWhenDOMContentLoaded({state, dispatch}) {
            if (state.user) {
                return dispatch('login', state.user);
            }
        },
        logout({commit}) {
            localStorage.removeItem('user');
            commit('removeUser');
            commit('removeRecord');
            commit('removeUserDetails');
        },
        login({commit, dispatch, getters}, {email, password}) {
            return loginUser(email, password)
                .then(userDetails => {
                    localStorage.setItem('user', JSON.stringify({email, password}));
                    commit('setUser', {email, password});
                    commit('setUserDetails', userDetails);
                })
                .then(() => {
                    if (getters.isFullyRegistered) {
                        return dispatch('setRecord');
                    }
                })
                .then(() => {
                    if (getters.isFullyRegistered) {
                        return dispatch('getAllMeals');
                    }
                })
        },
        register({commit}, data) {
            return register(data)
                .then(user => {
                    localStorage.setItem('user', JSON.stringify(user));
                    commit('setUser', user);
                });
        },
        deleteUser({commit}) {
            return deleteUser()
                .then(() => {
                    localStorage.removeItem('user');
                    commit('removeUser');
                    commit('removeRecord');
                    commit('removeUserDetails');
                });
        },
        updateUserDetails({commit, dispatch, getters}, data) {
            return modifyUserDetails(data)
                .then(userDetails => {
                    commit('setUserDetails', userDetails);
                    return userDetails;
                })
                .then(userDetails => {
                    if (getters.isFullyRegistered) {
                        return dispatch('setRecord');
                    }
                })
        },
        removeUserDetails({commit}) {
            commit('removeUserDetails');
        },
        setRecord({commit, state}) {
            return getRecord(state.recordSettingData)
                .then(record => {
                    commit('setRecord', record);
                });
        },
        removeRecord({commit}) {
            commit('removeRecord');
        },
        getAllMeals({commit}) {
            return getAllMeals()
                .then(meals => {
                    commit('setMeals', meals);
                });
        },
        createNewMeal({commit}, data) {
            return createMeal(data)
                .then(meal => {
                    commit('setNewMealById', meal);
                });
        },
        modifyMealById({commit}, data) {
            return modifyMeal(data.payload , data.id)
                .then(meal => {
                    commit('setNewMealById', meal);
                });
        },
        deleteMealById({commit}, mealId) {
            return deleteMealById(mealId)
                .then(() => {
                    commit('removeMealById', mealId);
                });
        }

    },
    getters: {
        user: state => state.user,
        record: state => state.record,
        userDetails: state => state.userDetails,
        isFullyRegistered: state =>
            state.userDetails &&
            state.userDetails.age &&
            state.userDetails.gender &&
            state.userDetails.height &&
            state.userDetails.kilograms &&
            state.userDetails.workoutState,
        authHeader: getters => {
            if (getters.user) {
                return `Basic ${btoa(`${getters.user.email}:${getters.user.password}`)}`;
            }
            return null;
        },
        meals: state => state.meals,
    },
    modules: {}
});

export default store;