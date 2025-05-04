const UserRoutes = {
    path: '/user',
    component: () => import('@/layouts/full/FullLayout.vue'),
    meta: {
        requiresAuth: true
    },
    children: [
        {
            name: 'Profile',
            path: '/user/profile',
            component: () => import('@/views/user/Profile.vue')
        },
        // {
        //     name: 'Category',
        //     path: '/management/category',
        //     component: () => import('@/views/managements/CategoryManagement.vue')
        // },
        // {
        //     name: 'Product',
        //     path: '/management/product',
        //     component: () => import('@/views/managements/ProductManagement.vue')
        // },
        // {
        //     name: 'ProductReviewManagement',
        //     path: '/management/productReview',
        //     component: () => import('@/views/managements/ProductReviewManagement.vue')
        // },

    ]
};

export default UserRoutes;
