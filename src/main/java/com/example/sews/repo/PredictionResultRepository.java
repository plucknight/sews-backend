package com.example.sews.repo;

import com.example.sews.dto.Device;
import com.example.sews.dto.MonitoringPhoto;
import com.example.sews.dto.PredictionResult;
import com.example.sews.dto.vo.DevicePredictionDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PredictionResultRepository extends JpaRepository<PredictionResult, Integer> {

    @Query("SELECT p.deviceId, p.modelId, m.modelName, m.modelType, p.forecastValue " +
            "FROM PredictionResult p " +
            "JOIN ModelInfo m ON p.modelId = m.modelId")
    List<Object[]> findAllPredictions();

    @Query("SELECT p.deviceId, p.modelId, m.modelName, m.modelType, p.forecastValue " +
            "FROM PredictionResult p " +
            "JOIN ModelInfo m ON p.modelId = m.modelId " +
            "WHERE p.deviceId IN (:deviceIds)")
    List<Object[]> findPredictionsByDeviceIds( List<Integer> deviceIds);

//    // 可以添加自定义查询方法，若需要更多查询功能
//    List<PredictionResultDTO> findByDeviceId(Integer deviceId);
//    List<PredictionResultDTO> findByModelId(Integer modelId);
}
