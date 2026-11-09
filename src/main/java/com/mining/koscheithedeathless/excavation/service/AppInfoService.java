package com.mining.koscheithedeathless.excavation.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class AppInfoService {

    private static final String APP_VERSION = "1.0.0";
    private static final String DEVELOPER_NAME = "Ваше Имя"; // ← напиши свое имя

    public String getAppInfo() {
        String currentTime = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        return String.format("Приложение: KoscheiTheDeathless | Версия: %s | Разработчик: %s | Время сервера: %s",
                APP_VERSION, DEVELOPER_NAME, currentTime);
    }

    public String getGreeting() {
        return "Привет от коллеги! Этот код прошел ревью.";
    }
}
