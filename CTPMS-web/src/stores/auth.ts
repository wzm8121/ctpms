/*
 * @Author: David Ma
 * @FilePath: \campus-trading-platform-management-system\CTPMS-web\src\stores\auth.ts
 * @LastEditors: David Ma
 * @Description: 该文件用于...
 * @Date: 2025-03-09 15:13:30
 */
import { defineStore } from 'pinia';
import { ref } from 'vue';
import axios from 'axios';
import { login as apiLogin, getCaptcha as apiGetCaptcha } from '@/api/auth';

const API_BASE_URL = 'http://localhost:3000';

export const useAuthStore = defineStore('auth', () => {
    const token = ref('');
    const user = ref(null);

    const login = async (credentials: { studentId: string; passwordHash: string; vcode: string; captchaKey: string }) => {
        try {
            const result = await apiLogin(credentials);

            if (result.code === 200) {
                // 保存token到localStorage
                localStorage.setItem('token', result.data.token.tokenValue);
                localStorage.setItem('user', JSON.stringify(result.data.user));
                // 保存用户信息到pinia store
                token.value = result.data.token.tokenValue;
                user.value = result.data.user;
            }
            return result;
        } catch (error) {
            console.error('登录失败:' + error);
            throw error;
        }
    };

    const getCaptcha = async () => {
        return await apiGetCaptcha();
    };

    const register = async (userInfo: { name: string; email: string; password: string; code: string }) => {
        try {
            const result = await axios.post(
                `${API_BASE_URL}/register`,
                {
                    name: userInfo.name,
                    email: userInfo.email,
                    password: userInfo.password
                },
                {
                    params: { code: userInfo.code }
                }
            );

            if (result.data.code === 200) {
                alert('注册成功');
                return result;
            } else {
                throw new Error(result.data.message || '注册失败');
            }
        } catch (error: any) {
            console.error('注册失败:', error);
            alert(error.response?.data?.message || '注册过程中发生错误');
            throw error;
        }
    };

    const sendVerificationCode = async (email: string) => {
        try {
            const result = await axios.post(`${API_BASE_URL}/register/sendCode`, null, {
                params: { email }
            });
            if (result.data.code === 200) {
                return result;
            } else {
                throw new Error(result.data.message || '发送验证码失败');
            }
        } catch (error: any) {
            console.error('发送验证码失败:', error);
            alert(error.response?.data?.message || '发送验证码过程中发生错误');
            throw error;
        }
    };

    return {
        token,
        user,
        login,
        getCaptcha,
        sendVerificationCode,
        register
    };
});
