<template>
  <div class="text-center text-xl font-semibold">
    Registration completed successfully! You will be redirected to the login page in {{ countdown }} seconds.
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();
const countdown = ref(5);

onMounted(() => {
  const intervalId = setInterval(() => {
    if (countdown.value > 0) {
      countdown.value--;
    } else {
      clearInterval(intervalId);
      router.push({ name: 'Home' });
    }
  }, 1000);
});
</script>

<style scoped>
/* Add your styles here */
</style>