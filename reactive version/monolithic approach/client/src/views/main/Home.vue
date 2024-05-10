<template>
  <div class="flex flex-col items-center justify-center p-8">
    <FullyAuthHome
        v-if="fullyAuth"
        :record="record"
        :meals="meals"
        @handleMealCreate="handleMealCreate"
        @handleMealDeletion="handleMealDeletion"
        @handleMealEdit="handleMealEdit"
        @handleMealInsertFood="handleMealInsertFood"
        @handleRemoveFoodById="handleRemoveFoodById"
        @handleChangeFoodById="handleChangeFoodById"
    />
    <NoAuthHomePage
        v-else
        :user="user"
        :userDetailsComplete="userDetailsComplete"
    />
    <router-view></router-view>
  </div>
</template>

<script setup>
import {computed} from 'vue';
import {useStore} from 'vuex';
import NoAuthHomePage from "../../components/NoAuthHomePage.vue";
import FullyAuthHome from "../../components/FullyAuthHome.vue";
import router from "../../router/index.js";

const store = useStore();
const fullyAuth = computed(() => store.getters.isFullyRegistered);
const record = computed(() => store.getters.record);
const meals = computed(() => store.getters.meals);
const user = computed(() => store.getters.user);
const userDetailsComplete = computed(() => store.getters.isFullyRegistered);

const handleMealCreate = () => {
  router.push({name: 'CreateMeal'});
};

const handleMealDeletion = (id) => {
  if (window.confirm("Do you want to delete this meal?")) {
    router.push({name: 'DeleteMeal', params: {id}});
  }
};

const handleMealEdit = (id) => {
  router.push({name: 'EditMeal', params: {id}});
};

const handleMealInsertFood = (id) => {
  router.push({name: 'InsertFood', params: {id}});
};

const handleRemoveFoodById = (mealId, foodId) => {
  if (window.confirm("Do you want to delete this food?")) {
    router.push({name: 'RemoveFoodById', params: {id: mealId, foodId}})
  }
};

const handleChangeFoodById = (mealId, foodId) => {
  router.push({name: 'EditInsertedFood', params: {id: mealId, foodId}});
};
</script>