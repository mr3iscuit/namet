package com.biscuit.namet.service;

public interface IBotService {

    void sendTelegramNotification(
            String message,
            String chatId
    );
}
