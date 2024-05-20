export default {
    setRecordSettingsNutrition(state, nutrition) {
        const index = state.recordSettingData.nutritions.findIndex(n => n.name === nutrition.name);

        if (index !== -1) {
            state.recordSettingData.nutritions.splice(index, 1, nutrition);
        } else {
            state.recordSettingData.nutritions.push(nutrition);
        }
        localStorage.removeItem('nutritions');
        localStorage.setItem('nutritions', JSON.stringify(state.recordSettingData.nutritions));
    },

    removeRecordSettingsNutrition(state) {
        localStorage.removeItem('nutritions');
        state.recordSettingData.nutritions = [];
    }
};