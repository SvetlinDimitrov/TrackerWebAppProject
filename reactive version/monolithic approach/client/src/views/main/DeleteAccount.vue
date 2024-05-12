<script setup>
import {onMounted} from "vue";
import router from "../../router/index.js";
import {useStore} from "vuex";
import {useToast} from "primevue/usetoast"

const store = useStore();
const toast = useToast();

onMounted(async () => {
  const confirmation = window.confirm('Are you sure you want to delete your account? This action cannot be undone.');

  if (confirmation) {
    try {
      await store.dispatch('deleteUser');
      toast.add({severity: 'success', summary: 'Success', detail: 'successful deletion of account', life: 3000});
    } catch (e) {
      toast.add({severity: 'error', summary: 'Error', detail: e.message, life: 3000});
    }
    await router.push({name: 'Home'});
  }else{
    await router.push({name: 'Settings'});
  }
});
</script>
