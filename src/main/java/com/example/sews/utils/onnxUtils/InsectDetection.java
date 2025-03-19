package com.example.sews.utils.onnxUtils;

import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtException;
import ai.onnxruntime.OrtSession;
import com.example.minio.util.MinioStaticUtils;
import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.FloatBuffer;
import java.util.*;
import java.util.stream.Collectors;


/**
 * yolov8害虫识别
 */
@Service
public class InsectDetection {

    static {
        nu.pattern.OpenCV.loadLocally();
    }


    public static void main(String[] args) throws OrtException {
        InsertDetection();
    }

    public static void InsertDetection() throws OrtException {

        String model_path = "src\\main\\resources\\model\\best.onnx";

        float confThreshold = 0.35F;

        float nmsThreshold = 0.55F;

        //禁用并行处理
//        System.setProperty("opencv.disable.parallel", "true");

        // 加载ONNX模型
        OrtEnvironment environment = OrtEnvironment.getEnvironment();
        OrtSession.SessionOptions sessionOptions = new OrtSession.SessionOptions();
        // 加载ONNX模型
        OrtSession session = environment.createSession(model_path, sessionOptions);
        // 新增您的自定义标签和颜色
        String[] labels = {"Grapholita molesta Busck",//李小
                "Carposina sasakii Matsumura"//桃小
        };
        List<double[]> colors = Arrays.asList(
                new double[]{255, 0, 0},   // 红色
                new double[]{0, 255, 0}   // 绿色
        );

        // 替换原有标签加载代码
        String meteStr = session.getMetadata().getCustomMetadata().get("names");
//        if (meteStr != null) {
//            System.out.println("===== 模型内置类别列表 =====");
//            Arrays.stream(meteStr.split(","))
//                    .forEach(System.out::println);
//        int modelClasses = meteStr.split(",").length;
//        if (modelClasses != labels.length) {
//            throw new IllegalArgumentException(String.format(
//                    "模型输出类别数（%d）与提供的标签数（%d）不匹配！",
//                    modelClasses,
//                    labels.length
//            ));
//        }
//        }

        // 输出基本信息
        session.getInputInfo().keySet().forEach(x -> {
            try {
                System.out.println("input name = " + x);
                System.out.println(session.getInputInfo().get(x).getInfo().toString());
            } catch (OrtException e) {
                throw new RuntimeException(e);
            }
        });
        // 要检测的图片所在目录
//        String imagePath = "src/main/java/com/example/sews/utils/onnxUtils/images";

        List<String> list =
                MinioStaticUtils.getImageList();
//        list.forEach(System.out::println);
        for (String fileName : list) {
            Mat img = readImageFromURL(fileName);
            if (img != null && img.empty()) {
                System.out.println("图片加载失败！");
            }
            Mat image = img.clone();
            Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2RGB);


            // 在这里先定义下框的粗细、字的大小、字的类型、字的颜色(按比例设置大小粗细比较好一些)
            int minDwDh = Math.min(img.width(), img.height());
            int thickness = minDwDh / ODConfig.lineThicknessRatio;
            long start_time = System.currentTimeMillis();
            // 更改 image 尺寸
            Letterbox letterbox = new Letterbox();
            image = letterbox.letterbox(image);

            double ratio = letterbox.getRatio();
            double dw = letterbox.getDw();
            double dh = letterbox.getDh();
            int rows = letterbox.getHeight();
            int cols = letterbox.getWidth();
            int channels = image.channels();

            // 将Mat对象的像素值赋值给Float[]对象
            float[] pixels = new float[channels * rows * cols];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    double[] pixel = image.get(j, i);
                    for (int k = 0; k < channels; k++) {
                        // 这样设置相当于同时做了image.transpose((2, 0, 1))操作
                        pixels[rows * cols * k + j * cols + i] = (float) pixel[k] / 255.0f;
                    }
                }
            }

            // 创建OnnxTensor对象
            long[] shape = {1L, (long) channels, (long) rows, (long) cols};
            OnnxTensor tensor = OnnxTensor.createTensor(environment, FloatBuffer.wrap(pixels), shape);
            HashMap<String, OnnxTensor> stringOnnxTensorHashMap = new HashMap<>();
            stringOnnxTensorHashMap.put(session.getInputInfo().keySet().iterator().next(), tensor);

            // 运行推理
            OrtSession.Result output = session.run(stringOnnxTensorHashMap);
//            System.out.println("\n===== 模型实际输出类别 =====");
            float[][] outputData = ((float[][][]) output.get(0).getValue())[0];
//            for (int i = 0; i < outputData.length; i++) {
//                float[] bbox = outputData[i];
//                float[] probs = Arrays.copyOfRange(bbox, 4, bbox.length);
//                int labelIdx = argmax(probs); // 获取最大置信度的类别索引
//                System.out.printf("[第 %d 个预测框] ", i);
//                System.out.printf("类别索引：%d | ", labelIdx);
//                System.out.printf("置信度：%.2f%% | ", probs[labelIdx] * 100);
//                System.out.printf("bbox：[%f,%f,%f,%f]\n",
//                        bbox[0], bbox[1], bbox[2], bbox[3]);
//            }

            outputData = transposeMatrix(outputData);
            Map<Integer, List<float[]>> class2Bbox = new HashMap<>();

            for (float[] bbox : outputData) {


                float[] conditionalProbabilities = Arrays.copyOfRange(bbox, 4, bbox.length);
                int label = argmax(conditionalProbabilities);
                float conf = conditionalProbabilities[label];
                if (conf < confThreshold) continue;

                bbox[4] = conf;

                // xywh to (x1, y1, x2, y2)
                xywh2xyxy(bbox);

                // skip invalid predictions
                if (bbox[0] >= bbox[2] || bbox[1] >= bbox[3]) continue;


                class2Bbox.putIfAbsent(label, new ArrayList<>());
                class2Bbox.get(label).add(bbox);
            }

            List<Detection> detections = new ArrayList<>();
            for (Map.Entry<Integer, List<float[]>> entry : class2Bbox.entrySet()) {
                int label = entry.getKey();
                List<float[]> bboxes = entry.getValue();
                bboxes = nonMaxSuppression(bboxes, nmsThreshold);
                for (float[] bbox : bboxes) {
                    String labelString = labels[label];
                    detections.add(new Detection(labelString, entry.getKey(), Arrays.copyOfRange(bbox, 0, 4), bbox[4]));
                }
            }


            for (Detection detection : detections) {
                float[] bbox = detection.getBbox();
//                System.out.println(detection.toString());
                // 画框
                Point topLeft = new Point((bbox[0] - dw) / ratio, (bbox[1] - dh) / ratio);
                Point bottomRight = new Point((bbox[2] - dw) / ratio, (bbox[3] - dh) / ratio);
                Scalar color = new Scalar(colors.get(detection.getClsId()));
                Imgproc.rectangle(img, topLeft, bottomRight, color, thickness);
                // 框上写文字
                // 第二个参数 detection.getLabel() 决定了显示的文字内容。
                //第四个参数 Imgproc.FONT_HERSHEY_SIMPLEX 表示字体类型，你可以替换为其他字体常量。
                //第五个参数 表示字体大小（缩放因子）。
                //最后两个参数 color 和 thickness 分别表示文字的颜色和粗细。
                Point boxNameLoc = new Point((bbox[0] - dw) / ratio, (bbox[1] - dh) / ratio - 3);
                Imgproc.putText(img, detection.getLabel(), boxNameLoc,
                        Imgproc.FONT_HERSHEY_SIMPLEX, 2, color, thickness,
                        Imgproc.LINE_4, false);

            }
            // 定义缩放比例，例如缩小为原来的一半
            double scaleFactor = 0.2;
            int newWidth = (int) (img.width() * scaleFactor);
            int newHeight = (int) (img.height() * scaleFactor);
            Mat resizedImg = new Mat();
            Imgproc.resize(img, resizedImg, new org.opencv.core.Size(newWidth, newHeight));
            HighGui.imshow("Display Image", resizedImg);

            //保存到minio服务器
            MinioStaticUtils.uploadImageToMinio(resizedImg, fileName);
            // 按任意按键关闭弹窗画面，结束程序
//            HighGui.waitKey();
        }
        HighGui.destroyAllWindows();
        System.exit(0);

    }

    //保存到本地
    public static void saveImage(Mat resultImage, String imageUrl) {
        try {
            // 从 URL 中提取文件名
            URL url = new URL(imageUrl);
            String path = url.getPath();           // 得到 "/product/tao_li2.jpg"
            String fileName = path.substring(path.lastIndexOf('/') + 1);
            int dotIndex = fileName.lastIndexOf(".");
            String nameWithoutExt = (dotIndex == -1) ? fileName : fileName.substring(0, dotIndex);
            String extension = (dotIndex == -1) ? ".jpg" : fileName.substring(dotIndex);

            // 构造新的文件名，例如 "tao_li2(识别).jpg"
            String newFileName = nameWithoutExt + "(Recognize)" + extension;

            // 定义保存目录，并确保目录存在
            String recognizeDir = "Recognize/";
            new File(recognizeDir).mkdirs();

            // 拼接完整保存路径
            String recognizePath = recognizeDir + newFileName;

            // 使用 OpenCV 保存图片到本地
            boolean success = Imgcodecs.imwrite(recognizePath, resultImage);

            if (success) {
                System.out.println("图片保存成功: " + recognizePath);
            } else {
                System.out.println("图片保存失败！");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    //从MinioURL读取文件到Mat对象
    public static Mat readImageFromURL(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            URLConnection connection = url.openConnection();
            InputStream inputStream = connection.getInputStream();

            // 读取流数据
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            byte[] data = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, bytesRead);
            }
            inputStream.close();

            // 将字节数组转换为 OpenCV Mat
            byte[] imageData = buffer.toByteArray();
            Mat img = Imgcodecs.imdecode(new MatOfByte(imageData), Imgcodecs.IMREAD_COLOR);
            return img;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static void xywh2xyxy(float[] bbox) {
        float x = bbox[0];
        float y = bbox[1];
        float w = bbox[2];
        float h = bbox[3];

        bbox[0] = x - w * 0.5f;
        bbox[1] = y - h * 0.5f;
        bbox[2] = x + w * 0.5f;
        bbox[3] = y + h * 0.5f;
    }

    public static float[][] transposeMatrix(float[][] m) {
        float[][] temp = new float[m[0].length][m.length];
        for (int i = 0; i < m.length; i++)
            for (int j = 0; j < m[0].length; j++)
                temp[j][i] = m[i][j];
        return temp;
    }

    public static List<float[]> nonMaxSuppression(List<float[]> bboxes, float iouThreshold) {

        List<float[]> bestBboxes = new ArrayList<>();

        bboxes.sort(Comparator.comparing(a -> a[4]));

        while (!bboxes.isEmpty()) {
            float[] bestBbox = bboxes.remove(bboxes.size() - 1);
            bestBboxes.add(bestBbox);
            bboxes = bboxes.stream().filter(a -> computeIOU(a, bestBbox) < iouThreshold).collect(Collectors.toList());
        }

        return bestBboxes;
    }

    public static float computeIOU(float[] box1, float[] box2) {

        float area1 = (box1[2] - box1[0]) * (box1[3] - box1[1]);
        float area2 = (box2[2] - box2[0]) * (box2[3] - box2[1]);

        float left = Math.max(box1[0], box2[0]);
        float top = Math.max(box1[1], box2[1]);
        float right = Math.min(box1[2], box2[2]);
        float bottom = Math.min(box1[3], box2[3]);

        float interArea = Math.max(right - left, 0) * Math.max(bottom - top, 0);
        float unionArea = area1 + area2 - interArea;
        return Math.max(interArea / unionArea, 1e-8f);

    }

    //返回最大值的索引
    public static int argmax(float[] a) {
        float re = -Float.MAX_VALUE;
        int arg = -1;
        for (int i = 0; i < a.length; i++) {
            if (a[i] >= re) {
                re = a[i];
                arg = i;
            }
        }
        return arg;
    }

    public static Map<String, String> getImagePathMap(String imagePath) {
        Map<String, String> map = new TreeMap<>();
        File file = new File(imagePath);
        if (file.isFile()) {
            map.put(file.getName(), file.getAbsolutePath());
        } else if (file.isDirectory()) {
            for (File tmpFile : Objects.requireNonNull(file.listFiles())) {
                map.putAll(getImagePathMap(tmpFile.getPath()));
            }
        }
        return map;
    }
}
