export default {
    user: state => state.user,
    record: state => state.record,
    userDetails: state => state.userDetails,
    isFullyRegistered: state =>
        state.userDetails &&
        state.userDetails.age &&
        state.userDetails.gender &&
        state.userDetails.height &&
        state.userDetails.kilograms &&
        state.userDetails.workoutState,
    authHeader: getters => {
        if (getters.user) {
            return `Basic ${btoa(`${getters.user.email}:${getters.user.password}`)}`;
        }
        return null;
    },
    isLoading: state => state.isLoading,
    recordSettingData: state => state.recordSettingData,
    loginCompleted: state => state.loginCompleted,
};