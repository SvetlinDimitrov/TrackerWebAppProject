<template>
  <div class="card flex flex-col items-center justify-center w-1/2 mx-auto h-screen">
    <Steps :model="items" class="mb-8" :activeStep="activeStep"/>
    <div v-if="activeStep === 1" class="mt-4">
      <div class="flex items-center bg-gray-100 p-4 rounded shadow-md max-w-md mx-auto">
        <Avatar icon="pi pi-user" size="large" shape="circle"/>
        <div>
          <h4 class="text-lg font-semibold mb-2">Email Verification</h4>
          <p class="text-gray-700">Verification email has been sent. Please check your inbox.</p>
        </div>
      </div>
    </div>
    <div v-if="countdown > 0 && activeStep === 2"
         class="bg-gray-100 p-4 rounded shadow-md text-center text-lg font-semibold mt-4">
      You will be redirected to the home page in {{ countdown }} seconds.
    </div>
  </div>
</template>

<script setup>
import {onMounted, ref, watchEffect} from "vue";
import {useRoute, useRouter} from "vue-router";
import {useToast} from "primevue/usetoast";
import {useStore} from "vuex";

const store = useStore();
const router = useRouter();
const route = useRoute();
const toast = useToast();
const countdown = ref(5);
const activeStep = ref(1);

onMounted(() => {
  const userDetails = localStorage.getItem('userDetails');

  if (userDetails) {

  } else {
    router.push({name: 'Register'});
    toast.add({severity: 'error', summary: 'Error', detail: 'Register first', life: 3000});
  }
});

watchEffect(async () => {
  const token = route.query.token;

  if (token) {
    activeStep.value = 2;
    try {
      const userDetails = localStorage.getItem('userDetails');

      const {email, password, username} = JSON.parse(userDetails);

      await store.dispatch('register', {username, email, password, token});
      toast.add({severity: 'success', summary: 'Success', detail: 'Account created', life: 3000});
      localStorage.removeItem('userDetails');
      await router.push({name: 'Home'});
    } catch (e) {
      toast.add({severity: 'error', summary: 'Error', detail: e.message, life: 3000});
    }
  }
});

const items = ref([
  {
    label: 'Sign Up'
  },
  {
    label: 'Email Verification'
  },
  {
    label: 'Completed'
  }
]);
</script>