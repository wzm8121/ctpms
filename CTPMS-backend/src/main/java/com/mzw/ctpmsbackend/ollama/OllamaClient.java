package com.mzw.ctpmsbackend.ollama;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class OllamaClient {

    private static final String OLLAMA_API_URL = "http://10.8.0.10:11434/api/generate";

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)  // 连接超时
            .writeTimeout(10, TimeUnit.SECONDS)    // 写入超时
            .readTimeout(60, TimeUnit.SECONDS)     // 读取超时（这里是重点）
            .build();

    private static final ObjectMapper mapper = new ObjectMapper();

    public String chat(String prompt) {
        try {
            ObjectMapper mapper = new ObjectMapper();

            // 构造 JSON 请求体
            ObjectNode jsonNode = mapper.createObjectNode();
            jsonNode.put("model", "deepseek-r1:7b");
            jsonNode.put("prompt", prompt);
            jsonNode.put("stream", false);

            String requestBody = mapper.writeValueAsString(jsonNode);

            Request request = new Request.Builder()
                    .url(OLLAMA_API_URL)
                    .post(RequestBody.create(requestBody, MediaType.parse("application/json")))
                    .build();

            try (Response response = client.newCall(request).execute()) {
                String body = response.body().string();
                log.info("收到 Ollama 响应: {}", body);

                JsonNode responseJson = mapper.readTree(body);
                JsonNode responseNode = responseJson.get("response");

                if (responseNode == null) {
                    log.error("Ollama 响应中未包含 response 字段: {}", body);
                    throw new RuntimeException("模型响应格式错误：缺少 response 字段");
                }

                return responseNode.asText();
            }
        } catch (IOException e) {
            log.error("调用 DeepSeek 模型失败", e);
            return "访问失败：无法访问模型";
        } catch (Exception e) {
            log.error("解析 Ollama 响应失败", e);
            return "访问失败：模型响应异常";
        }
    }

}
