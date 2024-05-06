<template>
  <div class="card flex flex-col items-center justify-center w-1/2 mx-auto">
    <Edit v-if="userData"
          :kilograms="userData.kilograms ? userData.kilograms : null"
          :height="userData.height ? userData.height : null"
          :age="userData.age ? userData.age : null"
          :workoutState="userData.workoutState ? userData.workoutState : null"
          :gender="userData.gender ? userData.gender : null"
          :showSecondButton=false
          @edit-skip-successful="handleSuccessfulEdit"/>
  </div>
</template>

<script setup>
import Edit from '../components/Edit.vue'
import router from "../router/index.js";
import {onMounted, ref} from "vue";
import {getUserDetails} from "../api/UserService.js";

const userData = ref(null);

onMounted(async () => {
  userData.value = await getUserDetails();
});

const handleSuccessfulEdit = () => {
  router.push({name: 'Home'});
};
</script>

<style scoped>

</style>