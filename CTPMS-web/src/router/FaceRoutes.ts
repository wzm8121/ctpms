const FaceRoutes = {
    path: '/face',
    component: () => import('@/layouts/blank/BlankLayout.vue'),
    meta: {
        requiresAuth: false
    },
    children: [
        {
            name: 'Register',
            path: '/face/register',
            component: () => import('@/views/face/RegisterFace.vue')
        },
        {
            name: 'Verify',
            path: '/face/verify',
            component: () => import('@/views/face/VerifyFace.vue')
        },
    ]
};

export default FaceRoutes;
