package com.example.servicea.service;

import com.example.servicea.model.BatchDataRequest;
import com.example.servicea.model.BatchDataResponse;
import com.example.servicea.model.DataItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProcessService {

    private final RestTemplate restTemplate;

    @Value("${service.b.url}")
    private String serviceBUrl;

    public BatchDataResponse processBatchData(int count) {
        log.info("Preparing {} data items to send to Service B", count);
        
        // 创建测试数据
        List<DataItem> items = generateTestData(count);
        
        // 创建请求对象
        BatchDataRequest request = new BatchDataRequest(items);
        
        // 调用Service B
        log.info("Calling Service B with {} items", items.size());
        long startTime = System.currentTimeMillis();
        
        BatchDataResponse response = restTemplate.postForObject(
                serviceBUrl,
                request,
                BatchDataResponse.class
        );
        
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        
        log.info("Received response from Service B in {}ms", totalTime);
        
        return response;
    }
    
    private List<DataItem> generateTestData(int count) {
        List<DataItem> items = new ArrayList<>();
        for (long i = 1; i <= count; i++) {
            items.add(new DataItem(i, "Test data " + i, null));
        }
        return items;
    }
}