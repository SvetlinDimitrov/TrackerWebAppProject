<template>
  <div class="flex flex-col items-center justify-center h-screen">
    <div v-if="fullyAuth" class="flex flex-col items-center justify-center">

      <FullyAuthHome
          :record="record"
          :meals="meals"
          @handleMealCreate="handleMealCreate"
          @handleMealDeletion="handleMealDeletion"
          @handleMealEdit="handleMealEdit"
          @handleMealInsertFood="handleMealInsertFood"
          @handleRemoveFoodById="handleRemoveFoodById"
          @handleChangeFoodById="handleChangeFoodById"
          @handleCustomFood="handleCustomFood"
      />
    </div>

    <NoAuthHomePage
        v-else
        :user="user"
        :userDetailsComplete="userDetailsComplete"
    />
    <router-view></router-view>
  </div>
</template>

<script setup>
import {computed, ref} from 'vue';
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
const items = ref([
  {
    label: 'Add',
    icon: 'pi pi-pencil',
    command: () => {
      toast.add({ severity: 'info', summary: 'Add', detail: 'Data Added' });
    }
  },
  {
    label: 'Update',
    icon: 'pi pi-refresh',
    command: () => {
      toast.add({ severity: 'success', summary: 'Update', detail: 'Data Updated' });
    }
  },
  {
    label: 'Delete',
    icon: 'pi pi-trash',
    command: () => {
      toast.add({ severity: 'error', summary: 'Delete', detail: 'Data Deleted' });
    }
  },
  {
    label: 'Upload',
    icon: 'pi pi-upload',
    command: () => {
      router.push('/fileupload');
    }
  },
  {
    label: 'Vue Website',
    icon: 'pi pi-external-link',
    command: () => {
      window.location.href = 'https://vuejs.org/'
    }
  }
])
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

const handleCustomFood = (mealId) => {
  router.push({name: 'CustomInsertFood' , params: {id: mealId}});
};
</script>