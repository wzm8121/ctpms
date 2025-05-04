<template>
    <v-container class="text-center">
        <FaceCapture title="人脸识别" @imageCaptured="handleImage" />

        <v-alert v-if="resultMessage" :type="resultSuccess ? 'success' : 'error'" class="mt-3">
            {{ resultMessage }}
        </v-alert>
    </v-container>
</template>

<script setup lang="ts">
import { ref } from "vue";
import FaceCapture from "@/components/face/FaceCapture.vue";

const resultMessage = ref<string | null>(null);
const resultSuccess = ref(false);

// 处理识别
const handleImage = async (imageData: string) => {
    try {
        const blob = await dataURLtoBlob(imageData);
        const formData = new FormData();
        formData.append("file", blob, "face.png");

        const response = await fetch("http://localhost:3000/api/face/verify", {
            method: "POST",
            body: formData,
        });

        const result = await response.json();
        resultSuccess.value = result.code === 200 && result.data.matched;
        resultMessage.value = resultSuccess.value
            ? `✅ 识别成功！用户 ID: ${result.data.userId}`
            : "❌ 识别失败";
    } catch (error) {
        resultMessage.value = "❌ 服务器错误";
        resultSuccess.value = false;
    }
};

// Base64 转 Blob
const dataURLtoBlob = async (dataUrl: string) => {
    const res = await fetch(dataUrl);
    return res.blob();
};
</script>
