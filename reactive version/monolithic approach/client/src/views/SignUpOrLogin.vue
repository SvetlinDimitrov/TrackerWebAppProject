<template>
  <div class="card flex items-center justify-center">
    <div class="w-1/2 flex flex-col md:flex-row">
      <Login/>
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
import { useRouter } from 'vue-router';
import Login from "../components/Login.vue";

const router = useRouter();

const goToSignUp = () => {
  router.push({ name: 'Register' });
};

</script>
