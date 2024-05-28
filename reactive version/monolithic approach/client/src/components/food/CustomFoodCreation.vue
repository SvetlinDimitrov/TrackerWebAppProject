<template>
  <div v-if="visible && form"
       class="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-50 backdrop-blur">
    <div class="bg-white rounded-lg w-96 p-6">
      <h2 class="text-xl font-bold mb-4">{{ header }}</h2>
      <div class="space-y-4 flex flex-col border-t border-b p-3">
        <InputText
            id="name"
            v-model="form.name"
            class="border p-2 rounded"
            placeholder="Name of the food : Chicken, Rice, ..."
        />
        <InputNumber
            v-model="form.calories"
            inputId="amount"
            :min="0.1"
            :max="10000"
            :minFractionDigits="1"
            :step="0.1"
            placeholder="Calories in kcal : 159 , 324"
        />
        <Dialog v-model:visible="visibleServing" modal header="Serving">
          <div class="flex flex-col">
            <label for="amount" class="mb-2">Amount</label>
            <InputNumber
                v-model="form.mainServing.amount"
                inputId="amount"
                :min="1"
                :max="1000"
                placeholder="amount of serving in grams : 1, 2, 3, ..."
            />
          </div>
          <div class="flex flex-col">
            <label for="weight" class="mb-2">Weight</label>
            <InputNumber
                v-model="form.mainServing.servingWeight"
                inputId="weight"
                :minFractionDigits="1"
                :min="0.1"
                :max="10000"
                :step="0.1"
                placeholder="weight of serving : 100, 200, 300, ..."
            />

          </div>
          <div class="flex flex-col">
            <label for="metric" class="mb-2">Metric</label>
            <InputText
                id="metric"
                v-model="form.mainServing.metric"
                class="border p-2 rounded"
                minlength="1"
                maxlength="10"
                placeholder="metric : grams, ml, ..."
            />
          </div>
          <div class="flex justify-end gap-2 mt-2">
            <Button type="button" label="Cancel" severity="secondary" @click="closeMainServing"></Button>
            <Button type="button" label="Save" @click="saveMainServing"></Button>
          </div>
        </Dialog>
        <Button type="button"
                @click="visibleServing = !visibleServing"
                :severity="form.mainServing.amount && form.mainServing.servingWeight && form.mainServing.metric
                ? 'warning' : 'success'"
                class="w-1/2 m-auto"
                :label="form.mainServing.amount && form.mainServing.servingWeight && form.mainServing.metric
                ? 'Change Serving' : 'Add Serving'">
        </Button>
        <span v-if="form.mainServing.servingWeight && form.mainServing.amount && form.mainServing.metric"
              class="inline-block p-1 hover:bg-gray-200 text-sm font-semibold">
          amount: {{ form.mainServing.amount }}
          metric: {{ form.mainServing.metric }} - (weight: {{ form.mainServing.servingWeight }}) </span>
        <Dialog v-model:visible="visibleInfo" modal header="Information for the food">
          <div class="flex flex-col">
            <label for="info" class="mb-2">Short info</label>
            <InputText
                v-model="form.foodDetails.info"
                inputId="info"
                maxlength="250"
                placeholder="eg: supper high in protein"
            />
          </div>
          <div class="flex flex-col">
            <label for="large info" class="mb-2">Large info</label>
            <InputText
                v-model="form.foodDetails.largeInfo"
                maxlength="50000"
                inputId="large info"
                placeholder="eg: ingredients, how it was cooked, ..."
            />
          </div>
          <div class="flex flex-col">
            <label for="picture" class="mb-2">Picture</label>
            <InputText
                id="picture"
                v-model="form.foodDetails.picture"
                placeholder="url to picture"
            />
          </div>
          <div class="flex justify-end gap-2 mt-2">
            <Button type="button" label="Cancel" severity="secondary" @click="visibleInfo = false"></Button>
            <Button type="button" label="Save" @click="visibleInfo = false"></Button>
          </div>
        </Dialog>
        <Button type="button"
                @click="visibleInfo = !visibleInfo"
                :severity="form.foodDetails.info && form.foodDetails.largeInfo && form.foodDetails.picture
                ? 'warning' : 'success'"
                class="w-1/2 m-auto"
                :label="form.foodDetails.info && form.foodDetails.largeInfo && form.foodDetails.picture
                ? 'Change Info' : 'Add Info'">
        </Button>
        <Dialog v-model:visible="visibleNutrients" modal header="Add nutrient">
          <div class="flex flex-col">
            <Dropdown v-model="form.nutrientCreation.name"
                      :options="nutrientsAvailable"
                      placeholder="Select a Nutrient"/>
          </div>
          <div class="flex flex-col">
            <label for="picture" class="mb-2">Amount</label>
            <InputNumber
                v-model="form.nutrientCreation.amount"
                :min="1"
                :max="10000"
                placeholder="amount of nutrient , eg: 23 "
            />
          </div>
          <span v-if="form.nutrientCreation.name">The measure unit is: {{
              nutrientsUnits[form.nutrientCreation.name]
            }}</span>
          <div class="flex justify-end gap-2 mt-2">
            <Button type="button" label="Cancel" severity="secondary" @click="cancelNutrient"></Button>
            <Button type="button" label="Save" @click="addNutrient"></Button>
          </div>
        </Dialog>
        <Button type="button"
                @click="visibleNutrients = !visibleNutrients"
                severity="success"
                class="w-1/2 m-auto"
                label="Add Nutrient">
        </Button>
        <div v-if="form.nutrients.length" class="max-h-[120px] overflow-auto border border-gray-500 rounded p-1">
          <div class="food-item flex justify-between items-center p-1 border-b border-gray-300"
               v-for="(nutrient, index) in form.nutrients" :key="index">
            <span class="food-name">{{ nutrient.name }}</span>
            <div class="flex gap-2 items-center">
              <span class="calorie-amount italic">{{ nutrient.amount }} {{ nutrient.unit }}</span>
              <Avatar icon="pi pi-trash"
                      style="background-color: #ff4d4d; color: white"
                      class="cursor-pointer"
                      @click.stop="removeNutrient(index)"></Avatar>
            </div>
          </div>
        </div>
        <Dialog v-model:visible="visibleOtherServing" modal header="Additional Servings">
          <div class="flex flex-col">
            <label for="amount" class="mb-2">Amount</label>
            <InputNumber
                v-model="form.additionalServingCreation.amount"
                inputId="amount"
                :min="1"
                :max="1000"
                placeholder="amount of serving in grams : 1, 2, 3, ..."
            />
          </div>
          <div class="flex flex-col">
            <label for="weight" class="mb-2">Weight</label>
            <InputNumber
                v-model="form.additionalServingCreation.servingWeight"
                inputId="weight"
                :minFractionDigits="1"
                :min="0.1"
                :max="10000"
                :step="0.1"
                placeholder="weight of serving : 100, 200, 300, ..."
            />

          </div>
          <div class="flex flex-col">
            <label for="metric" class="mb-2">Metric</label>
            <InputText
                id="metric"
                v-model="form.additionalServingCreation.metric"
                class="border p-2 rounded"
                minlength="1"
                maxlength="10"
                placeholder="metric : grams, ml, ..."
            />
          </div>
          <div class="flex justify-end gap-2 mt-2">
            <Button type="button" label="Cancel" severity="secondary" @click="closeOtherServing"></Button>
            <Button type="button" label="Save" @click="saveOtherServing"></Button>
          </div>
        </Dialog>
        <Button type="button"
                @click="visibleOtherServing = !visibleOtherServing"
                severity="success"
                class="w-1/2 m-auto"
                label="Add More Servings">
        </Button>
        <div v-if="form.otherServing.length" class="max-h-[120px] overflow-auto border border-gray-500 rounded p-1">
          <div class="food-item flex justify-between items-center p-1 border-b border-gray-300"
               v-for="(serving, index) in form.otherServing" :key="index">
            <span class="inline-block p-1 hover:bg-gray-200 text-sm font-semibold">
                amount: {{ serving.amount }} metric: {{ serving.metric }} - (weight: {{ serving.servingWeight }})
            </span>
            <div class="flex gap-2 items-center">
              <Avatar icon="pi pi-trash"
                      style="background-color: #ff4d4d; color: white"
                      class="cursor-pointer"
                      @click.stop="removeOtherServing(index)"></Avatar>
            </div>
          </div>
        </div>
      </div>
      <div class="flex justify-end gap-2 mt-2">
        <Button type="submit" label="Submit" security="success" @click="submit"/>
        <Button type="button" label="Cancel" severity="secondary" @click="handleClose"></Button>
      </div>
    </div>
  </div>
</template>

<script setup>
import {ref, toRaw} from "vue";
import {useToast} from "primevue/usetoast"
import {macro, minerals, vitamins} from "../../utils/nutrition/avaibleNutritions.js";
import {nutritionUnits} from "../../utils/nutrition/nutritionMesureUnit.js";

const toast = useToast();

const props = defineProps({
  food: Object,
  header: String
});

const form = ref({
  name: props.food?.name ? props.food.name : null,
  calories: props.food?.calories ? props.food.calories.amount : null,
  mainServing: {
    amount: props.food?.mainServing ? props.food.mainServing.amount : null,
    servingWeight: props.food?.mainServing ? props.food.mainServing.servingWeight : null,
    metric: props.food?.mainServing ? props.food.mainServing.metric : null,
  },
  foodDetails: {
    info: props.food?.foodDetails ? props.food.foodDetails.info : null,
    largeInfo: props.food?.foodDetails ? props.food.foodDetails.largeInfo : null,
    picture: props.food?.foodDetails ? props.food.foodDetails.picture : null,
  },
  nutrientCreation: {
    name: null,
    amount: null,
  },
  additionalServingCreation: {
    amount: null,
    servingWeight: null,
    metric: null,
  },
  otherServing: props.food?.otherServing ? props.food.otherServing : [],
  nutrients: props.food?.nutrients ? props.food.nutrients : [],
});
const visible = ref(true);
const visibleServing = ref(false);
const visibleInfo = ref(false);
const visibleNutrients = ref(false);
const visibleOtherServing = ref(false);
const header = ref(props.header);
const nutrientsAvailable = ref([...macro, ...vitamins, ...minerals]);
const nutrientsUnits = ref(nutritionUnits);

const emits = defineEmits(['close', 'submit']);

const addNutrient = () => {
  if (form.value.nutrientCreation.name && form.value.nutrientCreation.amount) {
    const existingNutrientIndex = form.value.nutrients.findIndex(nutrient => nutrient.name === form.value.nutrientCreation.name);
    if (existingNutrientIndex !== -1) {
      // If nutrient with the given name already exists, update its amount and unit
      form.value.nutrients[existingNutrientIndex].amount = form.value.nutrientCreation.amount;
      form.value.nutrients[existingNutrientIndex].unit = nutrientsUnits.value[form.value.nutrientCreation.name];
    } else {
      // If nutrient with the given name doesn't exist, add a new nutrient
      form.value.nutrients.push({
        name: form.value.nutrientCreation.name,
        amount: form.value.nutrientCreation.amount,
        unit: nutrientsUnits.value[form.value.nutrientCreation.name]
      });
    }
    form.value.nutrientCreation.name = null;
    form.value.nutrientCreation.amount = null;
    visibleNutrients.value = false;
  } else {
    toast.add({severity: 'error', summary: 'Error', detail: 'Please fill in the nutrient name and amount'});
  }
};
const removeNutrient = (index) => {
  form.value.nutrients.splice(index, 1);
};
const cancelNutrient = () => {
  form.value.nutrientCreation.name = null;
  form.value.nutrientCreation.amount = null;
  visibleNutrients.value = false;
};

const saveMainServing = () => {
  if (form.value.mainServing.amount && form.value.mainServing.servingWeight && form.value.mainServing.metric) {
    visibleServing.value = false;
  } else {
    toast.add({severity: 'error', summary: 'Error', detail: 'Please fill in the amount, weight and metric'});
  }
};
const closeMainServing = () => {
  form.value.mainServing.amount = null;
  form.value.mainServing.servingWeight = null;
  form.value.mainServing.metric = null;
  visibleServing.value = false;
};

const saveOtherServing = () => {
  if (form.value.additionalServingCreation.amount && form.value.additionalServingCreation.servingWeight && form.value.additionalServingCreation.metric) {
    form.value.otherServing.push({
      amount: form.value.additionalServingCreation.amount,
      servingWeight: form.value.additionalServingCreation.servingWeight,
      metric: form.value.additionalServingCreation.metric
    });
    form.value.additionalServingCreation.amount = null;
    form.value.additionalServingCreation.servingWeight = null;
    form.value.additionalServingCreation.metric = null;
    visibleOtherServing.value = false;
  } else {
    toast.add({severity: 'error', summary: 'Error', detail: 'Please fill in the amount, weight and metric'});
  }
};
const removeOtherServing = (index) => {
  form.value.otherServing.splice(index, 1);
};
const closeOtherServing = () => {
  form.value.additionalServingCreation.amount = null;
  form.value.additionalServingCreation.weight = null;
  form.value.additionalServingCreation.metric = null;
  visibleOtherServing.value = false;
};

const submit = async () => {
  if (!form.value.name || form.value.name.length < 2) {
    toast.add({severity: 'error', summary: 'Error', detail: 'Please fill in the name of the food'});
    return;
  }
  if (!form.value.mainServing.amount || !form.value.mainServing.servingWeight || !form.value.mainServing.metric) {
    toast.add({severity: 'error', summary: 'Error', detail: 'Please fill in the main serving'});
    return;
  }
  if (!form.value.calories || form.value.calories < 0) {
    toast.add({severity: 'error', summary: 'Error', detail: 'Please fill in the calories'});
    return;
  }

  const foodToCreate = {
    name: form.value.name,
    calories: {
      amount: form.value.calories,
      unit: "kcal"
    },
    mainServing: {
      amount: form.value.mainServing.amount,
      servingWeight: form.value.mainServing.servingWeight,
      metric: form.value.mainServing.metric
    },
    foodDetails: {
      info: form.value.foodDetails.info,
      largeInfo: form.value.foodDetails.largeInfo,
      picture: form.value.foodDetails.picture
    },
    nutrients: toRaw(form.value.nutrients),
    otherServing: toRaw(form.value.otherServing)
  };

  emits('submit', foodToCreate);
};

const handleClose = () => {
  visible.value = false;
  emits('close');
};
</script>