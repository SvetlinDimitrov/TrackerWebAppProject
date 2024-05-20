import {getRecord} from "../../api/RecordService.js";

export default {
    async modifyRecordSettingsNutrition({commit , getters}, data) {
        commit('setRecordSettingsNutrition', data);
        const record = await getRecord(getters.recordSettingData);
        commit('setRecord', record);
    },
};