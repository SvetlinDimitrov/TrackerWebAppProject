<template>
  <div class="card flex flex-col items-center justify-center w-1/2 mx-auto">
    <Steps :model="items" class="mb-8" :activeStep="activeStep"/>
    <Register @registration-successful="handleRegistrationSuccessful"/>
  </div>
</template>

<script setup>
import { ref } from "vue";
import Register from "../components/Register.vue";

const items = ref([
  {
    label: 'Sign Up'
  },
  {
    label: 'More Info'
  },
  {
    label: 'Completed'
  }
]);

const activeStep = ref(0);

const handleRegistrationSuccessful = () => {
  // Move to the next step
  activeStep.value++;
};

</script>

<style scoped>
.invalid-input {
  border-color: red;
}
</style>
