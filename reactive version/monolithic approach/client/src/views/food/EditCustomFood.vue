<template>
  <Food v-if="food"
        :food="food"
        :originalFood="originalFood"
        @close="handleClose"
        @submit="handleSubmit"/>
</template>

<script setup>
import {computed, onMounted, ref} from 'vue';
import router from "../../router/index.js";
import {useStore} from "vuex";
import {useRoute} from "vue-router";
import Food from "../../components/food/Food.vue";
import {useToast} from "primevue/usetoast";
import {generateGeneralFood} from "../../utils/food.js";

const toast = useToast();
const store = useStore();
const route = useRoute();
const foodId = ref(route.params.foodId);
const food = ref(null);
const originalFood = computed(() => store.getters.currentFood);

onMounted(async () => {
  try {
    const data = await store.dispatch('getCustomFoodById', foodId.value);
    food.value = generateGeneralFood(data);
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