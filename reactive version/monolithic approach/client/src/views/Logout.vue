<template>
  <Toast/>
</template>

<script setup>
import { useStore } from 'vuex';
import {onMounted} from "vue";
import router from "../router/index.js";
import {useToast} from "primevue/usetoast"

const toast = useToast();
const store = useStore();

onMounted(async () => {
  await store.dispatch('logout');
  await store.dispatch('removeRecord');
  await router.push({name: 'Home'}).catch(() => {});
  toast.add({severity: 'success', summary: 'Logout Completed', detail: 'Success', life: 3000});

});
</script>

<style scoped>

</style>