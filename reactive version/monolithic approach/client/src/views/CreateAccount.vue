<template>
  <div class="card flex flex-col items-center justify-center w-1/2 mx-auto">
    <Steps :model="items" class="mb-8" :activeStep="activeStep"/>
    <Register v-if="activeStep === 0" @registration-successful="handleNextStepOnSuccess"/>
    <Edit :showSecondButton="true" v-if="activeStep === 1" @edit-skip-successful="handleNextStepOnSuccess"/>
    <Completion v-if="activeStep === 2"/>
  </div>
</template>

<script setup>
import { ref } from "vue";
import Register from "../components/Register.vue";
import Edit from "../components/Edit.vue";
import Completion from "../components/Completion.vue";

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

const handleNextStepOnSuccess = () => {
  // Move to the next step
  activeStep.value++;
};

</script>

<style scoped>
.invalid-input {
  border-color: red;
}
</style>
