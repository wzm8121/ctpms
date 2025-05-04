package com.mzw.ctpmsbackend.service;

import java.io.IOException;

public interface OllamaService {
    String chat(String prompt) throws IOException;
}
