package com.example.serviceb.controller;

import com.example.serviceb.model.BatchDataRequest;
import com.example.serviceb.model.BatchDataResponse;
import com.example.serviceb.service.ProcessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class ProcessController {

    private final ProcessService processService;

    @PostMapping("/process")
    public BatchDataResponse processBatchData(@RequestBody BatchDataRequest request) {
        log.info("Service B received batch request with {} items", request.getItems().size());
        return processService.processBatch(request);
    }
}