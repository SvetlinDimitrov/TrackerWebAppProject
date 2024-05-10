<script setup>
import {onMounted, ref} from "vue";
import router from "../../router/index.js";
import {useRoute} from "vue-router";
import {useStore} from "vuex";
import {useToast} from "primevue/usetoast"

const store = useStore();
const toast = useToast();
const route = useRoute();

const mealId = ref(route.params.id);

onMounted(async () => {
  try{
    await store.dispatch('deleteMealById', mealId.value);
    toast.add({severity: 'success', summary: 'Success', detail: 'meal deleted successfully', life: 3000});
  }catch (error) {
    toast.add({severity: 'error', summary: 'Error', detail: error.message, life: 3000});
  }
  await router.push({name: 'Home'});
});
</script>

<style scoped>
</style>