import {createApp} from 'vue'
import App from './App.vue'
import router from './router';
import store from './store';

import PrimeVue from 'primevue/config';
import Accordion from 'primevue/accordion';
import AccordionTab from 'primevue/accordiontab';
import Avatar from 'primevue/avatar';
import Button from 'primevue/button';
import Chart from 'primevue/chart';
import Dialog from 'primevue/dialog';
import Divider from 'primevue/divider';
import Dropdown from 'primevue/dropdown';
import IconField from 'primevue/iconfield';
import InputGroup from 'primevue/inputgroup';
import InputGroupAddon from 'primevue/inputgroupaddon';
import InputIcon from 'primevue/inputicon';
import InputNumber from 'primevue/inputnumber';
import InputSwitch from 'primevue/inputswitch';
import InputText from 'primevue/inputtext';
import Knob from 'primevue/knob';
import Password from 'primevue/password';
import ProgressSpinner from 'primevue/progressspinner';
import Steps from 'primevue/steps';
import Tag from 'primevue/tag';
import Toast from 'primevue/toast';
import TreeSelect from 'primevue/treeselect';
import Tooltip from 'primevue/tooltip';
import ToastService from 'primevue/toastservice';

import Wind from "./presets/wind";
import './style.css'

const app = createApp(App);

app.use(PrimeVue, {
    unstyled: true,
    pt: Wind
});
app.directive('tooltip', Tooltip);
app.use(router);
app.use(store);
app.use(ToastService);
app.component('TreeSelect', TreeSelect);
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
app.component('Knob', Knob);
app.component('Tag', Tag);

export const toast = app.config.globalProperties.$toast;

app.mount('#app');
