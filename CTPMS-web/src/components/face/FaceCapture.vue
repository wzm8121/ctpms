<template>
    <div class="text-center">
        <v-card class="pa-5" outlined>
            <v-card-title>{{ title }}</v-card-title>
            <div class="video-container">
                <video ref="videoRef" autoplay class="video"></video>
            </div>

            <!-- 反馈提示 -->
            <v-alert v-if="errorMessage" type="error" class="mt-3">
                {{ errorMessage }}
            </v-alert>

            <canvas ref="canvasRef" style="display: none"></canvas>
            <v-img v-if="capturedImage" :src="capturedImage" class="mt-3 mx-auto d-block" max-width="300"></v-img>

            <!-- 手动拍照 & 上传按钮 -->
            <v-btn color="primary" class="mt-3" @click="captureImage">📸 截取图片</v-btn>
            <v-btn color="success" class="mt-3 ml-3" :disabled="!capturedImage" @click="uploadImage">⬆️ 上传图片</v-btn>
        </v-card>
    </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, defineProps, defineEmits } from "vue";

const props = defineProps<{ title: string }>();
const emits = defineEmits(["imageCaptured"]);

const videoRef = ref<HTMLVideoElement | null>(null);
const canvasRef = ref<HTMLCanvasElement | null>(null);
const capturedImage = ref<string | null>(null);
const errorMessage = ref<string | null>(null);

// 启动摄像头
onMounted(() => {
    navigator.mediaDevices.getUserMedia({ video: true })
        .then((stream) => {
            if (videoRef.value) videoRef.value.srcObject = stream;
        })
        .catch((err) => {
            errorMessage.value = `无法访问摄像头: ${err.message}`;
            alert(errorMessage.value);
        });
});

// 拍照
const captureImage = () => {
    if (!videoRef.value || !canvasRef.value) return;
    const canvas = canvasRef.value;
    const ctx = canvas.getContext("2d");
    if (!ctx) return;

    canvas.width = videoRef.value.videoWidth;
    canvas.height = videoRef.value.videoHeight;
    ctx.drawImage(videoRef.value, 0, 0, canvas.width, canvas.height);

    capturedImage.value = canvas.toDataURL("image/png");
};

// 上传图片
const uploadImage = () => {
    if (!capturedImage.value) return;
    emits("imageCaptured", capturedImage.value);
};

// 组件销毁时释放摄像头资源
onUnmounted(() => {
    if (videoRef.value?.srcObject) {
        (videoRef.value.srcObject as MediaStream).getTracks().forEach(track => track.stop());
    }
});
</script>

<style scoped>
.video-container {
    display: flex;
    justify-content: center;
    margin-top: 10px;
}
.video {
    width: 100%;
    max-width: 400px;
    border-radius: 10px;
    border: 2px solid #42a5f5;
}
</style>
