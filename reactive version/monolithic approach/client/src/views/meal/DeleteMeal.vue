<template>
  <div v-if="visible" class="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-50 backdrop-blur">
    <div class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative w-96 p-6" role="alert">
      <span class="block sm:inline">Are you sure you want to delete this meal?</span>
      <div class="flex justify-end gap-2 mt-4">
        <Button type="button" label="Cancel" class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded" @click="handleClose"></Button>
        <Button type="button" label="Yes" class="bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded" @click="submitMeal"></Button>
      </div>
    </div>
  </div>
  <Toast/>
</template>

<script setup>
import {ref} from "vue";
import router from "../../router/index.js";
import {useRoute} from "vue-router";
import {useStore} from "vuex";
import {useToast} from "primevue/usetoast"

const store = useStore();
const toast = useToast();
const route = useRoute();

const mealId = ref(route.params.id);
const visible = ref(true);

const submitMeal = async () => {
  try{
    await store.dispatch('deleteMealById', mealId.value);
    visible.value = false;
    toast.add({severity: 'success', summary: 'Success', detail: 'meal deleted successfully', life: 3000});
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