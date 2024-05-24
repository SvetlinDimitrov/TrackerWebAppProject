<template>
  <CustomFoodBar
      v-if="foods"
      :foods="foods"
      @close="handleClose"
      @foodClick="handleFoodClick"
      @handleAddCustomFood="handleAddCustomFood"
      @editFood="handleEditFood"
      @deleteFood="handleDeleteFood"
  />
</template>

<script setup>
import {computed, onMounted, ref} from 'vue';
import router from "../../router/index.js";
import {useToast} from "primevue/usetoast"
import {useRoute} from "vue-router";
import {useStore} from "vuex";
import CustomFoodBar from "../../components/food/CustomFoodBar.vue";

const store = useStore();
const route = useRoute();
const toast = useToast();
const mealId = ref(route.params.id);
const foods = computed(() => store.getters.customFoods);


onMounted(async () => {
  if (!foods.value) {
    try {
      await store.dispatch('getCustomFoods');
    } catch (error) {
      toast.add({severity: 'error', summary: 'Error', detail: 'Failed to load custom foods'});
      await router.push({name: 'Home'});
    }
  }
});

const handleClose = () => {
  router.push({name: 'Home'});
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

  } catch (error) {
    toast.add({severity: 'error', summary: 'Error', detail: 'Failed to delete food'});
  }
};
</script>
