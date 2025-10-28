package com.example.servicec.controller;

import com.example.servicec.model.DataItem;
import com.example.servicec.service.ProcessService;
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
    public DataItem processData(@RequestBody DataItem dataItem) {
        log.info("Service C received request for data item: {}", dataItem.getId());
        return processService.process(dataItem);
    }
}