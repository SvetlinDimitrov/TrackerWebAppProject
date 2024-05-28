export default {
    jwt: state => state.jwt,
    user: state => state.user,
    userDetails: state => state.user?.userDetails,
    record: state => state.record,
    isFullyRegistered: (state, getters) =>
        getters.userDetails &&
        getters.userDetails.age &&
        getters.userDetails.gender &&
        getters.userDetails.height &&
        getters.userDetails.kilograms &&
        getters.userDetails.workoutState,
    authHeader: state => {
        if (state.jwt?.value) {
            return `Bearer ${state.jwt.value}`;
        }
        return null;
    },
    isLoading: state => state.isLoading,
    recordSettingData: state => state.recordSettingData,
    isJwtValid: state => {
        if (state.jwt?.expiresIn) {
            const jwtExpirationDate = new Date(state.jwt.expiresIn);
            const currentDate = new Date();
            return jwtExpirationDate > currentDate;
        }
        return false;
    },
    isRebooting: state => state.isRebooting,
};