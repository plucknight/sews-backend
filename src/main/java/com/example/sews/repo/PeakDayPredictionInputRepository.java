package com.example.sews.repo;

import com.example.sews.dto.PeakDayPredictionInput;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PeakDayPredictionInputRepository extends JpaRepository<PeakDayPredictionInput, Integer> {

}
