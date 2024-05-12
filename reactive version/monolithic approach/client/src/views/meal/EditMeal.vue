<template>
  <div v-if="visible" class="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-50 backdrop-blur">
    <div class="bg-white rounded-lg w-96 p-6">
      <h2 class="text-xl font-bold mb-4">{{ header }}</h2>
      <div class="flex items-center gap-3 mb-3">
        <label for="mealName" class="font-semibold w-[6rem]">Meal Name</label>
        <InputText id="mealName" v-model="mealName" class="flex-auto" autocomplete="off" />
      </div>
      <div class="flex justify-end gap-2">
        <Button type="button" label="Cancel" severity="secondary" @click="handleClose"></Button>
        <Button type="button" label="Save" @click="submitMeal"></Button>
      </div>
    </div>
  </div>
</template>

<script setup>
import {onMounted, ref} from "vue";
import router from "../../router/index.js";
import {useRoute} from "vue-router";
import {useStore} from "vuex";
import {useToast} from "primevue/usetoast"

const store = useStore();
const toast = useToast();
const route = useRoute();

const mealId = ref(route.params.id);
const meal = ref(store.getters.meals[mealId.value]);
const mealName = ref(meal.value?.name);
const visible = ref(true);
const header = ref('Edit Meal');

onMounted(() => {
  if (!meal.value) {
    toast.add({severity: 'error', summary: 'Error', detail: 'no meal found', life: 3000});
    router.push({name: 'Home'});
  }
});

const submitMeal = async () => {
  try{
    const data = {
      payload: {
        name: mealName.value
      },
      id: mealId.value
    }
    await store.dispatch('modifyMealById', data);
    visible.value = false;
    toast.add({severity: 'success', summary: 'Success', detail: 'name changed successfully', life: 3000});
    await router.push({name: 'Home'});
  }catch (error) {
    toast.add({severity: 'error', summary: 'Error', detail: error.message, life: 3000});
  }
};

const handleClose = () => {
  router.push({name: 'Home'});
  visible.value = false;
};
</script>

<style scoped>
</style>