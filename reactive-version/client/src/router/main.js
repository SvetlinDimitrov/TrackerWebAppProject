export default [
    {
        path: '/',
        name: 'Home',
        component: () => import('../views/main/Home.vue'),
    },
    {
        path: '/about',
        name: 'About',
        component: () => import('../views/main/About.vue')
    },
    {
        path: '/settings',
        name: 'Settings',
        component: () => import('../views/main/Settings.vue'),
        meta: {requiresAuth: true}
    },
    {
        path: '/performance',
        name: 'Performance',
        component: () => import('../views/main/performance/Performance.vue'),
        meta: {requiredFullyAuth: true}
    },
    {
        path: '/performance/edit/:name/:intake',
        name: 'EditNutritionPerformance',
        component: () => import('../views/main/performance/EditNutritionPerformance.vue'),
        meta: {requiredFullyAuth: true}
    },
    {
        path: '/settings/edit',
        name: 'Edit',
        component: () => import('../views/main/Edit.vue'),
        meta: {requiresAuth: true}
    },
    {
        path: '/settings/account-remove',
        name: 'DeleteAccount',
        component: () => import('../views/main/DeleteAccount.vue'),
        meta: {requiresAuth: true}
    },
    {
        path: '/settings/logout',
        name: 'Logout',
        component: () => import('../views/main/Logout.vue'),
        meta: {requiresAuth: true}
    },
    {
        path: '/register',
        name: 'Register',
        component: () => import('../views/main/Register.vue'),
        meta: {requiresGuest: true}
    },
    {
        path: '/email-verification',
        name: 'EmailVerifier',
        component: () => import('../views/main/EmailVerifier.vue'),
        meta: {requiresGuest: true},
    },
    {
        path: '/recreate-password',
        name: 'RecreatePassword',
        component: () => import('../views/main/RecreatePassword.vue'),
        meta: {requiresGuest: true}
    },
    {
        path: '/forgot-account',
        name: 'ForgotAccount',
        component: () => import('../views/main/ForgotAccount.vue'),
        meta: {requiresGuest: true}
    },
    {
        path: '/nutri-info/:name',
        name: 'NutriInfo',
        component: () => import('../views/main/NutriInfo.vue')
    },
    {
        path: '/sign-login',
        name: 'SignUpOrLogin',
        component: () => import('../views/main/SignUpOrLogin.vue'),
        meta: {requiresGuest: true}
    },
];