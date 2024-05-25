<template>
  <CustomFoodCreation
      header="Create Custom Food"
      @submit="submit"
      @close="handleClose"/>
</template>

<script setup>
import router from "../../router/index.js";
import {useStore} from "vuex";
import {useToast} from "primevue/usetoast"
import CustomFoodCreation from "../../components/food/CustomFoodCreation.vue";

const toast = useToast();
const store = useStore();

const submit = async (food) => {
  try {
    await store.dispatch('createCustomFood', food);
    toast.add({severity: 'success', summary: 'Success', detail: 'Food created successfully'});
    router.go(-1);
  } catch (e) {
    toast.add({severity: 'error', summary: 'Error', detail: e.message});
  }
};

const handleClose = () => {
  router.go(-1);
};
</script>