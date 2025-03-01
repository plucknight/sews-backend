package com.example.sews.repo;

import com.example.sews.dto.Device;
import com.example.sews.dto.PredictionResult;
import com.example.sews.dto.vo.DevicePredictionDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PredictionResultRepository extends JpaRepository<PredictionResult, Integer> {

    @Query(value = "SELECT d.device_id, " +
            "       d.device_name, " +
            "       d.location, " +
            "       d.latitude, " +
            "       d.longitude, " +
            "       MAX(CASE WHEN p.model_id = 1 THEN p.forecast_value ELSE -1 END) AS peek_day_forecast_value, " +
            "       MAX(CASE WHEN p.model_id = 2 THEN p.forecast_value ELSE -1 END) AS short_term_forecast_value, " +
            "       p.forecast_value AS suggestion " +
            "FROM devices d " +
            "LEFT JOIN  prediction_results p ON d.device_id = p.device_id " +
            "GROUP BY d.device_id, d.device_name, d.location, d.latitude, d.longitude, p.forecast_value",
            nativeQuery = true)
    List<Object[]> findAllDeviceWithPredictionResult();


//    // 可以添加自定义查询方法，若需要更多查询功能
//    List<PredictionResultDTO> findByDeviceId(Integer deviceId);
//    List<PredictionResultDTO> findByModelId(Integer modelId);
}
