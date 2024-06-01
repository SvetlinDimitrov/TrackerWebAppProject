<template>
  <div class="card flex flex-col items-center justify-center w-1/2 mx-auto h-screen">
    <div class="flex flex-col gap-2" style="min-height: 16rem; max-width: 20rem">
      <div class="text-center mt-3 mb-3 text-xl font-semibold">Reset Password</div>
      <div class="mb-4" v-bind:class="{ 'border-2 rounded-lg border-red-500': passwordError !== '' }">
        <Password v-model="newPassword" toggleMask placeholder="New Password" class="w-full"/>
      </div>
      <div class="mb-4" v-bind:class="{ 'border-2 rounded-lg border-red-500': passwordConfirmError !== '' }">
        <Password v-model="confirmPassword" toggleMask placeholder="Confirm New Password" class="w-full"/>
      </div>
      <div class="flex pt-2 justify-center w-full">
        <Button label="Submit" icon="pi pi-check" iconPos="right" @click="handleSubmit"/>
      </div>
    </div>
  </div>
</template>

<script setup>
import {onMounted, ref} from "vue";
import {useToast} from "primevue/usetoast";
import {useRoute, useRouter} from "vue-router";
import {useStore} from "vuex";

const toast = useToast();
const router = useRouter();
const route = useRoute();
const store = useStore();
const newPassword = ref('');
const confirmPassword = ref('');
const passwordError = ref('');
const passwordConfirmError = ref('');
const token = ref(null);

onMounted(() => {
  token.value = route.query.token;
  if (!token.value) {
    router.push({name: 'SignUpOrLogin'});
  }
});

const handleSubmit = async () => {
  if (!validatePasswords()) {
    return;
  }
  try {
    await store.dispatch('resetPassword', {newPassword: newPassword.value, token: token.value})
    toast.add({severity: 'success', summary: 'Success', detail: 'Changes were made successfully', life: 3000});
    await router.push({name: 'SignUpOrLogin'});
  } catch (e) {
    toast.add({severity: 'error', summary: 'Error', detail: e.message, life: 3000});
  }
};

const validatePasswords = () => {
  newPassword.value = newPassword.value.trim();
  confirmPassword.value = confirmPassword.value.trim();

  if (newPassword.value.length < 4) {
    toast.add({
      severity: 'error',
      summary: 'Error',
      detail: 'Password should be at least 4 characters long',
      life: 3000
    });
    passwordError.value = 'Password should be at least 4 characters long';
    return false;
  }

  if (confirmPassword.value.length < 4) {
    toast.add({
      severity: 'error',
      summary: 'Error',
      detail: 'ConfirmPassword should be at least 4 characters long',
      life: 3000
    });
    passwordConfirmError.value = 'Password should be at least 4 characters long';
    return false;
  }

  if (newPassword.value !== confirmPassword.value) {
    toast.add({severity: 'error', summary: 'Error', detail: 'Passwords do not match', life: 3000});
    passwordConfirmError.value = 'Passwords do not match';
    return false;
  }

  return true;
};

</script>

<style scoped>
.invalid-input {
  border-color: red;
}
</style>