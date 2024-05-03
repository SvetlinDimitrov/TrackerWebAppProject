<template>
  <div class="card flex items-center justify-center">
    <div class="w-1/2 flex flex-col md:flex-row">
      <div class="w-full md:w-1/2 flex flex-col items-center justify-center gap-3 py-5">
        <div class="flex flex-col justify-center items-center gap-2">
          <div>
            <InputGroup>
              <template #prepend>
                <i class="pi pi-user"></i>
              </template>
              <InputText id="username" type="text" placeholder="Email" v-model="email" class="w-full" v-bind:class="{ 'invalid-input': emailError }" />
            </InputGroup>
          </div>
          <div>
            <InputGroup>
              <template #prepend>
                <i class="pi pi-key"></i>
              </template>
              <InputText id="password" type="password" placeholder="Password" v-model="password" class="w-full" v-bind:class="{ 'invalid-input': passwordError }" />
            </InputGroup>
          </div>
        </div>
        <Toast />
        <Button label="Login" icon="pi pi-user" class="w-40 mx-auto" @click="validateForm"></Button>
      </div>
      <div class="w-full md:w-1/12">
        <Divider layout="vertical" class="hidden md:flex"><b>OR</b></Divider>
        <Divider layout="horizontal" class="flex md:hidden" align="center"><b>OR</b></Divider>
      </div>
      <div class="w-full md:w-1/2 flex items-center justify-center py-5">
        <Button label="Sign Up" icon="pi pi-user-plus" severity="success" class="w-40" @click="goToSignUp"></Button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useToast } from "primevue/usetoast"
import { useStore } from 'vuex';
import { useRouter } from 'vue-router';

const router = useRouter();
const store = useStore();
const email = ref('');
const password = ref('');
const emailError = ref(false);
const passwordError = ref(false);
const toast = useToast();

const goToSignUp = () => {
  router.push({ name: 'Register' });
};

const validateEmail = (email) => {
  const re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
  return re.test(email);
};

const validateForm = async () => {
  const emailIsValid = email.value && validateEmail(email.value);
  const passwordIsValid = password.value && password.value.length >= 4;

  emailError.value = !emailIsValid;
  passwordError.value = !passwordIsValid;

  if (!emailIsValid) {
    toast.add({severity: 'error', summary: 'Error', detail: 'Invalid email', life: 3000});
  }
  if (!passwordIsValid) {
    toast.add({severity: 'error', summary: 'Error', detail: 'Password must be at least 4 characters long', life: 3000});
  }
  if (emailIsValid && passwordIsValid) {
    try {
      await store.dispatch('login', { username: email.value, password: password.value });
      toast.add({severity:'success', summary: 'Success', detail: 'Login successful', life: 3000});
    } catch (error) {
      toast.add({severity:'error', summary: 'Error', detail: error.message, life: 3000});
    }
  }
};
</script>

<style scoped>
.invalid-input {
  border-color: red;
}
</style>