package com.mzw.ctpmsbackend.ollama;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class OllamaAutoStartService implements CommandLineRunner {

    private static final String MODEL_NAME = "deepseek-r1:7b";

    @Override
    public void run(String... args) {
        try {
            if (!isModelRunning()) {
                log.info("模型 [{}] 未在运行，尝试启动...", MODEL_NAME);
                startModel();
                log.info("模型 [{}] 启动指令已发送", MODEL_NAME);
            } else {
                log.info("模型 [{}] 已在运行，无需启动", MODEL_NAME);
            }
        } catch (Exception e) {
            log.error("启动 Ollama 模型失败: {}", e.getMessage(), e);
        }
    }

    private boolean isModelRunning() {
        try {
            URL url = new URL("http://10.8.0.10:11434/api/tags");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            String result = new String(conn.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            return result.contains(MODEL_NAME);
        } catch (IOException e) {
            log.warn("无法连接到 Ollama，可能未启动: {}", e.getMessage());
            return false;
        }
    }

    private void startModel() throws IOException {
        ProcessBuilder pb = new ProcessBuilder("ollama", "run", MODEL_NAME);
        pb.redirectErrorStream(true);
        pb.start(); // 不阻塞启动过程
    }
}
