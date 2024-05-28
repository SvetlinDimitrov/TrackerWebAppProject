<template>
  <div class="card ">
    <Menubar :model="items">
      <template #start>
        <img alt="Vue logo" src="../assets/vue.svg"/>
      </template>
    </Menubar>
  </div>
</template>

<script setup>
import {computed} from "vue";
import {useRouter} from 'vue-router';
import Menubar from 'primevue/menubar';
import 'primeicons/primeicons.css';
import { useStore } from 'vuex';
import {macro, minerals, vitamins} from "../utils/nutrition/avaibleNutritions.js";

const store = useStore();
const router = useRouter();
const user = computed(() => store.getters.user);
const userDetailsComplete = computed(() => store.getters.isFullyRegistered);

const items = computed(() => {
  const userLoggedIn = user.value;
  const fullyAuthed = userDetailsComplete.value;
  return [
    {
      label: 'Home',
      icon: 'pi pi-home',
      command: () => router.push({name: 'Home'})
    },
    {
      label: 'About',
      icon: 'pi pi-info-circle',
      command: () => router.push({name: 'About'})
    },
    {
      label: 'Nutri Info',
      icon: 'pi pi-star',
      items: [
        {
          label: 'Vitamins',
          icon: 'pi pi-fw pi-plus',
          items: vitamins.map(vitamin => ({
            label: vitamin,
            command: () => router.push({name: 'NutriInfo', params: {name: vitamin}})
          }))
        },
        {
          label: 'Minerals',
          icon: 'pi pi-fw pi-plus',
          items: minerals.map(mineral => ({
            label: mineral,
            command: () => router.push({name: 'NutriInfo', params: {name: mineral}})
          }))
        },
        {
          label: 'Macros',
          icon: 'pi pi-fw pi-plus',
          items: macro.map(macronutrient => ({
            label: macronutrient,
            command: () => router.push({name: 'NutriInfo', params: {name: macronutrient}})
          }))
        }
      ]

    },
    userLoggedIn ? {
      label: 'Settings',
      icon: 'pi pi-cog',
      command: () => router.push({name: 'Settings'})
    } : null,
    userLoggedIn ? null : {
      label: 'Register/Login',
      icon: 'pi pi-user',
      command: () => router.push({name: 'SignUpOrLogin'})
    },
    fullyAuthed ?  {
      label: 'Performance',
      icon: 'pi pi-chart-bar',
      command: () => router.push({name: 'Performance'})
    } : null,
  ].filter(Boolean);
});
</script>