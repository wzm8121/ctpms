/*
 * @Author: David Ma
 * @FilePath: \campus-trading-platform-management-system\CTPMS-web\src\api\auth.ts
 * @LastEditors: David Ma
 * @Description: 该文件用于...
 * @Date: 2025-03-09 15:14:26
 */
import axios from 'axios';

const API_BASE_URL = 'http://localhost:3000';

/**
 * 获取验证码 key
 */
export const getCaptchaKey = async (): Promise<string> => {
    const response = await axios.get(`${API_BASE_URL}/api/user/getVCodeKey`);

    return response.data.data; // 直接获取返回的 captchaKey
};

/**
 * 获取验证码图片
 */
export const getCaptcha = async (): Promise<{ image: string; key: string }> => {
    const captchaKey = await getCaptchaKey();
    const response = await axios.get(`${API_BASE_URL}/api/user/getVCode`, {
        params: { captchaKey },
        responseType: 'blob' // 确保获取的是图片数据
    });

    return {
        image: URL.createObjectURL(response.data), // 创建图片 URL
        key: captchaKey // 关联的验证码 key
    };
};

/**
 * 校验验证码
 */
export const checkCaptcha = async (vcode: string, captchaKey: string) => {
    const response = await axios.post(`${API_BASE_URL}/api/user/check/code/${vcode}`, null, {
        params: { captchaKey }
    });
    console.log(response);
    return response.data;
};

/**
 * 发送注册验证码
 */
export const sendVerificationCode = async (email: string) => {
    const response = await axios.post(`${API_BASE_URL}/register/sendCode`, null, { params: { email } });
    return response.data;
};

/**
 * 注册
 */
export const register = async (data: { username: string; email: string; password: string; code: string }) => {
    const response = await axios.post(
        `${API_BASE_URL}/register`,
        {
            userName: data.username,
            email: data.email,
            passwordHash: data.password
        },
        { params: { code: data.code } }
    );
    return response.data;
};

/**
 * 登录
 */
export const login = async (credentials: { studentId: string; passwordHash: string; vcode: string; captchaKey: string }) => {
    const response = await axios.post(
        `${API_BASE_URL}/api/user/login/${credentials.vcode}`,
        {
            studentId: credentials.studentId,
            passwordHash: credentials.passwordHash
        },
        {
            params: { captchaKey: credentials.captchaKey }
        }
    );
    return response.data;
};
