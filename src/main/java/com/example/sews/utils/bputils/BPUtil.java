package com.example.sews.utils.bputils;
import BPergao.service;
import com.mathworks.toolbox.javabuilder.MWCharArray;
import com.mathworks.toolbox.javabuilder.MWException;
import com.mathworks.toolbox.javabuilder.MWNumericArray;
import BPtxduanqi.txduanqi;

import java.lang.reflect.Method;


/**
 * @Author: myh
 * @CreateTime: 2025-03-30  23:08
 * @Description: 高峰日
 * @Version: 1.0
 */
public class BPUtil {
    //gaofeng
    private static service gaofeng;
    //duanqi
    public static txduanqi duanqi;

    static {
        try {
            gaofeng = new service();
            duanqi = new txduanqi();
        } catch (MWException e) {
            e.printStackTrace();
        }
    }
    public static void close() {
        if (gaofeng != null) {
            gaofeng.dispose();
        }
    }

    public static void main(String[] args) {
System.out.println(predictGaoFeng(20, 100, 150, 120, 150));
    }
    public static double predictGaoFeng(double aprilTemp, double decRain, double janRain, double aprilRain, double mayRadiation) {
        try {
            String inputStr = String.format("%.2f %.2f %.2f %.2f %.2f", aprilTemp, decRain, janRain, aprilRain, mayRadiation);

            MWCharArray input = new MWCharArray(inputStr);
            Object[] result = gaofeng.test3(1, input);
            double[] output = ((MWNumericArray) result[0]).getDoubleData();

            return Math.round(output[0] * 1000) / 1000.0; // 保留3位小数
        } catch (MWException ex) {
            ex.printStackTrace();
            return Double.NaN;
        }
    }

    public static double predictShortTerm(
            double temp_20_16, double temp_15_11, double temp_10_6, double temp_5_1,
            double rain_20_16, double rain_15_11, double rain_10_6, double rain_5_1,
            double insects_20_16, double insects_15_11, double insects_10_6, double insects_5_1) {

        try {
            // 直接传递 12 个参数，避免构造字符串
            Object[] result = duanqi.testBP(
                    1,temp_20_16, temp_15_11, temp_10_6, temp_5_1,
                    rain_20_16, rain_15_11, rain_10_6, rain_5_1,
                    insects_20_16, insects_15_11, insects_10_6, insects_5_1
            );

            double[] output = ((MWNumericArray) result[0]).getDoubleData();
            return Math.round(output[0] * 1000) / 1000.0;
        } catch (MWException ex) {
            ex.printStackTrace();
            return Double.NaN;
        }
    }


}

