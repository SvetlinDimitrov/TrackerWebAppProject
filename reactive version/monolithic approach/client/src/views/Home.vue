
<template>
  <div class="card flex justify-center">
    <Dropdown v-model="selectedCity" editable :options="cities" optionLabel="name" placeholder="Select a City" class="w-full md:w-[14rem]" />
  </div>
  <div class="card flex justify-center">
    <Calendar v-model="date" dateFormat="dd/mm/yy" />
  </div>
  <Button label="Danger" severity="danger" />
</template>

<script setup>
import { ref } from "vue";
import Dropdown from 'primevue/dropdown';
import Calendar from 'primevue/calendar';
import Button from 'primevue/button';


const selectedCity = ref();
const date = ref();
const cities = ref([
  { name: 'New York', code: 'NY' },
  { name: 'Rome', code: 'RM' },
  { name: 'London', code: 'LDN' },
  { name: 'Istanbul', code: 'IST' },
  { name: 'Paris', code: 'PRS' }
]);
</script>
