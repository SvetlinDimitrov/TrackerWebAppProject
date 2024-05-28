<template>
  <div v-if="isLoading" class="spinner-container">
    <ProgressSpinner/>
  </div>
  <div v-if="!isRebooting" class="flex flex-col h-screen">
    <Header/>
    <router-view></router-view>
  </div>
  <Toast/>
</template>

<script setup>
import Header from "./components/Hedaer.vue";
import {useStore} from "vuex";
import {computed, onBeforeMount} from "vue";

const store = useStore();
const isLoading = computed(() => store.state.isLoading);
const isRebooting = computed(() => store.getters.isRebooting);

onBeforeMount(async () => {
  await store.dispatch("reboot");
});

</script>

<style scoped>
.spinner-container {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background-color: white;
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 9999;
}
</style>
