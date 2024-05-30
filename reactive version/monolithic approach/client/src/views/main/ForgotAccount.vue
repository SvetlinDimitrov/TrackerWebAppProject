<template>
  <div class="card flex items-center justify-center h-screen">
    <div class="mb-4">
      <h2 class="text-center text-2xl font-bold mb-4">Reset Password</h2>
      <p class="text-center mb-4">Please enter your email to receive a link to reset your password.</p>
      <div class="m-auto flex items-center">
        <IconField v-bind:class="{ 'border-2 rounded-lg border-red-500': emailError !== '' }" class="flex-grow">
          <InputIcon>
            <i class="pi pi-envelope"/>
          </InputIcon>
          <InputText id="input" v-model="email" type="email" placeholder="Email"/>
        </IconField>
        <Button label="Submit" iconPos="right" @click="registerHandle" class="ml-2"/>
      </div>
      <div class="flex justify-center w-full mt-2">
        <router-link :to="{name: 'SignUpOrLogin'}" class="text-sm text-blue-500">Back to Login</router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import {ref} from "vue";
import {useToast} from "primevue/usetoast";
import {useStore} from "vuex";

const toast = useToast();
const emailError = ref('');
const email = ref('');
const store = useStore();

const registerHandle = async () => {
  const emailValid = email.value && validateEmail(email.value);

  if (!emailValid) {
    emailError.value = 'Invalid email';
    toast.add({severity: 'error', summary: 'Error', detail: emailError, life: 3000});
    return
  }
  const lastRequestTime = store.getters.getRequestsEmailReset[email.value];
  const oneHourInMilliseconds = 60 * 60 * 1000;

  if (lastRequestTime && Date.now() - lastRequestTime < oneHourInMilliseconds) {
    const timeRemaining = Math.ceil((oneHourInMilliseconds - (Date.now() - lastRequestTime)) / 60000);
    toast.add({
      severity: 'error',
      summary: 'Error',
      detail: `You cannot make a request until ${timeRemaining} minutes have passed.`,
      life: 3000
    });
    return;
  }

  try {
    await store.dispatch('sendEmailForNewPasswordTokenLink', {email: email.value});
    store.commit('addRequestsEmailReset', email.value);
    toast.add({severity: 'success', summary: 'Success', detail: 'Rest link was sent to the given mail', life: 3000});
  } catch (e) {
    toast.add({severity: 'error', summary: 'Error', detail: e.message, life: 3000});
  }
}

const validateEmail = (email) => {
  const re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
  return re.test(email);
};
</script>

<style scoped>

</style>