<template>
  <div class="w-full md:w-1/2 flex flex-col items-center justify-center gap-3 py-5">
    <div class="flex flex-col justify-center items-center gap-2">
      <div>
        <InputGroup>
          <template>
            <i class="pi pi-user"></i>
          </template>
          <InputText id="username"
                     type="text"
                     placeholder="Email"
                     v-model="email"
                     class="w-full"
                     maxlength="100"
                     v-bind:class="{ 'invalid-input': emailError }"/>
        </InputGroup>
      </div>
      <div>
        <InputGroup>
          <template>
            <i class="pi pi-key"></i>
          </template>
          <InputText id="password"
                     type="password"
                     placeholder="Password"
                     v-model="password"
                     class="w-full"
                     minlength="4"
                     maxlength="100"
                     v-bind:class="{ 'invalid-input': passwordError }"/>
        </InputGroup>
      </div>
    </div>
    <Button label="Login" icon="pi pi-user" class="w-40 mx-auto" @click="validateForm"></Button>
  </div>
</template>

<script setup>
import {ref} from "vue";

const email = ref('');
const password = ref('');
const emit = defineEmits(['submit'])
const props = defineProps({
  emailError: Boolean,
  passwordError: Boolean
})

const validateForm = async () => {
  emit('submit', {email: email.value, password: password.value})
};
</script>

<style scoped>
.invalid-input {
  border-color: red;
}
</style>