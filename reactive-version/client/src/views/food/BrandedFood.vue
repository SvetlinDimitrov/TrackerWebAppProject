<template>
  <Food v-if="food && hardCopyOfCurrentFood"
        :food="food"
        :originalFood="hardCopyOfCurrentFood"
        @close="handleClose"
        @submit="handleSubmit"/>
</template>

<script setup>
import {onMounted, ref} from 'vue';
import router from "../../router/index.js";
import {useToast} from "primevue/usetoast"
import {useStore} from "vuex";
import {useRoute} from "vue-router";
import Food from "../../components/food/Food.vue";

const store = useStore();
const route = useRoute();
const toast = useToast();
const mealId = ref(route.params.id);
const foodId = ref(route.params.foodId);
const food = ref(null);
const hardCopyOfCurrentFood = ref(null);

onMounted(async () => {
  const currentMeal = store.getters.meals[mealId.value];

  if (!currentMeal) {
    toast.add({severity: 'error', summary: 'Error', detail: 'no meal found', life: 3000});
    await router.push({name: 'Home'});
    return;
  }

  try {
    food.value = await store.dispatch("getBrandedFoodById", foodId.value);
    hardCopyOfCurrentFood.value = JSON.parse(JSON.stringify(food.value));
  } catch (error) {
    await router.push({name: 'Home'});
    toast.add({severity: 'error', summary: 'Error', detail: error.message, life: 3000});
  }
});

const handleClose = () => {
  router.push({name: 'InsertFood', params: {id: mealId.value}});
};

const handleSubmit = async (food) => {
  const payload = {
    mealId: mealId.value,
    food: food
  };
  try {
    await store.dispatch("addFoodIntoMeal", payload);
    toast.add({severity: 'success', summary: 'Success', detail: 'Food added successfully', life: 3000});
    await router.push({name: 'Home'});
  } catch (error) {
    toast.add({severity: 'error', summary: 'Error', detail: error.message, life: 3000});
  }
};
</script>
