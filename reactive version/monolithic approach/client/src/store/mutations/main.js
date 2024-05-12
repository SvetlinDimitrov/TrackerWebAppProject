export default {
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
    setIsLoading(state, isLoading) {
        state.isLoading = isLoading;
    },
    setLoginCompleted(state, loginCompleted) {
        state.loginCompleted = loginCompleted;
    }
};