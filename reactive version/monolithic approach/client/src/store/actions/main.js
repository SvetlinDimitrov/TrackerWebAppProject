import {
    deleteUser,
    getUser,
    getUserDetails,
    login as loginUser,
    modifyUserDetails,
    register,
    resetUserPassword
} from "../../api/UserService.js";
import {getRecord} from "../../api/RecordService.js";
import {getAllMeals} from "../../api/MealService.js";
import {sendEmail, sendEmailForNewPasswordTokenLink} from "../../api/EmailVerificationService.js";

export default {
    async logout({commit}) {
        commit('setIsLoading', true);
        commit('clearState');
        commit('setIsLoading', false);
    },
    async login({commit, getters, dispatch}, {email, password}) {
        try {
            commit('setIsLoading', true);
            const jwtResponse = await loginUser(email, password);
            const jwt = jwtResponse.accessToken;
            const user = jwtResponse.userView;
            localStorage.setItem('jwt', JSON.stringify(jwt));
            commit('setJwt', jwt);
            commit('setUser', user);

            if (getters.isFullyRegistered) {
                const record = await getRecord(getters.recordSettingData);
                commit('setRecord', record);

                let pageNumber = 0;
                let totalPages = 0;
                do {
                    const response = await getAllMeals({page: pageNumber, size: 10});
                    const meals = response.content;
                    totalPages = response.totalPages;

                    commit('setMeals', meals);

                    pageNumber++;
                } while (pageNumber < totalPages);
            }
        } catch (error) {
            throw new Error('Login failed. Please check your credentials.');
        } finally {
            commit('setIsLoading', false);
        }
    },
    async reboot({commit, getters}) {
        try {
            commit('setIsRebooting', true);
            commit('setIsLoading', true);
            const user = await getUser();
            const userDetails = await getUserDetails();
            const userToSave = {
                user: user,
                userDetails: userDetails
            }
            commit('setUser', userToSave);

            if (getters.isFullyRegistered) {
                const record = await getRecord(getters.recordSettingData);
                commit('setRecord', record);

                let pageNumber = 0;
                let totalPages = 0;

                do {
                    const response = await getAllMeals({page: pageNumber, size: 10});
                    const meals = response.content;
                    totalPages = response.totalPages;

                    commit('setMeals', meals);

                    pageNumber++;
                } while (pageNumber < totalPages);
            }
        } catch (error) {

        } finally {
            commit('setIsLoading', false);
            commit('setIsRebooting', false);
        }
    },
    async register({commit}, data) {
        commit('setIsLoading', true);
        try {
            const jwtResponse = await register(data);
            const jwt = jwtResponse.accessToken;
            const user = jwtResponse.userView;
            localStorage.setItem('jwt', JSON.stringify(jwt));
            commit('setJwt', jwt);
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
            commit('clearState');
        } catch (error) {
            throw new Error(error.message);
        } finally {
            commit('setIsLoading', false);
        }
    },
    async updateUserDetails({commit, getters}, data) {
        commit('setIsLoading', true);
        try {
            const jwtResponse = await modifyUserDetails(data);
            const jwt = jwtResponse.accessToken;
            const user = jwtResponse.userView;
            localStorage.setItem('jwt', JSON.stringify(jwt));
            commit('setJwt', jwt);
            commit('setUser', user);

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
    async verifyUserDataAndSendEmail({commit}, data) {
        commit('setIsLoading', true);
        try {
            await sendEmail(data);
        } catch (error) {
            throw new Error(error.message);
        } finally {
            commit('setIsLoading', false);
        }
    },
    async sendEmailForNewPasswordTokenLink({commit}, email) {
        commit('setIsLoading', true);
        try {
            await sendEmailForNewPasswordTokenLink(email);
        } catch (error) {
            throw new Error(error.message);
        } finally {
            commit('setIsLoading', false);
        }
    },
    async resetPassword({commit}, data) {
        commit('setIsLoading', true);
        try {
            await resetUserPassword(data);
        } catch (error) {
            throw new Error(error.message);
        } finally {
            commit('setIsLoading', false);
        }
    }
};