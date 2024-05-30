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
  if (food.foodDetails) {
    food.foodDetails.info = food.foodDetails.info === "" ? null : food.foodDetails.info;
    food.foodDetails.largeInfo = food.foodDetails.largeInfo === "" ? null : food.foodDetails.largeInfo;
    food.foodDetails.picture = food.foodDetails.picture === "" ? null : food.foodDetails.picture;
  }
  try {
    await store.dispatch('createCustomFood', food);
    toast.add({severity: 'success', summary: 'Success', detail: 'Food created successfully' , life:3000});
    router.go(-1);
  } catch (e) {
    toast.add({severity: 'error', summary: 'Error', detail: "Food creation failed" , life: 3000});
  }
};

const handleClose = () => {
  router.go(-1);
};
</script>