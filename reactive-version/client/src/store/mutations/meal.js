export default {
    setMeals(state, meals) {
        meals.forEach(meal => {
            state.meals[meal.id] = meal;
        });
    },
    setNewMealById(state, meal) {
        state.meals[meal.id] = meal;
    },
    removeMealById(state, mealId) {
        delete state.meals[mealId];
    },
};