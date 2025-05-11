package com.example.sews.repo;

import com.example.sews.dto.OperateLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperateLogRepository extends JpaRepository<OperateLog, Integer
        > {
}
