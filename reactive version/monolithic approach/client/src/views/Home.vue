<template>
  <div class="flex flex-col items-center justify-center p-8">
    <FullyAuthHome v-if="user && record"/>
    <NoAuthHomePage v-else/>
    <router-view></router-view>
  </div>
</template>

<script setup>
import {ref} from 'vue';
import {useStore} from 'vuex';
import NoAuthHomePage from "../components/NoAuthHomePage.vue";
import FullyAuthHome from "../components/FullyAuthHome.vue";

const store = useStore();
const record = ref(store.getters.record);
const user = ref(store.getters.user);

</script>