package com.mzw.ctpmsbackend.ollama;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class OllamaClient {

    private static final String OLLAMA_API_URL = "http://121.40.250.141:11434/api/chat";
    private static final String MODEL_NAME = "deepseek-r1:1.5b";

    private static final int MAX_RETRIES = 3;
    private static final int RETRY_INTERVAL_MS = 2000; // 每次重试间隔 2 秒

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build();

    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * 发起带重试的 DeepSeek 模型调用
     * @param prompt 提示词
     * @return 模型返回结果
     */
    public String chat(String prompt) {
        for (int i = 0; i < MAX_RETRIES; i++) {
            try {
                return doChat(prompt);
            } catch (SocketTimeoutException e) {
                log.warn("第 {} 次调用超时，重试中...", i + 1);
            } catch (IOException e) {
                log.warn("第 {} 次调用失败（IO异常），重试中...", i + 1, e);
            } catch (Exception e) {
                log.error("第 {} 次调用失败（其他异常），不再重试", i + 1, e);
                break;
            }

            try {
                Thread.sleep(RETRY_INTERVAL_MS);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        return "访问失败：调用模型超时或异常";
    }

    /**
     * 实际请求逻辑，支持 max_tokens 限制
     */
    private String doChat(String prompt) throws IOException {
        // 构建 messages 数组
        ArrayNode messages = mapper.createArrayNode();
        ObjectNode userMessage = mapper.createObjectNode();
        userMessage.put("role", "user");
        userMessage.put("content", prompt);
        messages.add(userMessage);

        // 构建请求 JSON
        ObjectNode requestJson = mapper.createObjectNode();
        requestJson.put("model", MODEL_NAME);
        requestJson.put("stream", false);
        requestJson.set("messages", messages);

        // 设置 max_tokens 限制生成长度
        ObjectNode options = mapper.createObjectNode();
        options.put("max_tokens", 300); // 限制生成最多 300 token
        requestJson.set("options", options);

        String requestBody = mapper.writeValueAsString(requestJson);

        Request request = new Request.Builder()
                .url(OLLAMA_API_URL)
                .post(RequestBody.create(requestBody, MediaType.parse("application/json")))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                log.error("Ollama 请求失败，状态码: {}", response.code());
                throw new IOException("响应状态错误：" + response.code());
            }

            String responseBody = response.body().string();
            log.info("收到 Ollama 响应: {}", responseBody);

            JsonNode responseJson = mapper.readTree(responseBody);
            JsonNode messageNode = responseJson.get("message");

            if (messageNode != null && messageNode.has("content")) {
                return messageNode.get("content").asText();
            } else {
                log.error("响应缺少 message.content 字段: {}", responseBody);
                throw new IOException("响应格式错误：缺少 message.content");
            }
        }
    }
}


