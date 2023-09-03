package com.xuecheng.media.service.jobhandler;

import com.xuecheng.base.utils.Mp4VideoUtil;
import com.xuecheng.media.model.po.MediaProcess;
import com.xuecheng.media.service.MediaFileProcessService;
import com.xuecheng.media.service.MediaFileService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Component
@Slf4j
public class VideoTask {
    @Autowired
    MediaFileProcessService mediaFileProcessService;
    @Autowired
    MediaFileService mediaFileService;
    @Value("${videoprocess.ffmpegpath}")
    private String ffmpeg;

    @XxlJob("videoJobHandler")
    public void videoJobHandler() {
        //获取当前分片序号
        int shardIndex = XxlJobHelper.getShardIndex();
        //获取分片总数，就是集群中执行器的个数
        int shardTotal = XxlJobHelper.getShardTotal();
        //从数据查询待处理任务
        //获取cpu核心数,确定多少个线程能执行任务
        int processor = Runtime.getRuntime().availableProcessors();
        List<MediaProcess> mediaProcessList =
                mediaFileProcessService.getMediaProcessList(shardIndex, shardTotal, processor);
        //根据查询到待处理任务的个数来确定线程池
        int size = mediaProcessList.size();
        if(size<=0){
            log.debug("没有视频需要处理");
            return;
        }
        //通过线程池来处理任务
        ExecutorService executorService = Executors.newFixedThreadPool(size);
        //遍历待处理的任务
        CountDownLatch countDownLatch=new CountDownLatch(size);
        mediaProcessList.forEach(mediaProcess -> {
            Long taskId = mediaProcess.getId();
            //文件ID就是md5值
            String fileId = mediaProcess.getFileId();
            //将任务加入线程池
            executorService.execute(() -> {
                //开启任务
                boolean b = mediaFileProcessService.startTask(taskId);
                if (!b) {
                    log.debug("抢占任务失败,任务id{}", taskId);
                    return;
                }
                String bucket = mediaProcess.getBucket();
                String objectName = mediaProcess.getFilePath();
                //下载minio视频到本地
                File file = mediaFileService.downloadFileFromMinIO(bucket, objectName);
                if (file == null) {
                    log.debug("下载视频出错，任务id:{}，bucket:{}，objectName:{}", taskId, bucket, objectName);
                    //todo
                    mediaFileProcessService.saveProcessFinishStatus(taskId,"3",fileId,null,"下载视频到本地失败");
                    return;
                }
                //源avi视频的路径
                String video_path = file.getAbsolutePath();
                //转换后mp4文件的名称
                String mp4_name = fileId + "mp4";
                File mp4File = null;
                try {
                    mp4File = File.createTempFile("minio", ".mp4");
                } catch (IOException e) {
                    log.debug("创建临时文件异常，{}", e.getMessage());
                    //todo
                    mediaFileProcessService.saveProcessFinishStatus(taskId, "3", fileId, null, "创建临时文件异常");
                    return;
                }
                //转换后mp4文件的路径
                String mp4_path =mp4File.getAbsolutePath();
                //创建工具类对象
                Mp4VideoUtil videoUtil = new Mp4VideoUtil(ffmpeg, video_path, mp4_name, mp4_path);
                //开始视频转换，成功将返回success
                String result = videoUtil.generateMp4();
                if(!result.equals("success")){
                    log.debug("视频转码失败,原因：{}bucket:{},objectName:{}",result,bucket,objectName);
                    //todo
                    mediaFileProcessService.saveProcessFinishStatus(taskId, "3", fileId, null, result);
                    return;
                }
                //上传到minio
                boolean b1 = mediaFileService.addMediaFileToMinio(mp4File.getAbsolutePath(), "video/mp4", bucket, objectName);
                if(!b1){
                    log.debug("上传mp4到minio失败，taskId:{}",taskId);
                    mediaFileProcessService.saveProcessFinishStatus(taskId,"3",fileId,null,"上传到minio失败");
                    return;
                }
                //获取文件的url
                String url = getFilePathByMd5(fileId, ".mp4");
                mediaFileProcessService.saveProcessFinishStatus(taskId,"2",fileId,url,"创建临时文件异常");
                countDownLatch.countDown();
                //保存任务处理结果
            });
        });
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    //合并后的文件路径
    private String getFilePathByMd5(String fileMd5, String fileExt) {
        return fileMd5.substring(0, 1) + "/" + fileMd5.substring(1, 2) + "/" + fileMd5 + "/" + fileMd5 + fileExt;
    }
}
