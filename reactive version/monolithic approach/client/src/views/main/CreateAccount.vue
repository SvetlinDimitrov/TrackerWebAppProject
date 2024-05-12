<template>
  <div class="card flex flex-col items-center justify-center w-1/2 mx-auto h-screen">
    <Steps :model="items" class="mb-8" :activeStep="activeStep"/>
    <Register v-if="activeStep === 0" @registration-successful="nextStep"/>
    <Edit :showSecondButton="true" v-if="activeStep === 1" @submit="submit($event)" @skip='nextStep'/>
    <Completion v-if="activeStep === 2"/>
  </div>
</template>

<script setup>
import { ref } from "vue";
import Register from "../../components/Register.vue";
import Edit from "../../components/Edit.vue";
import Completion from "../../components/Completion.vue";
import {useToast} from "primevue/usetoast";
import { useStore } from 'vuex';

const store = useStore();
const toast = useToast();
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

const submit = async (data) => {
  try{
    await store.dispatch('updateUserDetails', data);
    toast.add({severity: 'success', summary: 'Success', detail: 'details added successfully', life: 3000});
    activeStep.value++;
  }catch (e) {
    toast.add({severity: 'error', summary: 'Error', detail: e.message, life: 3000});
  }
};

const nextStep = () => {
  activeStep.value++;
};

</script>

<style scoped>
.invalid-input {
  border-color: red;
}
</style>
