package ru.mtuci.coursemanagement.controller;

import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.InetAddress;
import java.net.URI;

@RestController
public class ProxyController {
    @GetMapping("/api/proxy")
    public String proxy(@RequestParam("targetUrl") String targetUrl) {
        // Необходимо добавить белый список
        // Ограничение частоты запросов за период
        // Проверка URI
        try {
            URI uri = new URI(targetUrl);
            if (!"https".equals(uri.getScheme())) {
                return "NOT";
            }
            String host = uri.getHost();
            InetAddress inetAddress = InetAddress.getByName(host);
            // Внутренние адреса
            boolean isNotLocal = !inetAddress.isSiteLocalAddress() &&
                    !inetAddress.isLoopbackAddress() &&
                    !inetAddress.isAnyLocalAddress();
            if (!isNotLocal){
                return "NOT";
            }
        } catch (Exception e) {
            return "NOT";
        }
        // Настройка Http соединений
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(5000);
        requestFactory.setReadTimeout(5000);
        RestTemplate rt = new RestTemplate(requestFactory);

        return rt.getForObject(targetUrl, String.class);
    }
}
