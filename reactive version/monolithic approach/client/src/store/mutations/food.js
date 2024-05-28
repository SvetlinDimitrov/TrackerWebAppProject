export default {
    setSearchedFoodWord(state , word) {
        state.searchFood = word;
    },
    setSearchedFoodResult(state, result) {
        state.searchFoodResult = result;
    },
    setTotalPagesCustomFood(state, pages) {
        state.totalPagesCustomFood = pages;
    },
}