 <template>
  <div v-if="isLoading" class="spinner-container">
    <ProgressSpinner />
  </div>
  <div class="flex flex-col min-h-screen max-h-screen">
    <Header/>
    <router-view class="flex-grow"></router-view>
  </div>
  <Toast/>
</template>

<script setup>
import Header from "./components/Hedaer.vue";
import {useStore} from "vuex";
import {useToast} from "primevue/usetoast"
import {computed, onBeforeMount} from "vue";

const store = useStore();
const toast = useToast();

const isLoading = computed(() => store.state.isLoading);

onBeforeMount(async () => {
  try {
    const user = store.getters.user;
    const email = user.email;
    const password = user.password;
    await store.dispatch('login' , {email, password});
  } catch (error) {
    toast.add({severity: 'error', summary: 'Error', detail: error.message, life: 3000});
  }
});

</script>

<style scoped>
.spinner-container {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background-color: white;
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 9999;
}
</style>
