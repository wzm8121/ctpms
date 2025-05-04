/*
 * @Author: David Ma
 * @FilePath: \campus-trading-platform-management-system\CTPMS_web\src\router\index.ts
 * @LastEditors: David Ma
 * @Description: 该文件用于...
 * @Date: 2025-03-08 13:54:47
 */
import { createRouter, createWebHistory } from 'vue-router';
import MainRoutes from './MainRoutes';
import AuthRoutes from './AuthRoutes';
import FaceRoutes from '@/router/FaceRoutes';
import ManagementRoutes from '@/router/ManagementRoutes';
import UserRoutes from '@/router/UserRoutes';

export const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),

    routes: [
        MainRoutes,
        AuthRoutes,
        FaceRoutes,
        ManagementRoutes,
        UserRoutes,
        {
            path: '/:pathMatch(.*)*',
            component: () => import('@/views/pages/Error404.vue')
        }
    ]
});
