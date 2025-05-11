package com.example.sews.minio.util;

import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtException;
import ai.onnxruntime.OrtSession;
import io.minio.*;
import io.minio.errors.MinioException;
import io.minio.messages.Item;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * @Author: myh
 * @CreateTime: 2025-03-19 17:06
 * @Description: MinIO 工具类，包括获取图片列表以及上传图片到 MinIO 的方法
 * @Version: 1.0
 */
@Component
public class MinioUtils {
    @Value("${spring.minio.url}")
    private String MINIO_URL;
    private static final String PHOTO_BUCKET_NAME = "product";
    private static final String RECOGNIZE_BUCKET_NAME = "recognize";
    public static final String ONNX_MODEL_BUCKET_NAME = "model";
    private static final String BP_GF_MODEL_BUCKET_NAME = "gaofeng";
    private static final String BP_DQ_MODEL_BUCKET_NAME = "duanqi";
    public static final String modelTempDir="/tmp/modeltemp/";
    MinioClient minioClient;

    // 构造器注入 MinioClient
    @Autowired
    public MinioUtils(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    /**
     * 获取 MinIO product bucket 下的所有图片 URL（仅处理 .png 和 .jpg 格式）
     */
    public List<String> getFileListFromBucket(String bucketName, List<String> extensions) {
        List<String> fileUrls = new ArrayList<>();

        try {
            ListObjectsArgs args = ListObjectsArgs.builder().bucket(bucketName).build();
            Iterable<Result<Item>> objects = minioClient.listObjects(args);

            for (Result<Item> result : objects) {
                try {
                    Item item = result.get();
                    String fileName = item.objectName();

                    for (String ext : extensions) {
                        if (fileName.toLowerCase().endsWith(ext.toLowerCase())) {
                            String directUrl = MINIO_URL + "/" + bucketName + "/" + fileName;
                            fileUrls.add(directUrl);
                            break;
                        }
                    }
                } catch (MinioException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
                    System.err.println("Error processing item in bucket " + bucketName + ": " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("Error listing objects from bucket " + bucketName + ": " + e.getMessage());
        }

        return fileUrls;
    }

    public List<String> getDuqnQiJarList() {
        return getFileListFromBucket(BP_DQ_MODEL_BUCKET_NAME, List.of(".jar"));
    }

    public List<String> getGaofengJarList() {
        return getFileListFromBucket(BP_GF_MODEL_BUCKET_NAME, List.of(".jar"));
    }

    public List<String> getOnnxList() {
        return getFileListFromBucket(ONNX_MODEL_BUCKET_NAME, List.of(".onnx"));
    }
    public List<String> getImageList() {
        return getFileListFromBucket(PHOTO_BUCKET_NAME, List.of(".png", ".jpg"));
    }



    /**
     * 将检测处理后的图片上传到 MinIO 服务器的 "Recognize" bucket
     * 上传后的文件名格式为：原文件名(识别).扩展名
     *
     * @param resultImage 检测处理后的 Mat 图像
     * @param imageUrl    原始图片的 URL 或文件名（如果不包含协议，则自动补全）
     */
    public void uploadImageToMinio(Mat resultImage, String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            String path = url.getPath(); // 例如 "/product/tao_li2.jpg"
            String fileName = path.substring(path.lastIndexOf('/') + 1); // 提取 "tao_li2.jpg"
            int dotIndex = fileName.lastIndexOf(".");
            String nameWithoutExt = (dotIndex == -1) ? fileName : fileName.substring(0, dotIndex);
            String extension = (dotIndex == -1) ? ".jpg" : fileName.substring(dotIndex);
            String newFileName = nameWithoutExt + "(recognize)" + extension;

            // 将 Mat 转换为 JPEG 格式的字节数组
            MatOfByte mob = new MatOfByte();
            Imgcodecs.imencode(".jpg", resultImage, mob);
            byte[] imageBytes = mob.toArray();

            // 上传到 MinIO 的 Recognize bucket
            String bucketName = RECOGNIZE_BUCKET_NAME;
            // 检查 bucket 是否存在，如不存在则创建
            boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!bucketExists) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(newFileName)
                            .stream(new ByteArrayInputStream(imageBytes), imageBytes.length, -1)
                            .contentType("image/jpeg")
                            .build()
            );
            System.out.println("图片已上传到 MinIO，文件名：" + newFileName);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (Exception e) {  // 捕获 MinIO SDK 和其他操作可能抛出的异常
            e.printStackTrace();
        }
    }
    public void uploadFileToMinio(MultipartFile file, String originalFileName, String bucketName, String contentType) {
        try {
            // 使用原始文件名，也可以使用 UUID 保证文件名唯一性
            String newFileName = originalFileName;

            // 获取上传的文件输入流
            InputStream fileInputStream = file.getInputStream();

            // 检查桶是否存在
            boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!bucketExists) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }

            // 上传文件到指定桶
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(newFileName)
                            .stream(fileInputStream, file.getSize(), -1)
                            .contentType(contentType)  // 根据文件类型设置 contentType
                            .build()
            );

            System.out.println("文件已上传到 MinIO 桶：" + bucketName + "，文件名：" + newFileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void uploadOnnxFileToMinio(MultipartFile file, String originalFileName) {
        uploadFileToMinio(file, originalFileName, ONNX_MODEL_BUCKET_NAME, "application/octet-stream");
    }
    public void uploadDuanQiJarFile(MultipartFile file, String originalFileName) {
        uploadFileToMinio(file, originalFileName, BP_DQ_MODEL_BUCKET_NAME, "application/java-archive");
    }
    public void uploadGaofengJarFile(MultipartFile file, String originalFileName) {
        uploadFileToMinio(file, originalFileName, BP_GF_MODEL_BUCKET_NAME, "application/java-archive");
    }
    public void downloadFromMinio(String bucketName, String objectName, String localPath) throws Exception {
        String filePath = localPath+objectName;
        File file = new File(filePath);
        if (file.exists()) {
            System.out.println("文件已存在，跳过下载：" + filePath);
            return;
        }
//        System.out.println("开始从 MinIO 下载：" + bucketName + "/" + objectName + " -> " + filePath);

        minioClient.downloadObject(
                DownloadObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .filename(filePath)
                        .build()
        );

        System.out.println("下载完成：" + localPath);
    }
//    public  void download(String fileName, String localPath) throws Exception {
//
//
//        try (InputStream in = new URL(fileName).openStream();
//             FileOutputStream out = new FileOutputStream(localPath)) {
//            byte[] buffer = new byte[8192];
//            int bytesRead;
//            while ((bytesRead = in.read(buffer)) != -1) {
//                out.write(buffer, 0, bytesRead);
//            }
//        }
//    }
    public String downloadOnnxModel(String modelName) throws Exception {
        System.out.println("modelName = " + modelName);
        downloadFromMinio(ONNX_MODEL_BUCKET_NAME, modelName, modelTempDir);
        String localPath = modelTempDir + modelName;
        return localPath;
    }
    public OrtSession loadOnnxModel(String localModelPath) throws OrtException {
        OrtEnvironment environment = OrtEnvironment.getEnvironment();
        OrtSession.SessionOptions sessionOptions = new OrtSession.SessionOptions();
        return environment.createSession(localModelPath, sessionOptions);
    }
    public OrtSession initOnnxModelFromMinio(String modelName) throws Exception {
        String localPath = downloadOnnxModel(modelName);
        return loadOnnxModel(localPath);
    }

}
