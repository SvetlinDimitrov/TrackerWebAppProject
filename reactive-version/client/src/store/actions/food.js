import {addFoodIntoMeal, changeFoodByIdAndMealId, getMealById, removeFoodByIdAndMealId} from "../../api/MealService.js";
import {getAllFoodsBySearchWord, getBrandedFoodById, getCommonFoodByName} from "../../api/FoodSeachService.js";
import {getRecord} from "../../api/RecordService.js";
import {filterNutrients, generateGeneralFood} from "../../utils/food.js";
import {
    createCustomFood,
    deleteCustomFood,
    getAllCustomFoodS,
    getCustomFoodById,
    updateCustomFood
} from "../../api/CustomFoodService.js";

export default {
    async addFoodIntoMeal({commit, getters}, data) {
        commit('setIsLoading', true);
        try {
            const mealId = data.mealId;
            const food = data.food;
            await addFoodIntoMeal(mealId, filterNutrients(food));
            const meal = await getMealById(mealId);
            commit('setNewMealById', meal);
            const record = await getRecord(getters.recordSettingData);
            commit('setRecord', record);
        } catch (e) {
            throw new Error(e.message)
        } finally {
            commit('setIsLoading', false);
        }
    },
    async removeFoodById({commit, getters}, data) {
        commit('setIsLoading', true);
        try {
            const mealId = data.mealId;
            const foodId = data.foodId;
            await removeFoodByIdAndMealId(mealId, foodId);
            const meal = await getMealById(mealId);
            commit('setNewMealById', meal);
            const record = await getRecord(getters.recordSettingData);
            commit('setRecord', record);
        } catch (e) {
            throw new Error(e.message)
        } finally {
            commit('setIsLoading', false);
        }
    },
    async changeFoodById({commit, getters}, data) {
        commit('setIsLoading', true);
        try {
            const mealId = data.mealId;
            const foodId = data.foodId;
            const food = data.food;
            await changeFoodByIdAndMealId(mealId, foodId, filterNutrients(food))
            const meal = await getMealById(mealId);
            commit('setNewMealById', meal);
            const record = await getRecord(getters.recordSettingData);
            commit('setRecord', record);
        } catch (e) {
            throw new Error(e.message)
        } finally {
            commit('setIsLoading', false);
        }
    },
    async getFoodsBySearchName({commit}, searchName) {
        commit('setIsLoading', true);
        try {
            const data = await getAllFoodsBySearchWord(searchName);
            const newFoods = await data.common.concat(data.branded).reduce((acc, food) => {
                if (food.food_name) {
                    acc.push({
                        name: food.food_name,
                        brand: food.brand_name,
                        id: food.nix_item_id,
                    });
                }
                return acc;
            }, []);
            commit('setSearchedFoodResult', newFoods);
            commit('setSearchedFoodWord', searchName);
            return newFoods;
        } catch (e) {
            throw new Error(e.message)
        } finally {
            commit('setIsLoading', false);
        }
    },
    async getCommonFoodByName({commit}, searchName) {
        commit('setIsLoading', true);
        try {
            const food = await getCommonFoodByName(searchName);
            return generateGeneralFood(food[0]);
        } catch (e) {
            throw new Error(e.message)
        } finally {
            commit('setIsLoading', false);
        }
    },
    async getBrandedFoodById({commit}, foodId) {
        commit('setIsLoading', true);
        try {
            const food = await getBrandedFoodById(foodId);
            return generateGeneralFood(food[0]);
        } catch (e) {
            throw new Error(e.message)
        } finally {
            commit('setIsLoading', false);
        }
    },
    async getCustomFoods({commit} , payload) {
        commit('setIsLoading', true);
        try {
            const pageable = await getAllCustomFoodS(payload.page , payload.size);
            commit('setTotalPagesCustomFood', pageable.totalPages);
            return pageable.content;
        } catch (e) {
            throw new Error(e.message)
        } finally {
            commit('setIsLoading', false);
        }
    },
    async createCustomFood({commit}, data) {
        commit('setIsLoading', true);
        try {
            await createCustomFood(filterNutrients(data));
        } catch (e) {
            throw new Error(e.message)
        } finally {
            commit('setIsLoading', false);
        }
    },
    async getCustomFoodById({commit}, id) {
        commit('setIsLoading', true);
        try {
            const food = await getCustomFoodById(id);
            return generateGeneralFood(food);
        } catch (e) {
            throw new Error(e.message)
        } finally {
            commit('setIsLoading', false);
        }
    },
    async changeCustomFoodById({commit, getters}, data) {
        commit('setIsLoading', true);
        try {
            const id = data.id;
            const food = data.food;
            await updateCustomFood(id, filterNutrients(food));
        } catch (e) {
            throw new Error(e.message)
        } finally {
            commit('setIsLoading', false);
        }
    },
    async deleteCustomFoodById({commit}, id) {
        commit('setIsLoading', true);
        try {
            await deleteCustomFood(id);
        } catch (e) {
            throw new Error(e.message)
        } finally {
            commit('setIsLoading', false);
        }
    }
};