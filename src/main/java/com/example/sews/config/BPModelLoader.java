package com.example.sews.config;

import com.example.sews.dto.ModelInfo;
import com.example.sews.repo.ModelInfoRepository;
import com.example.sews.service.MonitoringPhotoService;
import com.example.sews.service.WeatherDataService;
import com.example.sews.utils.bputils.BPModelInstance;
import com.mathworks.toolbox.javabuilder.MWArray;
import com.mathworks.toolbox.javabuilder.MWCharArray;
import com.mathworks.toolbox.javabuilder.MWNumericArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.sews.minio.util.MinioUtils;

import static com.example.sews.minio.util.MinioUtils.modelTempDir;

@Component
public class BPModelLoader {

    @Autowired
    MinioUtils minioUtils;
    private final Map<String, BPModelInstance> gaofengModels = new HashMap<>();
    private final Map<String, BPModelInstance> duanqiModels = new HashMap<>();

    @Autowired
    private WeatherDataService weatherAnalysisService;

    @Autowired
    ModelInfoRepository modelInfoRepository;

    @Autowired
    private MonitoringPhotoService pestAnalysisService;
    @PostConstruct
    public void initBPModel() {
        try {
            String modelTempDir = "/tmp/modeltemp/";
            new File(modelTempDir).mkdirs();
            List<String> gaofengJarList = minioUtils.getGaofengJarList();
//            System.out.println("gaofengJarList = " + gaofengJarList);

            System.out.println("==============加载BP模型中==============");
            // 加载高峰模型
            for (String jarUrl : gaofengJarList) {
                loadAndStoreModel(jarUrl, "BPergao.service", gaofengModels);
            }

            // 加载短期模型
            for (String jarUrl : minioUtils.getDuqnQiJarList()) {
                loadAndStoreModel(jarUrl, "BPtxduanqi.txduanqi", duanqiModels);
            }

            System.out.println("==============全部BP模型加载成功==============");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("BP模型初始化失败");
        }
    }

    public double predictGaoFeng(Integer modelId, double aprilTemp, double decRain, double janRain, double aprilRain, double mayRadiation) {
        try {

            ModelInfo modelInfo = modelInfoRepository.getReferenceById(modelId);
            String modelName = modelInfo.getModelFilePath();
            // 获取对应模型实例
            if (!gaofengModels.containsKey(modelName)) {
                throw new IllegalArgumentException("高峰模型未加载: " + modelName);
            }
            BPModelInstance modelInstance = gaofengModels.get(modelName);
            if (modelInstance == null) {
                throw new IllegalArgumentException("模型不存在：" + modelName);
            }

            Object model = modelInstance.getInstance();
            Class<?> clazz = model.getClass();
//            for (Method m : clazz.getMethods()) {
//                System.out.println(m.getName() + " " + Arrays.toString(m.getParameterTypes()));
//            }
            // 构造输入参数
            String inputStr = String.format("%.2f %.2f %.2f %.2f %.2f", aprilTemp, decRain, janRain, aprilRain, mayRadiation);
            MWCharArray input = new MWCharArray(inputStr);

            // 调用 test3 方法
            Method method = clazz.getMethod("test3", int.class, Object[].class);
            Object[] result = (Object[]) method.invoke(model, 1, new Object[]{input});
            double[] output = ((MWNumericArray) result[0]).getDoubleData();

            return Math.round(output[0] * 1000) / 1000.0;

        } catch (Exception e) {
            e.printStackTrace();
            return Double.NaN;
        }
    }

    public double predictShortTerm(Integer  modelId,Integer deviceId) {
        try {
            double[] weatherData = weatherAnalysisService.getSegmentAveragesArray();

            // 获取虫口数据的 4 个参数（4 段虫口发生量）
            double[] pestData = pestAnalysisService.getSegmentPestAccumulationArray(deviceId);
            double [] args = {
                    weatherData[0], weatherData[1], weatherData[2], weatherData[3], // temp_20_16, temp_15_11, temp_10_6, temp_5_1
                    weatherData[4], weatherData[5], weatherData[6], weatherData[7], // rain_20_16, rain_15_11, rain_10_6, rain_5_1
                    pestData[0], pestData[1], pestData[2], pestData[3]// pest_20_16, pest_15_11, pest_10
            };

            ModelInfo modelInfo = modelInfoRepository.getReferenceById(modelId);
            String modelName = modelInfo.getModelName();
            BPModelInstance modelInstance = duanqiModels.get(modelName);
            if (modelInstance == null) {
                throw new IllegalArgumentException("模型不存在：" + modelName);
            }
            Object model = modelInstance.getInstance();
            Class<?> clazz = model.getClass();

            Method method = clazz.getMethod("testBP",
                    int.class,
                    double.class, double.class, double.class, double.class,
                    double.class, double.class, double.class, double.class,
                    double.class, double.class, double.class, double.class);

            Object[] result = (Object[]) method.invoke(model,
                    1,
                    args[0], args[1], args[2], args[3],
                    args[4], args[5], args[6], args[7],
                    args[8], args[9], args[10], args[11]);

            double[] output = ((MWNumericArray) result[0]).getDoubleData();
            return Math.round(output[0] * 1000) / 1000.0;

        } catch (Exception e) {
            e.printStackTrace();
            return Double.NaN;
        }
    }

    private void loadAndStoreModel(String jarUrl, String className, Map<String, BPModelInstance> targetMap) throws Exception {
        String fileName = jarUrl.substring(jarUrl.lastIndexOf("/") + 1);
        String localPath = modelTempDir + fileName;

        URLClassLoader classLoader = new URLClassLoader(
                new URL[]{new File(localPath).toURI().toURL()},
                this.getClass().getClassLoader());

        Class<?> modelClass = classLoader.loadClass(className);
        Object modelInstance = modelClass.getDeclaredConstructor().newInstance();

        targetMap.put(fileName, new BPModelInstance(modelInstance, classLoader));
        System.out.println("模型加载成功：" + fileName);
    }
}
