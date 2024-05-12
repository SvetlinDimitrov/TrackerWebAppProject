<template>
  <div class="fixed inset-0 flex items-center justify-center backdrop-blur">
    <div class="bg-white border border-gray-300 px-4 py-3 rounded w-1/2 h-1/2">
      <div class="flex justify-between items-center mb-4">
        <h2 class="text-2xl font-bold">Search for food</h2>
        <Avatar icon="pi pi-times" class="cursor-pointer" @click="handleClose"/>
      </div>
      <div class="flex items-center gap-2 mb-4">
        <InputText v-model="search" class="flex-grow" placeholder="Example: Apple"/>
        <Button label="Search" @click="handleSearch"/>
      </div>

      <div class="relative h-3/4 overflow-auto border border-gray-300 rounded p-2">
        <ul>
          <li v-for="(food, index) in foods" :key="food.name + index"
              class="p-1 mb-2 cursor-pointer hover:bg-gray-200 transition-colors duration-200 border-b border-gray-200 text-lg font-semibold"
              @click="handleFoodClick(food)">{{ food.name }}
            <span v-if="food.brand">({{ food.brand }})</span>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script setup>
import {onMounted, ref} from 'vue';
import router from "../../router/index.js";
import {useToast} from "primevue/usetoast"
import {useRoute} from "vue-router";
import {useStore} from "vuex";

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