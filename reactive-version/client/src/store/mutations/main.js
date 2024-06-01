export default {
    setJwt(state, token) {
        state.jwt = token;
    },
    removeJwt(state) {
        state.jwt = null;
    },
    setUser(state, user) {
        state.user = user;
    },
    removeUser(state) {
        state.user = null;
    },
    setRecord(state, record) {
        state.record = record
    },
    removeRecord(state) {
        state.record = null;
    },
    setIsLoading(state, isLoading) {
        state.isLoading = isLoading;
    },
    setIsRebooting(state, isRebooting) {
        state.isRebooting = isRebooting;
    },
    clearState(state) {
        localStorage.removeItem('jwt');
        state.jwt = null;
        state.user = null;
        state.userDetails = null;
        state.record = null;
        localStorage.removeItem('userDetails');
        localStorage.removeItem('nutritions');
        state.recordSettingData = {nutritions: JSON.parse(localStorage.getItem('nutritions')) || []};
        state.meals = {};
        state.isLoading = false;
        state.isRebooting = false;
        state.searchFood = '';
        state.searchFoodResult = [];
        state.totalPagesCustomFood = null;
    },
    addRequestsEmailReset(state, email) {
        state.requestsEmailReset[email] = Date.now();
    }
};