import {createRouter, createWebHistory} from 'vue-router';
import store from '../store';
import mainRoutes from './main';
import mealRoutes from './meal';
import foodRoutes from './food';
import { toast } from '../main';

const routes = [
    ...mainRoutes,
    ...mealRoutes,
    ...foodRoutes,
    {
        path: '/:pathMatch(.*)*',
        name: 'NotFound',
        component: () => import('../views/NotFound.vue')
    }

];

const router = createRouter({
    history: createWebHistory(),
    routes
});

router.beforeEach((to, from, next) => {
    const isValidJwt = store.getters.isJwtValid;
    const jwtToken = store.getters.jwt;

    if (!isValidJwt || !jwtToken) {
        toast.add({severity:'info', summary: 'Info', detail:"Your session has expired", life: 3000}); // Show a toast when the session has expired
        next({ name: 'Login' });
    } else {
        next();
    }
});

router.beforeEach((to, from, next) => {
    if (store.getters.isRebooting) {
        const unwatch = store.watch(
            (state) => state.isRebooting,
            (isRebooting) => {
                if (!isRebooting) {
                    unwatch();
                    toast.add({severity:'info', summary: 'Info', detail:"Rebooting completed" , life: 3000}); // Show a toast when rebooting is completed
                    next();
                }
            }
        );
    } else {
        next();
    }
});

router.beforeEach((to, from, next) => {

    const user = store.getters.user;

    if (to.matched.some(record => record.meta.requiresAuth)) {

        if (!user) {
            next({ name: 'SignUpOrLogin' });
        } else {
            next();
        }
    } else {
        next();
    }
});

router.beforeEach((to, from, next) => {
    const user = store.getters.user;

    if (to.matched.some(record => record.meta.requiresGuest)) {
        if (user) {
            next({ name: 'Home' });
        } else {
            next();
        }
    } else {
        next();
    }
});

router.beforeEach((to, from, next) => {
    const fullyRegistered = store.getters.isFullyRegistered;

    if (to.matched.some(record => record.meta.requiredFullyAuth)) {
        if (!fullyRegistered) {
            next({ name: 'Home' });
        } else {
            next();
        }
    } else {
        next();
    }
});

export default router;
