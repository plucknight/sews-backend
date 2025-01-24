package com.example.sews.controller;

import com.example.sews.dto.OperateLog;
import com.example.sews.service.OperateLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/operate-log")
public class OperateLogController {

    @Autowired
    private OperateLogService operateLogService;

    // Create a new operate log (POST)
    @PostMapping("/create")
    public ResponseEntity<OperateLog> createOperateLog(@RequestBody OperateLog operateLog) {
        // Set the current timestamp if not set
        if (operateLog.getCreatedAt() == null) {
            operateLog.setCreatedAt(LocalDateTime.now());
        }

        OperateLog savedLog = operateLogService.saveOperateLog(operateLog);

        return new ResponseEntity<>(savedLog, HttpStatus.CREATED);
    }

    @GetMapping("/list")
    public ResponseEntity<List<OperateLog>> getOperateLogById() {
        List<OperateLog> logs = operateLogService.findAll();
        if (logs != null) {
            return ResponseEntity.ok(logs);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
