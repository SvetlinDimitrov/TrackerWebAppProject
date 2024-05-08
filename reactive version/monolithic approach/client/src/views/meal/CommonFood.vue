<template>
  <Toast/>
  <Food v-if="food"
        :visible="visible"
        :food="food"
        @close="handleClose"
        @submit="handleSubmit"/>
</template>

<script setup>
import {onMounted, ref} from 'vue';
import router from "../../router/index.js";
import {useToast} from "primevue/usetoast"
import {useRoute} from "vue-router";
import Food from "../../components/Food.vue";
import {getCommonFoodByName} from "../../api/FoodSeachService.js";

const route = useRoute();
const toast = useToast();
const mealId = ref(route.params.id);
const foodName = ref(route.params.name);
const visible = ref(true);
const food = ref(null);

onMounted(async () => {
  try {
    const data = await getCommonFoodByName(foodName.value);
    food.value = data[0];

    console.log(food.value);
  } catch (error) {
    toast.add({severity: 'error', summary: 'Error', detail: error.message, life: 3000});
  }

});
const handleClose = () => {
  router.push({name: 'InsertFood', params: {mealId: mealId.value}});
  visible.value = false;
};

const handleSubmit = () => {
  console.log("Yess");
};
</script>

<style scoped>

</style>