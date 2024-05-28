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
    }
};