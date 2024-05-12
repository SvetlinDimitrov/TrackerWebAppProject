<template>
  <div v-if="currentFood && originalFood"
       class="fixed inset-0 flex items-center justify-center backdrop-blur">
    <div class="bg-white border border-gray-300 px-4 py-3 rounded relative m-auto w-1/4 h-3/5 overflow-auto flex-col">
      <div class="flex justify-between items-center border-b border-gray-300 p-2">
        <div>
          <h2 class="text-2xl font-bold inline-block">{{ currentFood.name }}</h2>
        </div>
        <Avatar icon="pi pi-times" class="cursor-pointer" @click="$emit('close')"/>
      </div>
      <div class="flex justify-center items-center w-full border-b border-gray-300 p-2">
        <Chart class="w-1/2 h-auto" type="pie" :data="chartData"></Chart>
      </div>
      <div v-if="currentFood.calories" class="flex justify-between items-center border-b border-gray-300 p-2">
        <p>Calories</p>
        <p> {{ currentFood.calories.amount ? currentFood.calories.amount : 'NaN' }}
          {{ currentFood.calories.unit ? currentFood.calories.unit : 'NaN' }}</p></div>
      <div v-if="currentFood.mainServing" class="flex justify-between items-center border-b border-gray-300 p-2">
        <p>Serving Size</p>
        <Dropdown v-model="currentFood.mainServing"
                  :options="currentFood.otherServing"
                  :optionLabel="customLabel"
                  class="w-36 overflow-hidden"/>
      </div>
      <div v-if="currentFood.mainServing" class="flex justify-between items-center border-b border-gray-300 p-2">
        <p>Number of Servings</p>
        <InputNumber v-model="currentFood.mainServing.amount"
                     :minFractionDigits="2"
                     :min="0.01"
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
      <div v-if="currentFood.foodDetails" class="flex justify-between items-center border-b border-gray-300 p-2">
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
import {createNewNutrientArray} from "../utils/food.js";

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

if (currentFood.value.otherServing.find(serving =>
    serving.amount === currentFood.value.mainServing.amount &&
    serving.metric === currentFood.value.mainServing.metric &&
    serving.servingWeight === currentFood.value.mainServing.servingWeight) === undefined) {
  currentFood.value.otherServing.push(currentFood.value.mainServing);
}

watch(() => currentFood.value.mainServing, (newValue, oldValue) => {
  const ratio = newValue.servingWeight / oldValue.servingWeight;

  currentFood.value.calories.amount = originalFood.value.calories.amount;
  currentFood.value.calories.amount = (currentFood.value.calories.amount * ratio).toFixed(2);

  currentFood.value.nutrients = createNewNutrientArray(originalFood.value)
  currentFood.value.nutrients.forEach(nutrient => {
    nutrient.amount = (nutrient.amount * ratio).toFixed(2);
  });
});

watch(() => currentFood.value.mainServing.amount, (newValueSize, oldValueSize) => {
  const ratio = newValueSize / originalFood.value.mainServing.amount

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

const customLabel = (option) => {
  return `${option.metric} (${option.servingWeight} g)`;
};

const openImage = () => {
  window.open(currentFood.value.foodDetails?.picture ? currentFood.value.foodDetails.picture : "https://static.vecteezy.com/system/resources/previews/005/337/799/non_2x/icon-image-not-found-free-vector.jpg", '_blank');
};

</script>

