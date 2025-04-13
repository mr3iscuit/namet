package com.biscuit.namet.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class Telegram {

    @Value("${telegram.bot.token}")
    private String botToken;

    @Value("${telegram.bot.token}")
    private String chatId;
}
