<template>
  <div v-if="currentFood && originalFood"
       class="fixed inset-0 flex items-center justify-center backdrop-blur">
    <div class="bg-white border border-gray-300 px-4 py-3 rounded m-auto w-1/4 max-h-3/5 overflow-auto flex-col">
      <div class="flex justify-between items-center border-b border-gray-300 p-2">
        <div>
          <h2 class="text-2xl font-bold inline-block">{{ currentFood.name }}</h2>
        </div>
        <Avatar icon="pi pi-times" class="cursor-pointer" @click="$emit('close')"/>
      </div>
      <div class="flex justify-center items-center w-full border-b border-gray-300 p-2">
        <PieChart class="w-1/3 h-auto" :chartData="chartData"></PieChart>
      </div>
      <div v-if="currentFood.calories" class="flex justify-between items-center border-b border-gray-300 p-2">
        <p>Calories</p>
        <p> {{ currentFood.calories.amount ? currentFood.calories.amount : 'NaN' }}
          {{ currentFood.calories.unit ? currentFood.calories.unit : 'NaN' }}</p></div>
      <div v-if="currentFood.mainServing" class="flex justify-between items-center border-b border-gray-300 p-2">
        <p>Serving Size</p>
        <Dropdown v-model="currentServing"
                  :options="allServingsAvailable"
                  :optionLabel="customLabel"
                  class="w-36 overflow-hidden"/>
      </div>
      <div v-if="currentFood.mainServing" class="flex justify-between items-center border-b border-gray-300 p-2">
        <p>Number of Servings</p>
        <InputNumber v-model="servingSize"
                     :minFractionDigits="2"
                     :min="0.01"
                     :step="0.01"
                     :max="10000"
                     class="w-36 overflow-hidden"/>
      </div>
      <div v-if="currentFood.nutrients && currentFood.nutrients.length !== 0"
           class="flex justify-between items-center border-b border-gray-300 p-2">
        <p>Nutrients</p>
        <InputSwitch v-model="showNutrients"/>
      </div>
      <div v-if="showNutrients">
        <div class="flex justify-between items-center border-2 border-gray-300 p-1"
             v-for="nutrient in currentFood.nutrients"
             :key="nutrient.name">
          <p>{{ nutrient.name }}</p>
          <p>{{ nutrient.amount }} {{ nutrient.unit }}</p>
        </div>
      </div>
      <div v-if="currentFood.foodDetails
      && (currentFood.foodDetails.info || currentFood.foodDetails.largeInfo || currentFood.foodDetails.picture)"
           class="flex justify-between items-center border-b border-gray-300 p-2">
        <p>More Details</p>
        <InputSwitch v-model="showMoreDetails"/>
      </div>
      <div v-if="showMoreDetails">
        <div v-if="currentFood.foodDetails.info" class="flex justify-between items-center border-2 border-gray-300 p-1">
          <p>Info</p>
          <div class="card flex justify-center">
            <Button label="Show" @click="showInfo = true"/>
            <Dialog v-model:visible="showInfo" modal header="Info" :style="{ width: '50vw' }"
                    :breakpoints="{ '1199px': '75vw', '575px': '90vw' }">
              <p class="m-0">
                {{ currentFood.foodDetails.info }}
              </p>
            </Dialog>
          </div>
        </div>
        <div v-if="currentFood.foodDetails.largeInfo"
             class="flex justify-between items-center border-2 border-gray-300 p-1">
          <p>More Info</p>
          <div class="card flex justify-center">
            <Button label="Show" @click="showMoreInfo = true"/>
            <Dialog v-model:visible="showMoreInfo" modal header="Even More Information" :style="{ width: '50vw' }"
                    :breakpoints="{ '1199px': '75vw', '575px': '90vw' }">
              <p class="m-0">
                {{ currentFood.foodDetails.largeInfo }}
              </p>
            </Dialog>
          </div>
        </div>
        <div v-if="currentFood.foodDetails.picture"
             class="flex justify-between items-center border-2 border-gray-300 p-1">
          <p>Picture</p>
          <Avatar icon="pi pi-image" class="cursor-pointer inline-block ml-2" @click="openImage"/>
        </div>
      </div>
      <div class="flex justify-center items-center p-2 flex-grow">
        <Button label="Submit" @click="$emit('submit' , currentFood)"></Button>
      </div>
    </div>
  </div>
</template>

<script setup>
import {computed, ref, watch} from 'vue';
import {createNewNutrientArray} from "../../utils/food.js";
import PieChart from "./PieChart.vue";

const showNutrients = ref(false);
const showMoreDetails = ref(false);
const showInfo = ref(false);
const showMoreInfo = ref(false);
const props = defineProps({
  food: Object,
  originalFood: Object
})
const currentFood = ref(props.food);
const originalFood = ref(props.originalFood);
const servingSize = ref(currentFood.value.mainServing.amount);
const allServingsAvailable = ref(currentFood.value.otherServing.map(serving => {
  return {
    metric: serving.metric,
    servingWeight: serving.servingWeight
  };
}) || []);
const currentServing = ref({
  metric: currentFood.value.mainServing.metric,
  servingWeight: currentFood.value.mainServing.servingWeight
});
let ensuresCurrentServingRefreshes = ref(0);

const currentServingExists = allServingsAvailable.value.find(serving =>
    serving.metric === currentFood.value.mainServing.metric &&
    serving.servingWeight === currentFood.value.mainServing.servingWeight
);

if (!currentServingExists) {
  allServingsAvailable.value.unshift({
    metric: currentFood.value.mainServing.metric,
    servingWeight: currentFood.value.mainServing.servingWeight
  });
}


watch(() => currentServing.value, (newValue, oldValue) => {
  const newAmount = currentFood.value.otherServing.find(serving =>
      serving.metric === newValue.metric &&
      serving.servingWeight === newValue.servingWeight).amount;

  ensuresCurrentServingRefreshes.value += 1;
  servingSize.value = newAmount;

  currentFood.value.mainServing.metric = newValue.metric;
  currentFood.value.mainServing.servingWeight = newValue.servingWeight;
  currentFood.value.mainServing.amount = newAmount;

}, {flush: 'sync'});

watch(() => [servingSize.value, ensuresCurrentServingRefreshes.value], () => {

  const ratio = (servingSize.value * currentServing.value.servingWeight)
      / (originalFood.value.mainServing.amount * originalFood.value.mainServing.servingWeight);

  currentFood.value.mainServing.amount = servingSize.value;

  currentFood.value.calories.amount = originalFood.value.calories.amount;
  currentFood.value.calories.amount = (currentFood.value.calories.amount * ratio).toFixed(2);

  currentFood.value.nutrients = createNewNutrientArray(originalFood.value)
  currentFood.value.nutrients.forEach(nutrient => {
    nutrient.amount = (nutrient.amount * ratio).toFixed(2);
  });
});

const chartData = computed(() => {
  const protein = currentFood.value.nutrients.find(n => n.name === 'Protein');
  const carbs = currentFood.value.nutrients.find(n => n.name === 'Carbohydrate');
  const fat = currentFood.value.nutrients.find(n => n.name === 'Fat');

  let proteinAmount = protein ? protein.amount : 0;
  let carbsAmount = carbs ? carbs.amount : 0;
  let fatAmount = fat ? fat.amount : 0;

  // If all values are zero, add a small non-zero value to each one
  if (proteinAmount === 0 && carbsAmount === 0 && fatAmount === 0) {
    proteinAmount = 0.1;
    carbsAmount = 0.1;
    fatAmount = 0.1;
  }

  return {
    labels: ['Protein', 'Carbohydrate', 'Fat'],
    datasets: [
      {
        data: [proteinAmount, carbsAmount, fatAmount],
        backgroundColor: ['#42A5F5', '#66BB6A', '#FFA726'],
      }
    ]
  };
});

const customLabel = (option) => {
  return `${option.metric} (${option.servingWeight} g)`;
};

const openImage = () => {
  window.open(currentFood.value.foodDetails?.picture ? currentFood.value.foodDetails.picture : "https://static.vecteezy.com/system/resources/previews/005/337/799/non_2x/icon-image-not-found-free-vector.jpg", '_blank');
};

</script>

