<template>
  <div class="fixed inset-0 flex items-center justify-center backdrop-blur">
    <div class="bg-white border border-gray-300 px-4 py-3 rounded w-1/2 h-1/2">
      <div class="flex justify-between items-center mb-4">
        <h2 class="text-2xl font-bold">Search for food</h2>
        <Avatar icon="pi pi-times" class="cursor-pointer" @click="emits('close')"/>
      </div>
      <div class="flex items-center gap-2 mb-4">
        <InputText v-model="search"
                   class="flex-grow" placeholder="Example: Apple"
                   minlength="2"
                   maxlength="100"
                   @update:modelValue="emits('update:search', $event)"/>
        <Button label="Search" @click="emits('search')"/>
      </div>

      <div class="relative h-3/4 overflow-auto border border-gray-300 rounded p-2">
        <ul>
          <li v-for="(food, index) in props.foods" :key="food.name + index"
              class="p-1 mb-2 cursor-pointer hover:bg-gray-200 transition-colors duration-200 border-b border-gray-200 text-lg font-semibold"
              @click="emits('foodClick', food)">{{ food.name }}
            <span v-if="food.brand">({{ food.brand }})</span>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script setup>
import {defineProps} from 'vue';
const props = defineProps({
  search: String,
  foods: Array,
});

const search = props.search;

const emits = defineEmits(['close', 'search', 'foodClick', 'update:search']);
</script>
