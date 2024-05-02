import {createApp} from 'vue'
import './style.css'
import App from './App.vue'
import router from './router';
import store from './store';
import PrimeVue from 'primevue/config';

import Wind from "./presets/wind";
import Lara from "./presets/lara";

const app = createApp(App);
app.use(PrimeVue, {
    unstyled: true,
    pt: Wind
});
app.use(router);
app.use(store);
app.mount('#app');
