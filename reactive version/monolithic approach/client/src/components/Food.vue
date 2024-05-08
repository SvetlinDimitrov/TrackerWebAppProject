<template>
  <div v-if="visible && food" class="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50 backdrop-blur">
    <div class="bg-white border border-gray-300 px-4 py-3 rounded relative w-1/3 h-1/2 m-auto">
      <div class="flex justify-between items-center mb-4">
        <h2 class="text-2xl font-bold">{{food.name}}</h2>
        <Avatar icon="pi pi-times" class="cursor-pointer" @click="$emit('close')"/>
      </div>
      <div class="flex justify-between items-center w-full">
        <img class="w-1/2 h-auto mb-8 border border-black" :src="foodInfo.picture"  alt="Food image">
        <Chart class="w-1/2 h-auto mb-8" type="pie" :data="chartData"></Chart>
      </div>
      <div class="flex justify-between items-center">
        <p>Main Serving: {{ food.mainServing.amount }} {{ food.mainServing.metric }}</p>
      </div>
      <div class="flex justify-between items-center">
        <p>Nutrients</p>
        <InputSwitch v-model="showNutrients" />
      </div>
      <div v-if="showNutrients">
        <div class="flex justify-between items-center" v-for="nutrient in food.nutritionList" :key="nutrient.name">
          <p>{{ nutrient.name }}: {{ nutrient.amount }} {{ nutrient.unit }}</p>
        </div>
      </div>
      <div class="flex justify-between items-center">
        <Button label="Submit" @click="$emit('submit')"></Button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';


const isLoading = ref(false);
const showNutrients = ref(false);
const props = defineProps({
  food: Object,
  visible: Boolean
})
const visible = ref(props.visible);
const food = ref(props.food);

console.log(food.value);

const chartData = computed(() => {
  const nutrients = food.value.nutrients || food.value.nutritionList;
  const protein = nutrients.find(n => n.name === 'Protein');
  const carbs = nutrients.find(n => n.name === 'Carbohydrate');
  const fat = nutrients.find(n => n.name === 'Fat');

  return {
    labels: ['Protein', 'Carbohydrate', 'Fat'],
    datasets: [
      {
        data: [protein ? protein.amount : 0, carbs ? carbs.amount : 0, fat ? fat.amount : 0],
        backgroundColor: ['#42A5F5', '#66BB6A', '#FFA726'],
      }
    ]
  };
});
const foodInfo = computed(() => {
  return food.value.additionalInfo || food.value.foodDetails || {};
});



</script>

<style scoped>

</style>