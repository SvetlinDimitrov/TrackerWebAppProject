<template>
  <div class="fixed inset-0 flex items-center justify-center backdrop-blur">
    <div class="bg-white border border-gray-300 px-4 py-3 rounded w-1/2 h-1/2">
      <div class="flex justify-between items-center mb-4">
        <h2 class="text-2xl font-bold">Your foods</h2>
        <Avatar icon="pi pi-times" class="cursor-pointer" @click="emits('close')"/>
      </div>
      <div class="flex items-center gap-2 mb-4">
        <Button label="Add Custom Food" @click="emits('handleAddCustomFood')" />
      </div>

      <div class="relative h-3/4 overflow-auto border border-gray-300 rounded p-2">
        <ul>
          <li v-for="(food, index) in props.foods" :key="food.name + index"
              class="flex justify-between items-center p-1 mb-2 cursor-pointer hover:bg-gray-200 transition-colors duration-200 border-b border-gray-200 text-lg font-semibold"
              @click="emits('foodClick', food)">
            <span>{{ food.name }}</span>
            <div class="flex gap-2">
              <Avatar icon="pi pi-pencil" class="cursor-pointer" @click.stop="emits('editFood', food)" />
              <Avatar icon="pi pi-trash" class="cursor-pointer" @click.stop="emits('deleteFood', food)" />
            </div>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script setup>
import {defineEmits, defineProps} from 'vue';

const props = defineProps({
  foods: Array,
});

const emits = defineEmits(['close', 'foodClick', 'handleAddCustomFood' , 'editFood', 'deleteFood']);
</script>
