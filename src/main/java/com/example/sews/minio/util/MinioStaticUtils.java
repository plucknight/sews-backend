package com.example.sews.minio.util;

import ai.onnxruntime.OrtException;
import com.example.sews.utils.onnxUtils.InsectDetection;
import io.minio.*;
import io.minio.errors.MinioException;
import io.minio.messages.Item;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @Author: myh
 * @CreateTime: 2025-03-19 17:06
 * @Description: MinIO 工具类，包括获取图片列表以及上传图片到 MinIO 的方法
 * @Version: 1.0
 */
public class MinioStaticUtils {

    private static final String MINIO_URL = "http://127.0.0.1:9000";
    @Value("${spring.minio.accessKey}")
    private static  String ACCESS_KEY ;
    @Value("${spring.minio.secretKey}")
    private static  String SECRET_KEY ;
    private static final String PHOTO_BUCKET_NAME = "product";
    private static final String RECOGNIZE_BUCKET_NAME= "recognize";
    private static final String MODEL_BUCKET_NAME= "model";
    private static final MinioClient minioClient;

    static {
        // 静态初始化 MinioClient
        minioClient = MinioClient.builder()
                .endpoint(MINIO_URL)
                .credentials(ACCESS_KEY, SECRET_KEY)
                .build();
    }

    /**
     * 获取 MinIO product bucket 下的所有图片 URL（仅处理 .png 和 .jpg 格式）
     */
    public static List<String> getImageList() {
        List<String> imageUrls = new ArrayList<>();

        ListObjectsArgs args = ListObjectsArgs.builder().bucket(PHOTO_BUCKET_NAME).build();
        Iterable<Result<Item>> objects = minioClient.listObjects(args);

        for (Result<Item> result : objects) {
            try {
                Item item = result.get();
                String fileName = item.objectName();

                if (fileName.endsWith(".png") || fileName.endsWith(".jpg")) {
                    String directUrl = MINIO_URL + "/" + PHOTO_BUCKET_NAME + "/" + fileName;
                    imageUrls.add(directUrl);
                }
            } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
                System.err.println("Error processing item: " + e.getMessage());
            }
        }
        return imageUrls;
    }

    /**
     * 将检测处理后的图片上传到 MinIO 服务器的 "Recognize" bucket
     * 上传后的文件名格式为：原文件名(识别).扩展名
     *
     * @param resultImage 检测处理后的 Mat 图像
     * @param imageUrl    原始图片的 URL 或文件名（如果不包含协议，则自动补全）
     */
    public static void uploadImageToMinio(Mat resultImage, String imageUrl) {
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

    public static void uploadOnnxFileToMinio(MultipartFile file, String originalFileName) {
        try {
            // 生成新的文件名，可以使用 UUID 保证文件名唯一性
            String newFileName = originalFileName;

            // 获取上传的文件输入流
            InputStream fileInputStream = file.getInputStream();

            // 检查是否存在对应的桶
            boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(MODEL_BUCKET_NAME).build());
            if (!bucketExists) {
                // 如果桶不存在，则创建
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(MODEL_BUCKET_NAME).build());
            }

            // 上传文件到 MinIO
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(MODEL_BUCKET_NAME)
                            .object(newFileName)  // 上传后的文件名
                            .stream(fileInputStream, file.getSize(), -1)
                            .contentType("application/octet-stream") // 假设是二进制文件
                            .build()
            );

            System.out.println("文件已上传到 MinIO，文件名：" + newFileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
