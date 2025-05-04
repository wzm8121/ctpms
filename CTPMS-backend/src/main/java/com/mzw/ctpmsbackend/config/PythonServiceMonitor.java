/*
 * @Author: David Ma
 * @FilePath: \campus-trading-platform-management-system\CTPMS-backend\src\main\java\com\mzw\ctpmsbackend\config\PythonServiceMonitor.java
 * @LastEditors: David Ma
 * @Description: 该文件用于...
 * @Date: 2025-03-05 22:19:23
 */
package com.mzw.ctpmsbackend.config;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class PythonServiceMonitor {

    private Process process;
    private Thread outputThread;

    @Scheduled(initialDelay = 0, fixedRate = 60000)
    public void checkAndStartPythonService() {
        if (process == null || !process.isAlive()) {
            try {
                // 清理之前的进程
                if (process != null) {
                    process.destroy();
                }
                if (outputThread != null) {
                    outputThread.interrupt();
                }

                // 获取当前时间戳
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                // 启动日志
                System.out.println("[" + timestamp + "] [INFO] ⚡️ 正在启动人脸识别服务...");

                // 使用虚拟环境的 Python 解释器
                String pythonCommand = "A:\\MyProject\\python\\venv\\Scripts\\python.exe";
                String scriptPath = "D:\\AProject\\campus-trading-platform-management-system\\face-recognition-service\\app.py";
                File workingDir = new File("D:\\AProject\\campus-trading-platform-management-system");

                process = new ProcessBuilder(pythonCommand, scriptPath)
                        .directory(workingDir)
                        .redirectErrorStream(true)
                        .start();

                // 在单独的线程中读取输出
                outputThread = new Thread(() -> {
                    try (BufferedReader reader = new BufferedReader(
                            new InputStreamReader(process.getInputStream()))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            String outputTimestamp = LocalDateTime.now()
                                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                            System.out.println("[" + outputTimestamp + "] [PYTHON] " + line);
                        }
                    } catch (IOException e) {
                        String errorTimestamp = LocalDateTime.now()
                                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                        System.err.println("[" + errorTimestamp + "] [ERROR] 读取Python输出失败: " + e.getMessage());
                    }
                });
                outputThread.start();

                // 成功日志
                System.out.println("[" + timestamp + "] [INFO] ✅ 人脸识别服务已启动...");
            } catch (IOException e) {
                String errorTimestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                System.err.println("[" + errorTimestamp + "] [ERROR] ❌ 启动人脸识别服务失败: " + e.getMessage());
            }
        }
    }

    public void destroy() {
        if (process != null) {
            process.destroy();
        }
        if (outputThread != null) {
            outputThread.interrupt();
        }
    }
}
