package com.example.minio.util;

import com.example.minio.common.HttpStatus;
import com.example.minio.config.MinioConfig;
import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import lombok.SneakyThrows;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Description Minio工具类
 */

@Component
public class MinioUtils {

    @Autowired
    private MinioClient minioClient;

    @Autowired
    private MinioConfig configuration;

    /**
     * @param name 名字
     * @Description description: 判断bucket是否存在，不存在则创建
     */
    public boolean existBucket(String name) {
        boolean exists;
        try {
            exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(name).build());
            if (!exists) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(name).build());
                exists = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            exists = false;
        }
        return exists;
    }

    /**
     * @param bucketName 存储bucket名称
     * @Description 创建存储bucket
     */
    public Boolean makeBucket(String bucketName) {
        try {
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(bucketName)
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * @param bucketName 存储bucket名称
     * @Description 删除存储bucket
     */
    public Boolean removeBucket(String bucketName) {
        try {
            minioClient.removeBucket(RemoveBucketArgs.builder()
                    .bucket(bucketName)
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * @param fileName 文件名称
     * @param time     时间
     * @Description 获取上传临时签名
     */
    @SneakyThrows
    public Map getPolicy(String fileName, ZonedDateTime time) {
        PostPolicy postPolicy = new PostPolicy(configuration.getBucketName(), time);
        postPolicy.addEqualsCondition("key", fileName);
        try {
            Map<String, String> map = minioClient.getPresignedPostFormData(postPolicy);
            HashMap<String, String> map1 = new HashMap<>();
            map.forEach((k, v) -> {
                map1.put(k.replaceAll("-", ""), v);
            });
            map1.put("host", configuration.getUrl() + "/" + configuration.getBucketName());
            return map1;
        } catch (ErrorResponseException e) {
            e.printStackTrace();
        } catch (InsufficientDataException e) {
            e.printStackTrace();
        } catch (InternalException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidResponseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (XmlParserException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param objectName 对象名称
     * @param method     方法
     * @param time       时间
     * @param timeUnit   时间单位
     * @Description 获取上传文件的url
     */
    public String getPolicyUrl(String objectName, Method method, int time, TimeUnit timeUnit) {
        try {
            return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .method(method)
                    .bucket(configuration.getBucketName())
                    .object(objectName)
                    .expiry(time, timeUnit).build());
        } catch (ErrorResponseException e) {
            e.printStackTrace();
        } catch (InsufficientDataException e) {
            e.printStackTrace();
        } catch (InternalException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidResponseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (XmlParserException e) {
            e.printStackTrace();
        } catch (ServerException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getPolicyUrl(String objectName, Method method) {
        try {
            return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .method(method)
                    .bucket(configuration.getBucketName())
                    .object(objectName)
                    .build());
        } catch (ErrorResponseException e) {
            e.printStackTrace();
        } catch (InsufficientDataException e) {
            e.printStackTrace();
        } catch (InternalException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidResponseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (XmlParserException e) {
            e.printStackTrace();
        } catch (ServerException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void uploadImageToMinio(Mat resultImage, String imageUrl) {
        try {
            // 如果 imageUrl 中不包含协议，则假定其为文件名，需要自动补全 base URL

            URL url = new URL(imageUrl);
            String path = url.getPath();  // 如 "/product/tao_li2.jpg"
            String fileName = path.substring(path.lastIndexOf('/') + 1); // 提取 "tao_li2.jpg"
            int dotIndex = fileName.lastIndexOf(".");
            String nameWithoutExt = (dotIndex == -1) ? fileName : fileName.substring(0, dotIndex);
            String extension = (dotIndex == -1) ? ".jpg" : fileName.substring(dotIndex);
            String newFileName = nameWithoutExt + "(Recognize)" + extension;

            // 将 Mat 转换为 JPEG 格式的字节数组
            MatOfByte mob = new MatOfByte();
            Imgcodecs.imencode(".jpg", resultImage, mob);
            byte[] imageBytes = mob.toArray();

            // 指定上传到的 bucket 名称（这里假设 bucket 为 "Recognize"）
            String bucketName = "Recognize";

            // 如果 bucket 不存在则创建
            boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!bucketExists) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }

            // 调用 MinIO 的 putObject 方法上传图片
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
        } catch (Exception e) { // 包含 putObject 等操作可能抛出的异常
            e.printStackTrace();
        }
    }
    /**
     * @param file     文件
     * @param fileName 文件名称
     * @Description 上传文件
     */
    public void upload(MultipartFile file, String fileName) {
        // 使用putObject上传一个文件到存储桶中。
        try {
            InputStream inputStream = file.getInputStream();
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(configuration.getBucketName())
                    .object(fileName)
                    .stream(inputStream, file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build());
        } catch (ErrorResponseException e) {
            e.printStackTrace();
        } catch (InsufficientDataException e) {
            e.printStackTrace();
        } catch (InternalException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidResponseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (XmlParserException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param objectName 对象名称
     * @param time       时间
     * @param timeUnit   时间单位
     * @Description 根据filename获取文件访问地址
     */
    public String getUrl(String objectName, int time, TimeUnit timeUnit) {
        String url = null;
        try {
            url = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(configuration.getBucketName())
                    .object(objectName)
                    .expiry(time, timeUnit).build());
        } catch (ErrorResponseException e) {
            e.printStackTrace();
        } catch (InsufficientDataException e) {
            e.printStackTrace();
        } catch (InternalException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidResponseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (XmlParserException e) {
            e.printStackTrace();
        } catch (ServerException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * @Description description: 下载文件
     */
    public ResponseEntity<byte[]> download(String fileName) {
        ResponseEntity<byte[]> responseEntity = null;
        InputStream in = null;
        ByteArrayOutputStream out = null;
        try {
            in = minioClient.getObject(GetObjectArgs.builder().bucket(configuration.getBucketName()).object(fileName).build());
            out = new ByteArrayOutputStream();
            IOUtils.copy(in, out);
            //封装返回值
            byte[] bytes = out.toByteArray();
            HttpHeaders headers = new HttpHeaders();
            try {
                headers.add("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            headers.setContentLength(bytes.length);
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setAccessControlExposeHeaders(Arrays.asList("*"));
            responseEntity = new ResponseEntity<byte[]>(bytes, headers, HttpStatus.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return responseEntity;
    }

    /**
     * @param objectFile 对象文件
     */
    public String getFileUrl(String objectFile) {
        try {

            return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(configuration.getBucketName())
                    .object(objectFile)
                    .build()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
