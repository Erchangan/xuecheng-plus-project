package com.xuecheng.media.service;

import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.base.model.RestResponse;
import com.xuecheng.media.model.dto.QueryMediaParamsDto;
import com.xuecheng.media.model.dto.UploadFileParamsDto;
import com.xuecheng.media.model.dto.UploadFileResultDto;
import com.xuecheng.media.model.po.MediaFiles;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.File;
import java.util.List;

/**
 * @author Mr.M
 * @version 1.0
 * @description 媒资文件管理业务类
 * @date 2022/9/10 8:55
 */
public interface MediaFileService {

    /**
     * @param pageParams          分页参数
     * @param queryMediaParamsDto 查询条件
     * @return com.xuecheng.base.model.PageResult<com.xuecheng.media.model.po.MediaFiles>
     * @description 媒资文件查询方法
     * @author Mr.M
     * @date 2022/9/10 8:57
     */
    public PageResult<MediaFiles> queryMediaFiels(Long companyId, PageParams pageParams, QueryMediaParamsDto queryMediaParamsDto);

    /**
     * 上传文件
     *
     * @param companyId           公司ID
     * @param uploadFileParamsDto 请求参数
     * @param localFilepath       文件的本地路径
     * @return
     */
    public UploadFileResultDto uploadFile(Long companyId, UploadFileParamsDto uploadFileParamsDto, String localFilepath,String objectName);

    /**
     * 向数据库中插入媒资信息
     *
     * @param companyId
     * @param fileMd5
     * @param uploadFileParamsDto
     * @param bucket
     * @param objectName
     * @return
     */
    public MediaFiles addMediaFilesToDb(Long companyId, String fileMd5, UploadFileParamsDto uploadFileParamsDto, String bucket, String objectName);

    /**
     * 检查文件是否存在
     *
     * @param md5 文件id
     * @return
     */
    public RestResponse<Boolean> checkFile(String md5);

    /**
     * 查询minio是否存在分块
     *
     * @param md5
     * @param chunkIndex
     * @return
     */
    public RestResponse<Boolean> checkChunk(String md5, int chunkIndex);

    /**
     * 上传分块
     *
     * @param md5
     * @param chunk
     * @param localChunkPath
     * @return
     */
    public RestResponse<Boolean> uploadChunk(String md5, int chunk, String localChunkPath);

    /**
     * 合并分块
     *
     * @param companyId
     * @param fileMd5
     * @param uploadFileParamsDto 上传文件参数信息
     * @return
     */
    public RestResponse<Boolean> mergeChunk(Long companyId, String fileMd5, int chunkTotal, UploadFileParamsDto uploadFileParamsDto);

    /**
     * 下载文件
     *
     * @param bucket
     * @param objectName
     * @return
     */
    public File downloadFileFromMinIO(String bucket, String objectName);

    /**
     * 上传视频到minio
     *
     * @param localFilePath 本地路径
     * @param mimeType      文件类型
     * @param bucket
     * @param objectName    minio中的文件路径
     * @return
     */
    public boolean addMediaFileToMinio(String localFilePath, String mimeType, String bucket, String objectName);

    /**
     * 根据id查询文件信息
     * @param mediaId
     * @return
     */
    MediaFiles getFileById(String mediaId);
}