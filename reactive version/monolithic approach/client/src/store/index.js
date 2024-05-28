import {createStore} from 'vuex';
import mainActions from './actions/main.js';
import mealActions from './actions/meal.js';
import foodActions from './actions/food.js';
import recordActions from './actions/record.js';
import mainMutations from './mutations/main.js';
import mealMutations from './mutations/meal.js';
import foodMutations from './mutations/food.js';
import recordMutations from './mutations/record.js';
import mainGetters from './getters/main.js';
import mealGetters from './getters/meal.js';
import foodGetters from './getters/food.js';

const store = createStore({
    state() {
        return {
            jwt: localStorage.getItem('jwt'),
            user: null,
            userDetails: null,
            record: null,
            recordSettingData: {nutritions: JSON.parse(localStorage.getItem('nutritions')) || []},
            meals: {},
            isLoading: false,
            searchFood: '',
            searchFoodResult: [],
            customFoods: null,
        };
    },
    mutations: {
        ...mainMutations,
        ...mealMutations,
        ...foodMutations,
        ...recordMutations
    },
    actions: {
        ...mainActions,
        ...mealActions,
        ...foodActions,
        ...recordActions
    },
    getters: {
        ...mainGetters,
        ...mealGetters,
        ...foodGetters
    },
    modules: {}
});

export default store;