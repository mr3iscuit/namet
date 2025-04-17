package com.biscuit.namet.service;

import com.biscuit.namet.dto.DataRequest;
import com.biscuit.namet.dto.DataResponse;
import com.biscuit.namet.entity.DataEntity;
import com.biscuit.namet.mapper.DataMapper;
import com.biscuit.namet.repository.DataRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    private final DataRepository dataRepository;
    @Qualifier("dataMapperImpl")
    private final DataMapper mapper;
    private final IUserINfoHandler userINfoHandler;

    @Transactional
    public DataResponse store(DataRequest dataRequest) {

        log.info("Temperature is above 27 degrees");
        userINfoHandler.handleData(dataRequest);

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


    public DataResponse getLatest() {

        DataEntity entity = dataRepository
                .findFirstByOrderByCreatedAtDesc()
                .orElseThrow(() -> new RuntimeException("Data not found"));

        return mapper.toDataResponse(entity);
    }
}
