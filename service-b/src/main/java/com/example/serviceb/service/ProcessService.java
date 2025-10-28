package com.example.serviceb.service;

import com.example.serviceb.model.BatchDataRequest;
import com.example.serviceb.model.BatchDataResponse;
import com.example.serviceb.model.DataItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProcessService {

    private final RestTemplate restTemplate;
    private final ThreadPoolTaskExecutor asyncTaskExecutor;

    @Value("${service.c.url}")
    private String serviceCUrl;

    public BatchDataResponse processBatch(BatchDataRequest request) {
        long startTime = System.currentTimeMillis();
        List<DataItem> items = request.getItems();
        
        log.info("Starting to process {} items with thread pool", items.size());
        
        // 创建异步任务列表
        List<CompletableFuture<DataItem>> futures = new ArrayList<>();
        
        // 为每个数据项创建一个异步任务
        for (DataItem item : items) {
            CompletableFuture<DataItem> future = CompletableFuture.supplyAsync(() -> {
                log.info("Processing item {} in thread {}", item.getId(), Thread.currentThread().getName());
                return callServiceC(item);
            }, asyncTaskExecutor);
            
            futures.add(future);
        }
        
        // 等待所有异步任务完成
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(
                futures.toArray(new CompletableFuture[0])
        );
        
        // 获取所有结果
        List<DataItem> results;
        try {
            allFutures.get(); // 等待所有任务完成
            results = futures.stream()
                    .map(this::getResult)
                    .collect(Collectors.toList());
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error processing batch data", e);
            Thread.currentThread().interrupt();
            throw new RuntimeException("Failed to process batch data", e);
        }
        
        long endTime = System.currentTimeMillis();
        long processTime = endTime - startTime;
        
        log.info("Completed processing {} items in {}ms", items.size(), processTime);
        
        return new BatchDataResponse(results, processTime);
    }
    
    private DataItem callServiceC(DataItem item) {
        try {
            log.info("Calling Service C for item {}", item.getId());
            return restTemplate.postForObject(serviceCUrl, item, DataItem.class);
        } catch (Exception e) {
            log.error("Error calling Service C for item {}", item.getId(), e);
            item.setResult("Error: " + e.getMessage());
            return item;
        }
    }
    
    private DataItem getResult(CompletableFuture<DataItem> future) {
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error getting result from future", e);
            DataItem errorItem = new DataItem();
            errorItem.setResult("Error: " + e.getMessage());
            return errorItem;
        }
    }
}