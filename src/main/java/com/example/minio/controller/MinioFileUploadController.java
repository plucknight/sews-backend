package com.example.minio.controller;

import com.example.minio.util.AjaxResult;
import com.example.minio.util.MinioStaticUtils;
import com.example.minio.util.MinioUtils;
import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.Result;
import io.minio.errors.MinioException;
import io.minio.messages.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Description minio文件上传控制器
 */
@CrossOrigin
@RestController
@RequestMapping("/minio")
public class MinioFileUploadController {
    @Autowired
    private MinioUtils minioUtils;

    @Autowired
     MinioClient minioClient;

    @Value("${spring.minio.url}")
    private String minioServerUrl;  // 注入MinIO的URL

    @Value("${spring.minio.bucketName}")
    private String bucketName;      // 注入桶名称
    @GetMapping("/images")
    public List<String> getImageList() {
        List<String> imageUrls = new ArrayList<>();
        // 使用 MinIO 客户端列出桶内所有文件
        ListObjectsArgs args = ListObjectsArgs.builder().bucket(bucketName).build();
        Iterable<Result<Item>> objects = minioClient.listObjects(args);

        for (Result<Item> result : objects) {
            try {
                Item item = result.get(); // 获取实际对象
                String fileName = item.objectName();

                // 只处理 .png 和 .jpg 文件
                if (fileName.endsWith(".png") || fileName.endsWith(".jpg")) {

                    // 生成直接可访问的URL
                    String directUrl = minioServerUrl + "/" + bucketName + "/" + fileName;
                    imageUrls.add(directUrl); // 添加直接URL到列表
                }
            } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
                // 捕获 MinioException 异常，处理获取文件时可能出现的错误
                System.err.println("Error processing item: " + e.getMessage());
            }
        }
        return imageUrls;
    }
    /**
     * @param file     文件
     * @Description 上传文件
     */
    @PostMapping("/upload")
    public String uploadFile(@RequestParam("onnxFile") MultipartFile file) {
        try {
            // 获取原始文件名
            String originalFileName = file.getOriginalFilename();
            // 调用 MinIO 上传方法
            MinioStaticUtils.uploadOnnxFileToMinio(file, originalFileName);
            return "文件上传成功!";
        } catch (Exception e) {
            e.printStackTrace();
            return "文件上传失败!";
        }
    }
     @GetMapping("/test")
    public String test() {
        return "test";
    }

    /**
     * @param fileName 文件名称
     * @Description dowload文件
     */
    @GetMapping("/dowload")
    public ResponseEntity dowloadFile(@RequestParam("fileName") String fileName) {
        return minioUtils.download(fileName);
    }

    /**
     * @param fileName 文件名称
     * @Description 得到文件url
     */
    @GetMapping("/getUrl")
    public AjaxResult getFileUrl(@RequestParam("fileName") String fileName){
        HashMap map=new HashMap();
        map.put("FileUrl",minioUtils.getFileUrl(fileName));
        return AjaxResult.success(map);
    }
}
