package com.example.sews.repo;

import com.example.sews.dto.ModelInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface ModelInfoRepository extends JpaRepository<ModelInfo, Integer> {
        List<ModelInfo> findByModelIdIn(List<Integer> modelIds);

        List<ModelInfo> findByModelType(String modelType);

}
