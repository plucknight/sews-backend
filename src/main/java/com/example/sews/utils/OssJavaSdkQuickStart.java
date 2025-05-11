package com.example.sews.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.*;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;

public class OssJavaSdkQuickStart {
    static String OSSFilePath = "2024-07-07/device1/";

    @Value("${OSS.accessKeyId}")
    static String accessKeyId ;
    @Value("${OSS.accessKeySecret}")
    static String accessKeySecret ;
    public static void uploadFile(OSS ossClient, String bucketName, String objectName, String content) {
        ossClient.putObject(bucketName, objectName, new ByteArrayInputStream(content.getBytes()));
        System.out.println("文件 " + objectName + " 上传成功。");
    }

    public static void downloadFile(OSS ossClient, String bucketName, String objectName) {
        // 构造当前目录下的 tmp 目录路径
        File tmpDir = new File("tmp");
        if (!tmpDir.exists()) {
            tmpDir.mkdirs(); // 如果不存在就创建
        }

        // 获取文件名
        String fileName = new File(objectName).getName();
        File localFile = new File(tmpDir, fileName);

        // 下载并保存到 tmp 目录下
        OSSObject ossObject = ossClient.getObject(bucketName, objectName);
        try (InputStream inputStream = ossObject.getObjectContent();
             FileOutputStream outputStream = new FileOutputStream(localFile)) {

            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            System.out.println("下载成功");
        } catch (IOException e) {
            System.err.println("下载失败");
        }
    }


    public static void listFiles(OSS ossClient, String bucketName) {
        ObjectListing objectListing = ossClient.listObjects(bucketName);
        System.out.println("列出 Bucket 中的文件：");
        for (OSSObjectSummary objectSummary : objectListing.getObjectSummaries()) {
            System.out.println(" - " + objectSummary.getKey() + " (大小 = " + objectSummary.getSize() + ")");
        }
    }

    public static void deleteFile(OSS ossClient, String bucketName, String objectName) {
        ossClient.deleteObject(bucketName, objectName);
        System.out.println("文件 " + objectName + " 删除成功。");
    }

    public static void deleteBucket(OSS ossClient, String bucketName) {
        ossClient.deleteBucket(bucketName);
        System.out.println("Bucket " + bucketName + " 删除成功。");
    }

    private static void listFilesInDirectory(OSS ossClient, String bucketName, String prefix) {
        // 创建ListObjectsRequest对象，指定前缀（目录路径）。
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucketName);
        listObjectsRequest.setPrefix(prefix); // 设置目录前缀
        listObjectsRequest.setMaxKeys(100);   // 每次最多返回100个文件

        ObjectListing objectListing;
        do {
            // 执行列举操作。
            objectListing = ossClient.listObjects(listObjectsRequest);

            // 遍历文件列表。
            for (OSSObjectSummary objectSummary : objectListing.getObjectSummaries()) {
                System.out.println("File: " + objectSummary.getKey());
            }

            // 如果结果被截断，则更新继续标记。
            listObjectsRequest.setMarker(objectListing.getNextMarker());
        } while (objectListing.isTruncated());
    }

    public static void uploadImage(OSS ossClient, String bucketName, String localFilePath) {
        try {
            File file = new File(localFilePath);
            if (!file.exists()) {
                System.out.println("文件不存在：" + localFilePath);
                return;
            }
            String objectName = OSSFilePath + file.getName(); // 存储到 OSS 中的路径
            // 创建 PutObjectRequest 对象
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, new File(localFilePath));

            // 上传文件
            ossClient.putObject(putObjectRequest);
            System.out.println("图片上传成功：" + objectName);
        } catch (Exception e) {
            System.out.println("图片上传失败：" + e.getMessage());
        }
    }

    public static void main(String[] args) throws IOException {
        // 1. 配置Endpoint（以华东1（杭州）为例）
        String endpoint = "https://oss-cn-hangzhou.aliyuncs.com";

        // 2. 配置AccessKey ID和AccessKey Secret


        // 4. 创建OSSClient实例
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        String bucketName = "hkcam";
        listFilesInDirectory(ossClient, bucketName, OSSFilePath);
        String localFilePath = "C:\\Users\\Myh\\Desktop\\paper\\code\\sews-backend\\Recognize\\li(Recognize).jpg";
        // 调用上传方法
        uploadImage(ossClient, bucketName, localFilePath);
        downloadFile(ossClient, bucketName, OSSFilePath + "li(Recognize).jpg");
        // 关闭 OSS 客户端
        ossClient.shutdown();
    }
}
