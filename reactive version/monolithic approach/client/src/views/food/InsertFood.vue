<template>
  <SearchBar
      :search="search"
      :foods="foods"
      @close="handleClose"
      @search="handleSearch"
      @foodClick="handleFoodClick"
      @update:search="search = $event"
  />
</template>

<script setup>
import {onMounted, ref} from 'vue';
import router from "../../router/index.js";
import {useToast} from "primevue/usetoast"
import {useRoute} from "vue-router";
import {useStore} from "vuex";
import SearchBar from "../../components/food/SearchBar.vue";

const store = useStore();
const route = useRoute();
const toast = useToast();
const mealId = ref(route.params.id);
const search = ref(store.getters.searchedFoodWord);
const foods = ref(store.getters.searchedFoodResult);

onMounted(async () => {
  const currentMeal = store.getters.meals[mealId.value];

  if (!currentMeal) {
    toast.add({severity: 'error', summary: 'Error', detail: 'no meal found', life: 3000});
    await router.push({name: 'Home'});
  }
});

const handleClose = () => {
  router.push({name: 'Home'});
};

const handleSearch = async () => {
  if (search.value.trim().length < 2) {
    toast.add({severity: 'info', summary: 'Info', detail: 'Please enter at least 2 words', life: 3000});
    return;
  }
  try{
    const data = await store.dispatch("getFoodsBySearchName" , search.value);
    foods.value = [];
    data.forEach((food) => {
      foods.value.push(food);
    });

    if(foods.value.length === 0){
      toast.add({severity: 'info', summary: 'Info', detail: 'No food found', life: 3000});
    }else{
      toast.add({severity: 'success', summary: 'Success', detail: 'Search Successful', life: 3000});
    }
  }catch (error){
    toast.add({severity: 'error', summary: 'Error', detail: error.message, life: 3000});
  }
};

const handleFoodClick = (food) => {
  if(food.id){
    router.push({name: 'BrandedFood', params: {id: mealId.value, foodId: food.id}});
  }else{
    router.push({name: 'CommonFood', params: {id: mealId.value, name: food.name}});
  }
};
</script>

<style scoped>
</style>