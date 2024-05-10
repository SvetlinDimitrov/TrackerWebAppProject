<template>
  <div class="flex flex-col gap-2" style="min-height: 16rem; max-width: 20rem">
    <div class="text-center mt-3 mb-3 text-xl font-semibold">Create your account</div>
    <div class="mb-4" v-bind:class="{ 'invalid-input': emailError !== '' }">
      <IconField v-bind:class="{ 'border-2 rounded-lg border-red-500': nameError !== '' }">
        <InputIcon>
          <i class="pi pi-user"/>
        </InputIcon>
        <InputText id="input" v-model="name" type="text" placeholder="Name"/>
      </IconField>
    </div>
    <div class="mb-4">
      <IconField v-bind:class="{ 'border-2 rounded-lg border-red-500': emailError !== '' }">
        <InputIcon>
          <i class="pi pi-envelope"/>
        </InputIcon>
        <InputText id="email" v-model="email" type="email" placeholder="Email"/>
      </IconField>
    </div>
    <div class="mb-4" v-bind:class="{ 'border-2 rounded-lg border-red-500': passwordError !== '' }">
      <Password v-model="password" toggleMask placeholder="Password" class="w-full"/>
    </div>
    <div class="flex pt-2 justify-center w-full">
      <Button label="Next" icon="pi pi-arrow-right" iconPos="right" @click="registerHandle"/>
    </div>
  </div>
</template>

<script setup>
import {ref} from 'vue';
import {useToast} from "primevue/usetoast"
import store from "../store/index.js";

const toast = useToast();
const name = ref('');
const email = ref('');
const password = ref('');
const emit = defineEmits(['registration-successful'])

const nameError = ref('');
const emailError = ref('');
const passwordError = ref('');

const validateEmail = (email) => {
  const re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
  return re.test(email);
};

const registerHandle = async () => {
  const emailValid = email.value && validateEmail(email.value);
  const passwordValid = password.value && password.value.length >= 4;
  const nameValid = name.value && name.value.length >= 2;

  emailError.value= "";
  passwordError.value= "";
  nameError.value= "";

  if (!emailValid) {
    emailError.value = 'Invalid email';
    toast.add({severity: 'error', summary: 'Error', detail: emailError, life: 3000});
  }
  if (!passwordValid) {
    passwordError.value = 'Invalid password';
    toast.add({severity: 'error', summary: 'Error', detail: passwordError, life: 3000});
  }
  if (!nameValid) {
    nameError.value = 'Invalid name';
    toast.add({severity: 'error', summary: 'Error', detail: nameError, life: 3000});
  }
  if (emailValid && passwordValid && nameValid) {
    try {
      await store.dispatch('register', {username: name.value, email: email.value, password: password.value});
      toast.add({severity: 'success', summary: 'Success', detail: 'Login successful', life: 3000});
      emit('registration-successful');
    } catch (error) {
      toast.add({severity: 'error', summary: 'Error', detail: error.message, life: 3000});
    }
  }
};
</script>

<style scoped>

</style>