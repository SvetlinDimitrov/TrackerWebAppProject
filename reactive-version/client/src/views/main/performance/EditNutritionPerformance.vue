<template>
  <div v-if="visible" class="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-50 backdrop-blur">
    <div class="bg-white rounded-lg w-96 p-6">
      <h2 class="text-xl font-bold mb-4">Edit: {{ nutritionName }}</h2>
      <div class="flex-col items-start gap-3 mb-3">
        <label for="mealName" class="font-semibold w-[6rem]">Daily recommended intake in {{ measureUnit }}</label>
        <InputNumber id="mealName" v-model="nutritionRecommendation" class="flex-auto" autocomplete="off" :min="0.01" :max="100000"/>      </div>
      <div class="flex justify-end gap-2">
        <Button type="button" label="Cancel" severity="secondary" @click="handleClose"></Button>
        <Button type="button" label="Save" @click="submit"></Button>
      </div>
    </div>
  </div>
</template>

<script setup>
import {ref} from "vue";
import {useRoute} from "vue-router";
import {useStore} from "vuex";
import {useToast} from "primevue/usetoast"
import router from "../../../router/index.js";
import {nutritionUnits} from "../../../utils/nutrition/nutritionMesureUnit.js";

const store = useStore();
const toast = useToast();
const route = useRoute();

const measureUnit = ref(nutritionUnits[route.params.name]);
const nutritionName = ref(route.params.name);
const nutritionRecommendation = ref(Number(route.params.intake));
const visible = ref(true);

const submit = async () => {

  await store.dispatch('modifyRecordSettingsNutrition', {
    name: nutritionName.value,
    recommendedIntake: nutritionRecommendation.value
  });
  toast.add({severity: 'success', summary: 'Success', detail: 'nutrition modified successfully', life: 3000});
  visible.value = false;
  await router.push({name: 'Performance'});

};

const handleClose = () => {
  visible.value = false;
  router.push({name: 'Performance'});
};
</script>