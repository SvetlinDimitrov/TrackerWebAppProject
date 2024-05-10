<script setup>
import {onMounted, ref} from "vue";
import {useStore} from "vuex";
import {useRoute} from "vue-router";
import {useToast} from "primevue/usetoast";
import router from "../../router/index.js";

const toast = useToast();
const store = useStore();
const route = useRoute();
const mealId = ref(route.params.id);
const foodId = ref(route.params.foodId);

onMounted(async () => {
  try {
    await store.dispatch('removeFoodById', {mealId: mealId.value, foodId: foodId.value});
    toast.add({severity: 'success', summary: 'Success', detail: 'food removed successfully', life: 3000});
    await router.push({name: 'Home'});
  } catch (e) {
    await router.push({name: 'Home'});
    toast.add({severity: 'error', summary: 'Error', detail: e.message, life: 3000});
  }
});
</script>
