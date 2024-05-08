<template>
  <Toast/>
  <div v-if="visible" class="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50 backdrop-blur">
    <div class="bg-white border border-gray-300 px-4 py-3 rounded relative w-1/2 h-1/2 m-auto">
      <div class="flex justify-between items-center mb-4">
        <h2 class="text-2xl font-bold">Search for food</h2>
        <Avatar icon="pi pi-times" class="cursor-pointer" @click="handleClose"/>
      </div>
      <div class="flex items-center gap-2 mb-4">
        <InputText v-model="search" class="flex-grow" placeholder="Example: Apple"/>
        <Button label="Search" @click="handleSearch"/>
      </div>

      <div  class="relative h-3/4 overflow-auto border border-gray-300 rounded p-2">
        <ProgressSpinner v-if="isLoading" class="absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2"/>
        <ul v-else>
          <li v-for="(food, index) in foods" :key="index"
              class="p-1 mb-2 cursor-pointer hover:bg-gray-200 transition-colors duration-200 border-b border-gray-200 text-lg font-semibold"
              @click="handleFoodClick(food)">{{ food.name }} <span v-if="food.brand">({{ food.brand }})</span></li>
        </ul>
      </div>
    </div>
  </div>
  <router-view></router-view>
</template>

<script setup>
import {ref} from 'vue';
import router from "../../router/index.js";
import {getAllFoodsBySearchWord} from "../../api/FoodSeachService.js";
import {useToast} from "primevue/usetoast"
import {useRoute} from "vue-router";

const route = useRoute();
const toast = useToast();
const mealId = ref(route.params.id);
const isLoading = ref(false);
const visible = ref(true);
const search = ref('');
const foods = ref([]);

const handleClose = () => {
  router.push({name: 'Home'});
  visible.value = false;
};

const handleSearch = async () => {
  foods.value=[];
  isLoading.value = true;

  if (search.value.trim().length < 2) {
    toast.add({severity: 'info', summary: 'Info', detail: 'Please enter at least 2 words', life: 3000});
    isLoading.value = false;
    return;
  }

  try{
    const data = await getAllFoodsBySearchWord(search.value);
    const commonFoods = data.common.map(food => ({
      name: food.food_name,
    }));
    const brandedFoods = data.branded.map(food => ({
      name: food.food_name,
      brand: food.brand_name,
      id: food.nix_item_id,
    }));
    foods.value = [...commonFoods, ...brandedFoods];
    if(foods.value.length === 0){
      toast.add({severity: 'info', summary: 'Info', detail: 'No food found', life: 3000});
    }else{
      toast.add({severity: 'success', summary: 'Success', detail: 'Search Successful', life: 3000});
    }
  }catch (error){
    toast.add({severity: 'error', summary: 'Error', detail: error.message, life: 3000});
  }
  isLoading.value = false;
};

const handleFoodClick = (food) => {
  if(food.id){
    router.push({name: 'BrandedFood', params: {mealId: mealId.value, foodId: food.id}});
  }else{
    router.push({name: 'CommonFood', params: {mealId: mealId.value, name: food.name}});
  }
};
</script>

<style scoped>
</style>