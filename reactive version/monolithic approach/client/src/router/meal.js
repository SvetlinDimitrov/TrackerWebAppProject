export default [
    {
        path: '/meal/create',
        name: 'CreateMeal',
        component: () => import('../views/meal/CreateMeal.vue'),
        meta: { requiresAuth: true }
    },
    {
        path: '/meal/:id/edit',
        name: 'EditMeal',
        component: () => import('../views/meal/EditMeal.vue'),
        meta: { requiresAuth: true }
    }
    ,
    {
        path: '/meal/:id/delete',
        name: 'DeleteMeal',
        component: () => import('../views/meal/DeleteMeal.vue'),
        meta: { requiresAuth: true }
    },
];