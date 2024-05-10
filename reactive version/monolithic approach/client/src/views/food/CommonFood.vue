<template>
  <Food v-if="food"
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
import {getCommonFoodByName} from "../../api/FoodSeachService.js";

const store = useStore();
const route = useRoute();
const toast = useToast();
const mealId = ref(route.params.id);
const foodName = ref(route.params.name);
const food = ref(null);

onMounted(async () => {
  try {
    const data = await store.dispatch("getCommonFoodByName" , foodName.value);
    food.value = data[0];
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
  try{
    await store.dispatch("addFoodIntoMeal" , payload);
    toast.add({severity: 'success', summary: 'Success', detail: 'Food added successfully', life: 3000});
    await router.push({name: 'Home'});
  } catch (error) {
    toast.add({severity: 'error', summary: 'Error', detail: error.message, life: 3000});
  }
};
</script>
