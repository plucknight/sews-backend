package com.example.sews.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ListObjectsRequest;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;

public class ListFilesInDirectory {
    public static void main(String[] args) {
        // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
        String endpoint = "https://oss-cn-hangzhou.aliyuncs.com";
        
        // 填写Bucket名称。
        String bucketName = "hkcam";
        
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, System.getenv("OSS_ACCESS_KEY_ID"), System.getenv("OSS_ACCESS_KEY_SECRET"));
        
        // 列举指定目录下的文件。
        listFilesInDirectory(ossClient, bucketName, "2024-10-31/device1/");
        
        // 关闭OSSClient。
        ossClient.shutdown();
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
}