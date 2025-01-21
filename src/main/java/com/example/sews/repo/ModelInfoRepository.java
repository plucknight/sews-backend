package com.example.sews.repo;

import com.example.sews.dto.ModelInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModelInfoRepository extends JpaRepository<ModelInfo, Integer> {
    // 可以根据需要添加一些自定义查询方法
}
