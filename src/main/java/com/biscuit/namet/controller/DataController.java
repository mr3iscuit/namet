package com.biscuit.namet.controller;

import com.biscuit.namet.dto.DataRequest;
import com.biscuit.namet.dto.DataResponse;
import com.biscuit.namet.service.DataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/data")
@Slf4j
@RequiredArgsConstructor
public class DataController {

    private final DataService dataService;

    @PostMapping()
    @ResponseStatus(HttpStatus.OK)
    public DataResponse store(
            @RequestBody DataRequest dataRequest
    ) {
        log.info(dataRequest.toString());
        return dataService.store(dataRequest);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public Page<DataResponse> getPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return dataService.getPage(
                page,
                size
        );
    }

    @GetMapping("/latest")
    @ResponseStatus(HttpStatus.OK)
    public DataResponse getLatest() {
        return dataService.getLatest();
    }
}
