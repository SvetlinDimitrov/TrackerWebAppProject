<script setup>

import Header from "./components/Hedaer.vue";
</script>

<template>
  <div class="flex flex-col min-h-screen">
    <Header />
    <router-view class="flex-grow" />
  </div>
</template>

<style scoped>
</style>
