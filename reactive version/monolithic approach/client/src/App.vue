<template>
  <ProgressSpinner v-if="isLoading"/>
  <div v-else class="flex flex-col min-h-screen">
    <Header/>
    <router-view class="flex-grow"/>
  </div>
</template>

<script setup>
import Header from "./components/Hedaer.vue";
import {useStore} from "vuex";
import {onBeforeMount, ref} from "vue";

const store = useStore();
const isLoading = ref(true);

onBeforeMount(async () => {
  try {
    await store.dispatch('fetchDataWhenDOMContentLoaded');
  } catch (error) {

  }
  isLoading.value = false;
});
</script>

<style scoped>
</style>
