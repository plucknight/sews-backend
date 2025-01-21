package com.example.sews.repo;

import com.example.sews.dto.PredictionResultDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PredictionResultRepository extends JpaRepository<PredictionResultDTO, Integer> {

    // 可以添加自定义查询方法，若需要更多查询功能
    List<PredictionResultDTO> findByDeviceId(Integer deviceId);
    List<PredictionResultDTO> findByModelId(Integer modelId);
}
