package com.mzw.ctpmsbackend.service.impl;

import com.mzw.ctpmsbackend.service.OllamaService;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class OllamaServiceImpl implements OllamaService {

    private static final String OLLAMA_URL = "http://localhost:11434/api/chat";

    private final OkHttpClient client = new OkHttpClient();

    @Override
    public String chat(String prompt) throws IOException {
        String json = "{\n" +
                "  \"model\": \"deepseek-r1:1.5b\",\n" +
                "  \"messages\": [\n" +
                "    {\"role\": \"user\", \"content\": \"" + prompt + "\"}\n" +
                "  ],\n" +
                "  \"stream\": false\n" +
                "}";

        RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));
        Request request = new Request.Builder().url(OLLAMA_URL).post(body).build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            String responseBody = response.body().string();
            // 解析 content 字段
            return parseContent(responseBody);
        }
    }

    private String parseContent(String responseJson) {
        // 你可以用更严格的 JSON 解析方式，如使用 Jackson
        int idx = responseJson.indexOf("\"content\":\"");
        if (idx != -1) {
            String content = responseJson.substring(idx + 11);
            int endIdx = content.indexOf("\"");
            return content.substring(0, endIdx);
        }
        return "无法解析内容";
    }
}
