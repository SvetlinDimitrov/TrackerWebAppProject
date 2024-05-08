import {createApp} from 'vue'
import './style.css'
import App from './App.vue'
import router from './router';
import store from './store';
import PrimeVue from 'primevue/config';
import ToastService from 'primevue/toastservice';
import Wind from "./presets/wind";
import Lara from "./presets/lara";
import Button from 'primevue/button';
import Divider from 'primevue/divider';
import InputText from 'primevue/inputtext';
import InputGroup from 'primevue/inputgroup';
import Toast from 'primevue/toast';
import IconField from 'primevue/iconfield';
import InputIcon from 'primevue/inputicon';
import Password from 'primevue/password';
import Steps from 'primevue/steps';
import InputGroupAddon from 'primevue/inputgroupaddon';
import InputNumber from 'primevue/inputnumber';
import Dropdown from 'primevue/dropdown';
import Accordion from 'primevue/accordion';
import AccordionTab from 'primevue/accordiontab';
import ProgressSpinner from 'primevue/progressspinner';
import Dialog from 'primevue/dialog';
import Avatar from 'primevue/avatar';
import InputSwitch from 'primevue/inputswitch';
import Chart from 'primevue/chart';

const app = createApp(App);
app.use(PrimeVue, {
    unstyled: true,
    pt: Wind
});
app.use(router);
app.use(store);
app.use(ToastService);

app.component('Steps', Steps);
app.component('Button', Button);
app.component('Divider', Divider);
app.component('InputText', InputText);
app.component('InputGroup', InputGroup);
app.component('Toast', Toast);
app.component('Dropdown', Dropdown);
app.component('IconField', IconField);
app.component('InputIcon', InputIcon);
app.component('Password', Password);
app.component('InputNumber', InputNumber);
app.component('InputGroupAddon', InputGroupAddon);
app.component('Accordion', Accordion);
app.component('AccordionTab', AccordionTab);
app.component('ProgressSpinner', ProgressSpinner);
app.component('Dialog', Dialog);
app.component('Avatar', Avatar);
app.component('InputSwitch', InputSwitch);
app.component('Chart', Chart);
app.mount('#app');
