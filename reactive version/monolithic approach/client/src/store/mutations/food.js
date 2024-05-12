export default {
    setSearchedFoodWord(state , word) {
        state.searchFood = word;
    },
    setSearchedFoodResult(state, result) {
        state.searchFoodResult = result;
    },
    setCurrentFood(state, food) {
        state.currentFood = food;
    },
}