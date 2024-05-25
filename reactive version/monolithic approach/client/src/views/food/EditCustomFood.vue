<template>
  <CustomFoodCreation
      v-if="food"
      header="Edit Custom Food"
      :food="food"
      @submit="handleSubmit"
      @close="handleClose"/>
</template>

<script setup>
import {onMounted, ref} from 'vue';
import router from "../../router/index.js";
import {useStore} from "vuex";
import {useRoute} from "vue-router";
import {useToast} from "primevue/usetoast";
import CustomFoodCreation from "../../components/food/CustomFoodCreation.vue";

const toast = useToast();
const store = useStore();
const route = useRoute();
const foodId = ref(route.params.foodId);
const food = ref(null);

onMounted(async () => {
  try {
    food.value = await store.dispatch('getCustomFoodById', foodId.value);
  } catch (error) {
    toast.add({severity: 'error', summary: 'Error', detail: error.message, life: 3000});
    router.go(-1);
  }

  if (food.value === null) {
    toast.add({severity: 'error', summary: 'Error', detail: 'Food not found', life: 3000});
    router.go(-1);
  }
});

const handleClose = () => {
  router.go(-1);
};

const handleSubmit = async (food) => {
  try {
    await store.dispatch('changeCustomFoodById', {food, id: foodId.value});
    toast.add({severity: 'success', summary: 'Success', detail: 'Food updated successfully', life: 3000});
    router.go(-1);
  } catch (error) {
    toast.add({severity: 'error', summary: 'Error', detail: error.message, life: 3000});
    router.go(-1);
  }
};
</script>

<style scoped>

</style>