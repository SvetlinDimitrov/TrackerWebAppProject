<template>
  <CustomFoodBar
      v-if="foods"
      :foods="foods"
      @close="handleClose"
      @foodClick="handleFoodClick"
      @handleAddCustomFood="handleAddCustomFood"
      @editFood="handleEditFood"
      @deleteFood="handleDeleteFood"
      @navigate="navigation"
  />
</template>

<script setup>
import {computed, ref, watchEffect} from 'vue';
import router from "../../router/index.js";
import {useToast} from "primevue/usetoast"
import {useRoute} from "vue-router";
import {useStore} from "vuex";
import CustomFoodBar from "../../components/food/CustomFoodBar.vue";

const store = useStore();
const route = useRoute();
const toast = useToast();
const mealId = ref(route.params.id);
const foods = ref([]);
const page = computed(() => parseInt(route.query.page) || 0);
const totalPages = computed(() => store.getters.totalPagesCustomFood);
const deletionRefresher = ref(false);

watchEffect(async () => {
  try {
    deletionRefresher.value
    const payload = {page: page.value ? page.value : 0, size: 6};
    foods.value = await store.dispatch('getCustomFoods', payload);
  } catch (error) {
    toast.add({severity: 'error', summary: 'Error', detail: 'Failed to load custom foods'});
    await router.push({name: 'Home'});
  }

});

const handleClose = () => {
  router.push({name: 'Home'});
};

const navigation = (direction) => {
  let newPage = page.value;
  if (direction === 'left') {
    newPage = page.value - 1;
    if (newPage < 0) {
      return; // Do nothing if the new page is less than 0
    }
  } else if (direction === 'right') {
    newPage = page.value + 1;
    if (newPage >= totalPages.value) {
      return; // Do nothing if the new page is greater than or equal to the total pages
    }
  }
  router.push({name: 'CustomInsertFood', params: {id: mealId.value}, query: {page: newPage}});
};

const handleAddCustomFood = () => {
  router.push({name: 'CreateCustomFood'});
};

const handleFoodClick = (food) => {
  router.push({name: 'CustomFood', params: {id: mealId.value, foodId: food.id}});
};

const handleEditFood = (food) => {
  router.push({name: 'EditCustomFood', params: {foodId: food.id}});
};

const handleDeleteFood = async (food) => {
  try {
    await store.dispatch('deleteCustomFoodById', food.id);
    toast.add({severity: 'success', summary: 'Success', detail: 'Food deleted successfully'});
    deletionRefresher.value = !deletionRefresher.value;
    await router.push({name: 'CustomInsertFood', params: {id: mealId.value}});
  } catch (error) {
    toast.add({severity: 'error', summary: 'Error', detail: 'Failed to delete food'});
  }
};
</script>
