<template>
  <div class="w-full h-4 bg-gray-200 rounded-full no-margin">
    <div :style="{ width: `${progress}%` }" class="h-full bg-green-500 rounded-full flex items-center justify-center">
      <span v-if="progress >= 10" class="text-white text-xs">{{ progress }}%</span>
    </div>
  </div>
</template>

<script setup>

const props = defineProps({
  progress: {
    type: Number,
    required: true
  }
});

</script>

<style scoped>
.no-margin {
  margin: 6px !important;
}
</style>