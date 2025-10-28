package com.example.servicec.service;

import com.example.servicec.model.DataItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class ProcessService {

    public DataItem process(DataItem dataItem) {
        // 模拟处理数据
        try {
            // 模拟处理时间
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // 设置处理结果
        dataItem.setResult("Processed by Service C: " + UUID.randomUUID().toString().substring(0, 8));
        log.info("Service C processed data item: {}, result: {}", dataItem.getId(), dataItem.getResult());
        
        return dataItem;
    }
}