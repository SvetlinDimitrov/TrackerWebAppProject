<template>
  <div>
    <h1>{{ name }}</h1>
  </div>
</template>

<script setup>
import {useRoute} from 'vue-router';
import {ref, watch} from "vue";

const route = useRoute();
const name = ref(route.params.name);

watch(route, () => {
  name.value = route.params.name;
});
</script>

<style scoped>

</style>