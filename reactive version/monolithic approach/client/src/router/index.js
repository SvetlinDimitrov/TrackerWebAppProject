import {createRouter, createWebHistory} from 'vue-router';

const routes = [
    {
        path: '/',
        name: 'Home',
        component: () => import('../views/Home.vue')
    },
    {
        path: '/about',
        name: 'About',
        component: () => import('../views/About.vue')
    },
    {
        path: '/settings',
        name: 'Settings',
        component: () => import('../views/Settings.vue')
    },
    {
        path: '/settings/edit',
        name: 'Edit',
        component: () => import('../views/Edit.vue')
    },
    {
        path: '/login',
        name: 'Login',
        component: () => import('../views/Login.vue')
    },
    {
        path: '/register',
        name: 'Register',
        component: () => import('../views/CreateAccount.vue')
    },
    {
        path: '/nutri-info',
        name: 'NutriInfo',
        component: () => import('../views/NutriInfo.vue')
    },
    {
        path: '/sign-login',
        name: 'SignUpOrLogin',
        component: () => import('../views/SignUpOrLogin.vue')
    }


];

const router = createRouter({
    history: createWebHistory(),
    routes
});

export default router;
