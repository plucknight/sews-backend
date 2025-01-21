package com.example.sews.repo;

import com.example.sews.dto.WarningRules;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WarningRulesRepository extends JpaRepository<WarningRules, Integer> {
    // 可以根据需要添加一些自定义查询方法
}
