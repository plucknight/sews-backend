package com.example.sews.service;

import com.example.sews.dto.OperateLog;
import com.example.sews.repo.OperateLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OperateLogService {

    @Autowired
    private OperateLogRepository operateLogRepository;

    // Save a new operate log
    public OperateLog saveOperateLog(OperateLog operateLog) {
        return operateLogRepository.save(operateLog);
    }

    // Find operate log by ID
    public OperateLog findById(Integer id) {
        return operateLogRepository.findById(id).orElse(null);
    }

    public List<OperateLog> findAll() {
        return operateLogRepository.findAll();
    }

    // Additional business logic can be added for retrieving logs, filtering, etc.
}
