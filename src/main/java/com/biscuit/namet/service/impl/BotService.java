package com.biscuit.namet.service.impl;

import com.biscuit.namet.config.Telegram;
import com.biscuit.namet.service.IBotService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BotService implements IBotService {

    private final Telegram telegram;

    @Override
    public void sendTelegramNotification(
            String message,
            String chatId
    ) {
        String botToken = telegram.getBotToken();

        String url = String.format(
                "https://api.telegram.org/bot%s/sendMessage",
                botToken
        );

        String jsonPayload = String.format(
                "{\"chat_id\": \"%s\", \"text\": \"%s\"}",
                chatId,
                message
        );

        try {
            java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();
            java.net.http.HttpRequest request = java.net.http.HttpRequest
                    .newBuilder()
                    .uri(java.net.URI.create(url))
                    .header(
                            "Content-Type",
                            "application/json"
                    )
                    .POST(java.net.http.HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build()
                    ;
            client.send(
                    request,
                    java.net.http.HttpResponse.BodyHandlers.ofString()
            );
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
