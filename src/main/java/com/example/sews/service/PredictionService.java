package com.example.sews.service;

import com.example.sews.config.BPModelLoader;
import com.example.sews.dto.PeakDayPredictionInput;
import com.example.sews.dto.ShortTermPredictionInput;
import com.example.sews.repo.PeakDayPredictionInputRepository;
import com.example.sews.utils.bputils.BPUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;

@Service
public class PredictionService {

    @Autowired
    PeakDayPredictionInputRepository peakDayPredictionInputRepository;


    @Autowired
    BPModelLoader bpModelLoader;
    /**
     * 模拟计算短期发生量
     * @return 预测的害虫数量
     */
    public String calculateShortTermPestCount(Integer modelId,Integer deviceId) {
        return Double.toString(bpModelLoader.predictShortTerm(modelId,deviceId));
    }

    /**
     * 模拟计算高峰日
     * @return 预测的高峰日
     */
    public String calculatePeakDay(PeakDayPredictionInput input){
        double result = bpModelLoader.predictGaoFeng(input.getModelId(),input.getInput1(), input.getInput2(), input.getInput3(), input.getInput4(), input.getInput5());
        int month =0;
        int day =0;
        if(input.getModelId()==1){
            month=3;
            day=10;
        }else if(input.getModelId()==2){
            month=4;
            day=10;
        }
        LocalDate startDate = LocalDate.of(LocalDate.now().getYear(), month, day);
        input.setForecastDate(startDate.plusDays((long) Math.round(Double.parseDouble(result + "")) - 1));
        peakDayPredictionInputRepository.save(input);
        return input.getForecastDate().toString();
    }
}
