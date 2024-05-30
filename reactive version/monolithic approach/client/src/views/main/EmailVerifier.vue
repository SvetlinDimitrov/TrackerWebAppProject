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
    <div v-if="countdown > 0 && activeStep === 3"
         class="bg-gray-100 p-4 rounded shadow-md text-center text-lg font-semibold mt-4">
      You will be redirected to the home page in {{ countdown }} seconds.
    </div>
  </div>
</template>

<script setup>
import {ref, watchEffect} from "vue";
import {useRoute, useRouter} from "vue-router";
import {useToast} from "primevue/usetoast";
import {useStore} from "vuex";
import {AES, enc} from 'crypto-js';

const store = useStore();
const router = useRouter();
const route = useRoute();
const toast = useToast();
const countdown = ref(5);
const activeStep = ref(1);

watchEffect(async () => {
  const token = route.query.token;
  const userDetails = localStorage.getItem('ud');
  const secretKey = import.meta.env.VITE_CRYPTO_SECRET;

  if (!userDetails) {
    await router.push({name: 'Register'});
    return;
  }

  if (token) {
    activeStep.value = 2;
    try {
      const bytes = AES.decrypt(userDetails, secretKey);
      const {email, password, username} = JSON.parse(bytes.toString(enc.Utf8));

      await store.dispatch('register', {username, email, password, token});
      toast.add({severity: 'success', summary: 'Success', detail: 'Account created', life: 3000});
    } catch (e) {
      toast.add({severity: 'error', summary: 'Error', detail: "Something went wrong", life: 3000});
    } finally {
      localStorage.removeItem('ud');
      activeStep.value = 3;
      let intervalId = setInterval(() => {
        countdown.value--;
        if (countdown.value === 0) {
          clearInterval(intervalId);
          router.push({name: 'Home'});
        }
      }, 1000);
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