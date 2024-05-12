import {createRouter, createWebHistory} from 'vue-router';
import store from '../store';
import mainRoutes from './main';
import mealRoutes from './meal';
import foodRoutes from './food';

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
