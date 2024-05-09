<template>
  <Toast/>
  <Food v-if="food"
        :visible="visible"
        :food="food"
        @close="handleClose"
        @submit="handleSubmit"/>
</template>

<script setup>
import {onMounted, ref} from 'vue';
import router from "../../router/index.js";
import {useToast} from "primevue/usetoast"
import {useStore} from "vuex";
import {useRoute} from "vue-router";
import Food from "../../components/Food.vue";

const store = useStore();
const route = useRoute();
const toast = useToast();
const mealId = ref(route.params.id);
const foodId = ref(route.params.foodId);
const visible = ref(true);
const food = ref(null);

onMounted(() => {
  try{
    const currentMeal = store.getters.meals[mealId.value];

    currentMeal.foods.forEach((f) => {
      if (f.id === foodId.value) {
        food.value = f;
      }
    });

    if(food.value === null) {
      throw new Error("no food found");
    }
  }catch (error) {
    toast.add({severity: 'error', summary: 'Error', detail: error.message, life: 3000});
    router.push({name: 'Home'});
  }
});

const handleClose = () => {
  router.push({name: 'Home'});
  visible.value = false;
};

const handleSubmit = async (food) => {
  try {
    await store.dispatch("changeFoodById", {mealId: mealId.value, foodId: foodId.value, food});
    toast.add({severity: 'success', summary: 'Success', detail: 'Food changed successfully', life: 3000});
    await router.push({name: 'Home'});
  } catch (error) {
    toast.add({severity: 'error', summary: 'Error', detail: error.message, life: 3000});
  }
  visible.value = false;
};
</script>

<style scoped>

</style>