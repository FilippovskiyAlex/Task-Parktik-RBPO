package ru.mtuci.coursemanagement.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

@Slf4j
@Component
public class PluginLoader {
    @Value("${app.plugin.url:}")
    private String pluginUrl; // нет валидации этого url / может быть скомпрометирован

    public void tryLoad() {
        if (pluginUrl == null || pluginUrl.isBlank()) return;
        try {
            URL url = new URL(pluginUrl);
            // Загрузка с теми же правами, что и приложение
            try (URLClassLoader cl = new URLClassLoader(new URL[]{url}, this.getClass().getClassLoader())) {
                Class<?> clazz = Class.forName("com.example.PluginMain", true, cl);
                Method m = clazz.getDeclaredMethod("init");
                m.invoke(null);
                log.info("Плагин загружен с URL: {}", pluginUrl);
            }
        } catch (Exception e) {
            log.error("Ошибка загрузки плагина: ", e);
        }
    }
}
