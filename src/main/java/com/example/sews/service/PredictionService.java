package com.example.sews.service;

import com.example.sews.dto.PeakDayPredictionInput;
import com.example.sews.dto.ShortTermPredictionInput;
import org.springframework.stereotype.Service;

@Service
public class PredictionService {

    /**
     * 进行短期发生量预测
     * @param input 短期预测输入数据
     * @return 预测结果（字符串类型）
     */
    public String predictShortTerm(ShortTermPredictionInput input) {
        // 这里模拟预测过程，实际实现可能会调用机器学习模型或数据分析模型
        // 假设使用某种算法进行预测，并返回结果
        double predictedPestCount = calculateShortTermPestCount(input);
        return "短期预测害虫数量: " + predictedPestCount;
    }

    /**
     * 进行高峰日预测
     * @param input 高峰日预测输入数据
     * @return 预测结果（字符串类型）
     */
    public String predictPeakDay(PeakDayPredictionInput input) {
        // 这里模拟预测过程，实际实现可能会调用机器学习模型或数据分析模型
        // 假设使用某种算法进行预测，并返回结果
        String peakDay = calculatePeakDay(input);
        return "预测的高峰日是: " + peakDay;
    }

    /**
     * 模拟计算短期发生量
     * @param input 短期预测输入数据
     * @return 预测的害虫数量
     */
    private double calculateShortTermPestCount(ShortTermPredictionInput input) {
        // 这里可以结合输入的数据进行预测，例如温度、降水量和积累害虫数量等
        // 这里只是一个简单的模拟示例
        return input.getAccumulatedPest20_16Days() * 1.2;  // 这是一个示例计算，实际应用中需根据模型来预测
    }

    /**
     * 模拟计算高峰日
     * @param input 高峰日预测输入数据
     * @return 预测的高峰日
     */
    private String calculatePeakDay(PeakDayPredictionInput input) {
        // 这里可以结合输入的数据进行高峰日预测，例如温度、降水量、辐射等
        // 这里只是一个简单的模拟示例
        return "2025-06-15";  // 示例返回值
    }
}
