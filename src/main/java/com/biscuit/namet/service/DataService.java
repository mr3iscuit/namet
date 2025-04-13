package com.biscuit.namet.service;

import com.biscuit.namet.config.Telegram;
import com.biscuit.namet.dto.DataRequest;
import com.biscuit.namet.dto.DataResponse;
import com.biscuit.namet.entity.DataEntity;
import com.biscuit.namet.mapper.DataMapper;
import com.biscuit.namet.repository.DataRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataService {

    private final Telegram telegram;
    private final DataRepository dataRepository;
    @Qualifier("dataMapperImpl")
    private final DataMapper mapper;

    @Transactional
    public DataResponse store(DataRequest dataRequest) {

        if (Double.compare(
                dataRequest.getTemperature(),
                27
        ) > 0) {
            log.info("Temperature is above 27 degrees");
            sendTelegramNotification(dataRequest.toString());
        }

        DataEntity newEntity = mapper.toEntity(dataRequest);
        newEntity.setCreatedAt(LocalDateTime.now());

        DataEntity stored = dataRepository.save(newEntity);
        return mapper.toDataResponse(stored);
    }

    public Page<DataResponse> getPage(
            int page,
            int size
    ) {
        return dataRepository
                .findAll(
                        PageRequest.of(
                                page,
                                size,
                                Sort.by(
                                        Sort.Direction.DESC,
                                        "createdAt"
                                )
                        )
                )
                .map(mapper::toDataResponse);
    }

    private void sendTelegramNotification(String message) {
        String botToken = telegram.getBotToken();
        String chatId = telegram.getChatId();

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
