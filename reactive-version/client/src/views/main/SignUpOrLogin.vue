<template>
  <div class="card flex items-center justify-center h-screen">
    <div class="w-1/2 flex flex-col md:flex-row">
      <Login @submit="submit" :emailError="emailError" :passwordError="passwordError"/>
      <div class="w-full md:w-1/12">
        <Divider layout="vertical" class="hidden md:flex"><b>OR</b></Divider>
        <Divider layout="horizontal" class="flex md:hidden" align="center"><b>OR</b></Divider>
      </div>

      <div class="w-full md:w-1/2 flex items-center justify-center py-5 flex-col gap-2">
        <Button label="Sign Up" icon="pi pi-user-plus" severity="success" class="w-40" @click="goToSignUp"></Button>
        <router-link :to="{name: 'ForgotAccount'}" class="text-sm text-blue-500">Forgot account?</router-link>
      </div>

    </div>
  </div>
</template>

<script setup>
import {useRouter} from 'vue-router';
import Login from "../../components/Login.vue";
import {useStore} from "vuex";
import {useToast} from "primevue/usetoast";
import {ref} from "vue";

const toast = useToast();
const router = useRouter();
const store = useStore();
const emailError = ref(false);
const passwordError = ref(false);

const goToSignUp = () => {
  router.push({name: 'Register'});
};

const submit = async (data) => {
  const emailIsValid = data.email && validateEmail(data.email);
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
      await store.dispatch('login', data);
      toast.add({severity: 'success', summary: 'Success', detail: 'Login successful', life: 3000});
      await router.push({name: 'Home'});
    } catch (error) {
      toast.add({severity: 'error', summary: 'Error', detail: 'Invalid credentials', life: 3000});
    }
  }
};

const validateEmail = (email) => {
  const re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
  return re.test(email);
};

</script>
