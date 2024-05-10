<template>
  <Food v-if="food"
        :food="food"
        @close="handleClose"
        @submit="handleSubmit"/>
</template>

<script setup>
import {onMounted, ref} from 'vue';
import router from "../../router/index.js";
import {useStore} from "vuex";
import {useRoute} from "vue-router";
import Food from "../../components/Food.vue";
import {useToast} from "primevue/usetoast";

const toast = useToast();
const store = useStore();
const route = useRoute();
const mealId = ref(route.params.id);
const foodId = ref(route.params.foodId);
const food = ref(null);

onMounted(async () => {

  const currentMeal = store.getters.meals[mealId.value];

  currentMeal.foods.forEach((f) => {
    if (f.id === foodId.value) {
      food.value = f;
    }
  });

  if (food.value === null) {
    toast.add({severity: 'error', summary: 'Error', detail: 'Food not found', life: 3000});
    await router.push({name: 'Home'});
  }
});

const handleClose = () => {
  router.push({name: 'Home'});
};

const handleSubmit = async (food) => {
  try {
    await store.dispatch("changeFoodById", {mealId: mealId.value, foodId: foodId.value, food});
    toast.add({severity: 'success', summary: 'Success', detail: 'Food changed successfully', life: 3000});
    await router.push({name: 'Home'});
  } catch (error) {
    toast.add({severity: 'error', summary: 'Error', detail: error.message, life: 3000});
  }
};
</script>

<style scoped>

</style>