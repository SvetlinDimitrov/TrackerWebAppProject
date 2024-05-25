import {deleteUser, login as loginUser, modifyUserDetails, register} from "../../api/UserService.js";
import {getRecord} from "../../api/RecordService.js";
import {getAllMeals} from "../../api/MealService.js";

export default {
    async logout({commit}) {
        commit('setIsLoading', true);
        localStorage.removeItem('user');
        commit('removeRecordSettingsNutrition');
        commit('removeUser');
        commit('removeRecord');
        commit('removeUserDetails');
        commit('setIsLoading', false);
    },
    async login({commit, getters, dispatch}, {email, password}) {
        try {
            commit('setIsLoading', true);
            const userDetails = await loginUser(email, password);
            localStorage.setItem('user', JSON.stringify({email, password}));
            commit('setUser', {email, password});
            commit('setUserDetails', userDetails);

            if (getters.isFullyRegistered) {
                const record = await getRecord(getters.recordSettingData);
                commit('setRecord', record);

                const meals = await getAllMeals();
                commit('setMeals', meals);
            }
        } catch (error) {
            await dispatch('logout');
            throw new Error('Session expired, please login again');
        } finally {
            commit('setIsLoading', false);
        }
    },
    async register({commit}, data) {
        commit('setIsLoading', true);
        try {
            const user = await register(data);
            localStorage.setItem('user', JSON.stringify(user));
            commit('setUser', user);
        } catch (error) {
            throw new Error(error.message);
        } finally {
            commit('setIsLoading', false);
        }
    },
    async deleteUser({commit}) {
        commit('setIsLoading', true);
        try {
            await deleteUser();
            localStorage.removeItem('user');
            commit('removeUser');
            commit('removeRecord');
            commit('removeUserDetails');
            commit('removeRecordSettingsNutrition');
        } catch (error) {
            throw new Error(error.message);
        } finally {
            commit('setIsLoading', false);
        }
    },
    async updateUserDetails({commit, getters}, data) {
        commit('setIsLoading', true);
        try {
            const userData = await modifyUserDetails(data);
            commit('setUserDetails', userData);

            if (getters.isFullyRegistered) {
                const record = await getRecord(getters.recordSettingData);
                commit('setRecord', record);
            }

        } catch (error) {
            throw new Error(error.message);
        } finally {
            commit('setIsLoading', false);
        }
    },
};