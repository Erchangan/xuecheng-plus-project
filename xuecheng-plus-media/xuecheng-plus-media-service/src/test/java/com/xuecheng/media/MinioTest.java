package com.xuecheng.media;

import io.minio.*;
import io.minio.errors.*;
import org.junit.jupiter.api.Test;

import java.io.FilterInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;


public class MinioTest {
    static MinioClient minioClient = MinioClient.builder()
            .endpoint("http://192.168.190.128:9000")
            .credentials("minioadmin", "minioadmin")
            .build();

    @Test
    public void test_upload() throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        //上传文件的参数信息
        UploadObjectArgs uploadObjectArgs = UploadObjectArgs.builder()
                .bucket("testbucket")//指定要上传的桶
                .filename("F:\\视频素材\\1.mp4")//指定本地文件路径
                .object("test1/1.mp4")//上传之后桶中对象的路径+文件名
                .contentType("video/mp4")
                .build();
        //上传文件
        minioClient.uploadObject(uploadObjectArgs);
    }

    @Test
    public void test_delete() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        //确定删除文件的信息
        RemoveObjectArgs removeObjectArgs = RemoveObjectArgs.builder()
                .bucket("testbucket")
                .object("1.mp4")
                .build();
//        删除文件
        minioClient.removeObject(removeObjectArgs);
    }
    @Test
    public void test_get() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        GetObjectArgs getObjectArgs = GetObjectArgs.builder()
                .bucket("testbucket")
                .object("test1/1.mp4")
                .build();
        FilterInputStream inputStream=minioClient.getObject(getObjectArgs);
    }
}
