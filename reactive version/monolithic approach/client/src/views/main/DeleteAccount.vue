<template>
  <div class="flex flex-col justify-center items-center w-1/2 mx-auto">
    <div>
      <h1 class="text-2xl mb-4">Are you sure you want to delete your account?</h1>
      <button @click="deleteAccount" class="w-full p-4 bg-gray-100 text-black rounded shadow-lg text-center cursor-pointer mb-4 transition duration-500 ease-in-out transform hover:-translate-y-1 hover:scale-110">
        Delete Account
      </button>
    </div>
  </div>
</template>

<script setup>
import {useStore} from 'vuex';
import router from "../../router/index.js";
import {useToast} from "primevue/usetoast";

const store = useStore();
const toast = useToast();

const deleteAccount = async () => {
  try{
    await store.dispatch('deleteUser');
    toast.add({severity: 'success', summary: 'Success', detail: 'successful deletion of account', life: 3000});
  } catch (e) {
    toast.add({severity: 'error', summary: 'Error', detail: e.message, life: 3000});
  }
  await router.push({name: 'Home'});
};
</script>