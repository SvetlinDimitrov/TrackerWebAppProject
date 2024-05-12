<template>
  <div class="flex flex-col items-center justify-center gap-3 h-[calc(100vh-60px)] overflow-hidden">
    <h1 class="text-2xl font-bold">Nutrition Fulfillment</h1>
    <div class="flex gap-20">
      <MainFulfillment :knobValue="Number(vitaminProgress)"
                       :show="showVitamins"
                       @toggle="handleVitaminToggle">Vitamins
      </MainFulfillment>
      <MainFulfillment :knobValue="Number(mineralProgress)"
                       :show="showMinerals"
                       @toggle="handleMineralToggle">Minerals
      </MainFulfillment>
      <MainFulfillment :knobValue="Number(macrosProgress)"
                       :show="showMacros"
                       @toggle="handleMacroToggle">Macros
      </MainFulfillment>
      <MainFulfillment :knobValue="Number(calorieProgress)"
                       :show="showCalories"
                       @toggle="handleCalorieToggle">Calories
      </MainFulfillment>
    </div>
    <h1 v-if="showCalories || showMacros || showMinerals || showVitamins" class="text-2xl font-bold">More
      Information</h1>

    <div v-if="showVitamins" class="flex flex-col overflow-auto w-1/3 h-1/2 p-2">
      <NutrientRowInfo v-for="(nutrient) in record.vitaminIntake"
                       :key="nutrient.name"
                       :consumed="nutrient.dailyConsumed"
                       :total="nutrient.recommendedIntake"
                       :unit="nutrient.measurement"
                       :foodList="mergeFoods(getNutrientAmountFromFood(allFoods , nutrient.name))"
                       :show-danger="false"
                       @handle-click-info="handleInfoRedirection(nutrient)"
                       @handle-click-danger="handleInfoRedirection(nutrient)">{{ nutrient.name }}

      </NutrientRowInfo>
    </div>
    <div v-if="showMinerals" class="flex flex-col overflow-auto w-1/3 h-1/2 p-2">
      <NutrientRowInfo v-for="(nutrient) in record.mineralIntakes"
                       :key="nutrient.name"
                       :consumed="nutrient.dailyConsumed"
                       :total="nutrient.recommendedIntake"
                       :unit="nutrient.measurement"
                       :foodList="getNutrientAmountFromFood(allFoods , nutrient.name)"
                       :show-danger="false"
                       @handle-click-info="handleInfoRedirection(nutrient)"
                       @handle-click-danger="handleInfoRedirection(nutrient)">{{ nutrient.name }}
      </NutrientRowInfo>
    </div>
    <div v-if="showMacros" class="flex flex-col overflow-auto w-1/3 h-1/2 p-2">
      <NutrientRowInfo v-for="(nutrient) in record.macroIntakes"
                       :key="nutrient.name"
                       :consumed="nutrient.dailyConsumed"
                       :total="nutrient.recommendedIntake"
                       :unit="nutrient.measurement"
                       :foodList="getNutrientAmountFromFood(allFoods , nutrient.name)"
                       :show-danger="false"
                       @handle-click-info="handleInfoRedirection(nutrient)"
                       @handle-click-danger="handleInfoRedirection(nutrient)">{{ nutrient.name }}
      </NutrientRowInfo>
    </div>
    <div v-if="showCalories" class="flex flex-col overflow-auto w-1/3 h-1/2 p-2">
      <NutrientRowInfo v-for="(meal , index) in meals"
                       :key="index"
                       :consumed="meal.consumedCalories"
                       :total="record.dailyCaloriesToConsume"
                       :foodList="[]"
                       :hideTag="true">{{ meal.name }}
      </NutrientRowInfo>
    </div>

  </div>
</template>

<script setup>
import MainFulfillment from "../../components/performance/MainFufilment.vue";
import {computed, ref} from "vue";
import {useStore} from "vuex";
import {
  calculateAveragePercentage,
  calculateAveragePercentageForArrayNutrients,
  getAllFoods,
  getNutrientAmountFromFood,
  mergeFoods
} from "../../utils/performance.js";
import NutrientRowInfo from "../../components/performance/NutrientRowInfo.vue";
import router from "../../router/index.js";

const store = useStore();
const showVitamins = ref(false);
const showMinerals = ref(false);
const showMacros = ref(false);
const showCalories = ref(false);
const record = computed(() => store.getters.record);
const meals = computed(() => store.getters.meals);
const allFoods = ref(getAllFoods(meals.value))
const vitaminProgress = ref(calculateAveragePercentageForArrayNutrients(record.value.vitaminIntake));
const mineralProgress = ref(calculateAveragePercentageForArrayNutrients(record.value.mineralIntakes));
const macrosProgress = ref(calculateAveragePercentageForArrayNutrients(record.value.macroIntakes));
const calorieProgress = ref(calculateAveragePercentage(record.value.dailyCaloriesConsumed, record.value.dailyCaloriesToConsume));

const resetToggles = () => {
  showVitamins.value = false;
  showMinerals.value = false;
  showMacros.value = false;
  showCalories.value = false;
}

const handleVitaminToggle = (show) => {
  resetToggles();
  showVitamins.value = show;
}

const handleMineralToggle = (show) => {
  resetToggles();
  showMinerals.value = show;
}

const handleMacroToggle = (show) => {
  resetToggles();
  showMacros.value = show;
}

const handleCalorieToggle = (show) => {
  resetToggles();
  showCalories.value = show;
}

const handleInfoRedirection = async (nutrient) => {
  await router.push({name: 'NutriInfo', params: {name: nutrient.name}});
}
</script>

<style scoped>

</style>