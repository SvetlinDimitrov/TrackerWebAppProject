<template>
  <div v-if="isLoading" class="spinner-container">
    <ProgressSpinner/>
  </div>
  <div v-if="isLoginCompleted" class="flex flex-col h-screen">
    <Header/>
    <router-view></router-view>
  </div>
  <Toast/>
</template>

<script setup>
import Header from "./components/Hedaer.vue";
import {useStore} from "vuex";
import {useToast} from "primevue/usetoast"
import {computed, onBeforeMount, ref} from "vue";

const store = useStore();
const toast = useToast();
const isLoginCompleted = ref(false);
const isLoading = computed(() => store.state.isLoading);
onBeforeMount(async () => {
  const user = store.getters.user;
  if (user) {
    try {
      const email = user.email;
      const password = user.password;
      await store.dispatch('login', {email, password});
    } catch (error) {
      toast.add({severity: 'error', summary: 'Error', detail: error.message, life: 3000});
    }
  } else {
    store.commit('setLoginCompleted', true);
  }
  isLoginCompleted.value = true;
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
