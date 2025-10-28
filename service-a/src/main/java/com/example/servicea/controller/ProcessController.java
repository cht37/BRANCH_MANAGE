package com.example.servicea.controller;

import com.example.servicea.model.BatchDataResponse;
import com.example.servicea.service.ProcessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class ProcessController {

    private final ProcessService processService;

    @GetMapping("/process")
    public BatchDataResponse processBatchData(@RequestParam(defaultValue = "200") int count) {
        log.info("Service A received request to process {} items", count);
        return processService.processBatchData(count);
    }
}