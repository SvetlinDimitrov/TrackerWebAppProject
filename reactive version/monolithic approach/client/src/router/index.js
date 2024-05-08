import {createRouter, createWebHistory} from 'vue-router';
import store from '../store';

const routes = [
    {
        path: '/',
        name: 'Home',
        component: () => import('../views/Home.vue'),
        children: [
            {
                path: 'meal/create',
                name: 'CreateMeal',
                component: () => import('../views/meal/CreateMeal.vue'),
            },
            {
                path: 'meal/:id/edit',
                name: 'EditMeal',
                component: () => import('../views/meal/EditMeal.vue'),
            }
            ,
            {
                path: 'meal/:id/delete',
                name: 'DeleteMeal',
                component: () => import('../views/meal/DeleteMeal.vue'),
            }
            ,
            {
                path: 'meal/:id/insert-food',
                name: 'InsertFood',
                component: () => import('../views/meal/InsertFood.vue'),
                children: [
                    {
                        path: 'common/:name',
                        name: 'CommonFood',
                        component: () => import('../views/meal/CommonFood.vue'),
                    },
                    {
                        path: 'branded/:foodId',
                        name: 'BrandedFood',
                        component: () => import('../views/meal/BrandedFood.vue'),
                    }
                ]
            }
        ]
    },
    {
        path: '/about',
        name: 'About',
        component: () => import('../views/About.vue')
    },
    {
        path: '/settings',
        name: 'Settings',
        component: () => import('../views/Settings.vue'),
        meta: { requiresAuth: true }
    },
    {
        path: '/settings/edit',
        name: 'Edit',
        component: () => import('../views/Edit.vue'),
        meta: { requiresAuth: true }
    },
    {
        path: '/settings/account-remove',
        name: 'DeleteAccount',
        component: () => import('../views/DeleteAccount.vue'),
        meta: { requiresAuth: true }
    },
    {
        path: '/settings/logout',
        name: 'Logout',
        component: () => import('../views/Logout.vue'),
        meta: { requiresAuth: true }
    },
    {
        path: '/register',
        name: 'Register',
        component: () => import('../views/CreateAccount.vue'),
        meta: { requiresGuest: true }
    },
    {
        path: '/nutri-info',
        name: 'NutriInfo',
        component: () => import('../views/NutriInfo.vue')
    },
    {
        path: '/sign-login',
        name: 'SignUpOrLogin',
        component: () => import('../views/SignUpOrLogin.vue'),
        meta: { requiresGuest: true }
    },
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

export default router;
