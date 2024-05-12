<template>
  <div class="card flex flex-col items-center justify-center w-1/2 mx-auto h-screen">
    <Edit v-if="userData"
          :kilograms="userData.kilograms ? userData.kilograms : null"
          :height="userData.height ? userData.height : null"
          :age="userData.age ? userData.age : null"
          :workoutState="userData.workoutState ? userData.workoutState : null"
          :gender="userData.gender ? userData.gender : null"
          :showSecondButton=false
          @submit="handleSuccessfulEdit"/>
  </div>
</template>

<script setup>
import Edit from '../../components/Edit.vue'
import router from "../../router/index.js";
import {useStore} from "vuex";
import {ref} from "vue";
import {useToast} from "primevue/usetoast";

const toast = useToast();
const store = useStore();
const userData = ref(store.getters.userDetails);

const handleSuccessfulEdit = async (data) => {
  try{
    await store.dispatch('updateUserDetails', data);
    toast.add({severity: 'success', summary: 'Success', detail: 'User details updated successfully'});
    await router.push({name: 'Home'});
  }catch (e) {
    toast.add({severity: 'error', summary: 'Error', detail: 'Failed to update user details'});
  }
};
</script>