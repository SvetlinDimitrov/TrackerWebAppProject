<template>
  <div class="card ">
    <Menubar :model="items">
      <template #start>
        <img alt="Vue logo" src="../assets/vue.svg" />
      </template>
    </Menubar>
  </div>
</template>

<script setup>
import { computed } from "vue";
import { useRouter } from 'vue-router';
import Menubar from 'primevue/menubar';
import 'primeicons/primeicons.css';
import { useStore } from 'vuex';

const router = useRouter();
const store = useStore();

const items = computed(() => {
  const userLoggedIn = store.getters.isLoggedIn;
  return [
    {
      label: 'Home',
      icon: 'pi pi-home',
      command: () => router.push({name: 'Home'})
    },
    {
      label: 'Nutri Info',
      icon: 'pi pi-star',
      command: () => router.push({name: 'NutriInfo'})
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
  ].filter(Boolean);
});
</script>