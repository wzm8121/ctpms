<!--
 * @Author: David Ma
 * @FilePath: \campus-trading-platform-management-system\CTPMS-web\src\components\auth\LoginForm.vue
 * @LastEditors: David Ma
 * @Description: 该文件用于...
 * @Date: 2025-03-08 13:54:47
-->
<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useAuthStore } from '@/stores/auth';
import type { AxiosError } from 'axios';
import { router } from '@/router';

const authStore = useAuthStore();
const checkbox = ref(true);
const captchaImage = ref('');
const studentId = ref('');
const passwordHash = ref('');
const captchaCode = ref('');
const captchaKey = ref('');
const loading = ref(false);
const errorMessage = ref('');

const handleLogin = async () => {
    loading.value = true;
    errorMessage.value = '';

    if (!studentId.value || !passwordHash.value || !captchaCode.value) {
        errorMessage.value = '请填写完整的登录信息';
        alert(errorMessage.value);
        loading.value = false;
        return;
    }

    try {
        const result = await authStore.login({
            studentId: studentId.value,
            passwordHash: passwordHash.value,
            vcode: captchaCode.value,
            captchaKey: captchaKey.value
        });
        console.log(result);
        if (result.code === 200) {
            alert(result.message);
            await router.push('/');
        } else {
            alert('登录失败：' + result.message);
            await refreshCaptcha();
        }
    } catch (error) {
        console.log(error);
        await refreshCaptcha();
    } finally {
        loading.value = false;
    }
};

const refreshCaptcha = async () => {
    try {
        const captcha = await authStore.getCaptcha();
        captchaImage.value = captcha.image;
        captchaKey.value = captcha.key;
    } catch (error) {

        console.error("获取验证码失败", error);
    }
};

onMounted(() => {
    refreshCaptcha();
});
</script>

<template>
    <v-row class="d-flex mb-3">
        <v-col cols="12">
            <v-label class="font-weight-bold mb-1">学号</v-label>
            <v-text-field v-model="studentId" variant="outlined" hide-details color="primary"></v-text-field>
        </v-col>
        <v-col cols="12">
            <v-label class="font-weight-bold mb-1">密码</v-label>
            <v-text-field v-model="passwordHash" variant="outlined" type="password" hide-details color="primary"></v-text-field>
        </v-col>
        <v-col cols="12">
            <v-label class="font-weight-bold mb-1">验证码</v-label>
            <div class="d-flex gap-2">
                <v-text-field
                    v-model="captchaCode"
                    variant="outlined"
                    hide-details
                    color="primary"
                ></v-text-field>
                <img
                    :src="captchaImage"
                    alt="验证码"
                    class="captcha-image"
                    @click="refreshCaptcha"
                    style="cursor: pointer; height: 56px; border-radius: 4px;"
                />
            </div>
        </v-col>
        <v-col cols="12" class="pt-0">
            <div class="d-flex flex-wrap align-center ml-n2">
                <v-checkbox v-model="checkbox" color="primary" hide-details>
                    <template v-slot:label class="text-body-1">记住我</template>
                </v-checkbox>
                <div class="ml-sm-auto">
                    <RouterLink to="/"
                                class="text-primary text-decoration-none text-body-1 opacity-1 font-weight-medium">
                        忘记密码 ?
                    </RouterLink>
                </div>
            </div>
        </v-col>
        <v-col cols="12" class="pt-0">
            <v-btn
                color="primary"
                size="large"
                block
                flat
                @click="handleLogin"
                :loading="loading"
            >
                登录
            </v-btn>
        </v-col>
    </v-row>
</template>

<style scoped>
.captcha-image {
    border: 1px solid #ddd;
    padding: 4px;
}
</style>
