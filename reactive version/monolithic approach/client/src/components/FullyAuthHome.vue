<template>
  <div class="flex flex-col items-center justify-center w-full gap-2">
    <div class="w-1/2">
      <h2 class="text-2xl font-bold mb-6 text-center">Your Calorie Consumption Overview.</h2>
      <p class="text-xl mb-4 leading-relaxed">
        The daily calorie consumption, tailored to the user's provided details, is set at
        {{ record.dailyCaloriesToConsume }}
        <img src="../assets/calorie.png" class="h-8 w-8 inline-block" alt="calorie_image">. Currently,
        you have consumed {{ record.dailyCaloriesConsumed }}
        <img src="../assets/calorie.png" class="h-8 w-8 inline-block" alt="calorie_image"> of your allotted calories. To
        meet your daily target, consider crafting a meal
        featuring.
        <Button label="Create Meal" class="w-1/9" @click="handleMealCreate"></Button>
      </p>
    </div>
    <Accordion class="w-1/2">
      <AccordionTab class="w-full" v-for="(meal, id) in meals" :key="id">
        <template #header>
          <span class="flex items-center gap-2 w-full justify-between">
            <span class="font-bold white-space-nowrap">{{ meal.name }}</span>
            <span class="flex items-center gap-2">
              <span class="text-sm">Calories: {{ meal.consumedCalories }}</span>
              <Avatar icon="pi pi-apple" style="background-color: #60C921FF; color: white" @click.stop="handleMealInsertFood(id)"></Avatar>
              <Avatar icon="pi pi-trash" style="background-color: #ff4d4d; color: white" @click.stop="handleMealDeletion(id)"></Avatar>
              <Avatar icon="pi pi-pencil" style="background-color: #4d94ff; color: white" @click.stop="handleMealEdit(id)"></Avatar>
            </span>
          </span>
        </template>
        <div class="overflow-auto max-h-40">
          <div class="food-item flex justify-between items-center p-2 border-b border-gray-300" v-for="(food, index) in meal.foods" :key="index">
            <span class="food-name">{{ food.name }}</span>
            <div class="flex gap-2 items-center">
              <span class="calorie-amount italic">{{ food.calorie.amount }} {{ food.calorie.unit }}</span>
              <Avatar icon="pi pi-pencil"
                      style="background-color: #4d94ff; color: white"
                      class="cursor-pointer"
                      @click.stop="handleChangeFoodById(meal.id , food.id)"></Avatar>
              <Avatar icon="pi pi-trash"
                      style="background-color: #ff4d4d; color: white"
                      class="cursor-pointer"
                      @click.stop="handleRemoveFoodById(meal.id , food.id)"></Avatar>
            </div>
          </div>
        </div>
      </AccordionTab>
    </Accordion>
  </div>
</template>

<script setup>
import {computed, ref} from 'vue';
import {useStore} from "vuex";
import router from "../router/index.js";

const store = useStore();
const record = computed(() => store.getters.record);

const meals = ref(store.getters.meals);

const handleMealCreate = () => {
  router.push({name: 'CreateMeal'});
};

const handleMealDeletion = (id) => {
  router.push({name: 'DeleteMeal', params: {id}});
};

const handleMealEdit = (id) => {
  router.push({name: 'EditMeal', params: {id}});
};

const handleMealInsertFood = (id) => {
  router.push({name: 'InsertFood', params: {id}});
};

const handleRemoveFoodById = (mealId, foodId) => {
  store.dispatch('removeFoodById', {mealId, foodId});
};

const handleChangeFoodById = (mealId, foodId) => {
  router.push({name: 'EditInsertedFood', params: {id: mealId, foodId}});
};
</script>
