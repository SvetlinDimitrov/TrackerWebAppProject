import {createMeal, deleteMealById, modifyMeal} from "../../api/MealService.js";

export default {
    async createNewMeal({commit}, data) {
        commit('setIsLoading', true);
        try {
            const meal = await createMeal(data);
            commit('setNewMealById', meal);
        } catch (e) {
            throw new Error(e.message)
        } finally {
            commit('setIsLoading', false);
        }
    },
    async modifyMealById({commit}, data) {
        commit('setIsLoading', true);
        try {
            const meal = await modifyMeal(data.payload, data.id);
            commit('setNewMealById', meal);
        }catch (e) {
            throw new Error(e.message)
        }finally {
            commit('setIsLoading', false);
        }
    },
    async deleteMealById({commit}, mealId) {
        commit('setIsLoading', true);
        try {
            await deleteMealById(mealId);
            commit('removeMealById', mealId);
        }catch (e) {
            throw new Error(e.message)
        }finally {
            commit('setIsLoading', false);
        }
    },
};