<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '@/stores/auth';

const authStore = useAuthStore();
const router = useRouter();
const form = ref({
  name: '',
  email: '',
  password: '',
  confirmPassword: '',
  code: ''
});
const captchaImage = ref('');
const captchaKey = ref('');
const loading = ref(false);
const errorMessage = ref('');

const handleRegister = async () => {
  loading.value = true;
  errorMessage.value = '';

  if (!form.value.name || !form.value.email || !form.value.password || form.value.password !== form.value.confirmPassword || !form.value.code) {
    errorMessage.value = '请填写完整的注册信息并确保密码一致';
    alert(errorMessage.value);
    loading.value = false;
    return;
  }

  try {
    await authStore.register({
      name: form.value.name,
      email: form.value.email,
      password: form.value.password,
      code: form.value.code
    });
    alert('注册成功');
    await router.push('/auth/login');
  } catch (error: any) {
    errorMessage.value = error.response?.data?.message || '注册失败，请稍后重试';
    alert(errorMessage.value);
    refreshCaptcha();
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
  <v-form @submit.prevent="handleRegister">
    <v-row class="d-flex mb-3">
      <v-col cols="12">
        <v-label class="font-weight-bold mb-1">用户名</v-label>
        <v-text-field
            v-model="form.name"
            variant="outlined"
            hide-details
            color="primary"
            required
        ></v-text-field>
      </v-col>
      <v-col cols="12">
        <v-label class="font-weight-bold mb-1">邮箱</v-label>
        <v-text-field
            v-model="form.email"
            variant="outlined"
            type="email"
            hide-details
            color="primary"
            required
        ></v-text-field>
      </v-col>
      <v-col cols="12">
        <v-label class="font-weight-bold mb-1">密码</v-label>
        <v-text-field
            v-model="form.password"
            variant="outlined"
            type="password"
            hide-details
            color="primary"
            required
        ></v-text-field>
      </v-col>
      <v-col cols="12">
        <v-label class="font-weight-bold mb-1">确认密码</v-label>
        <v-text-field
            v-model="form.confirmPassword"
            variant="outlined"
            type="password"
            hide-details
            color="primary"
            :rules="[() => form.password === form.confirmPassword || '两次输入的密码不一致']"
            required
        ></v-text-field>
      </v-col>
      <v-col cols="12">
        <v-label class="font-weight-bold mb-1">验证码</v-label>
        <div class="d-flex gap-2">
          <v-text-field
              v-model="form.code"
              variant="outlined"
              hide-details
              color="primary"
              required
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
      <v-col cols="12">
        <v-btn
            type="submit"
            color="primary"
            size="large"
            block
            flat
            :loading="loading"
            :disabled="!form.name || !form.email || !form.password || form.password !== form.confirmPassword || !form.code"
        >
          注册
        </v-btn>
      </v-col>
    </v-row>
  </v-form>
</template>

<style scoped>
.captcha-image {
  border: 1px solid #ddd;
  padding: 4px;
}
</style>
