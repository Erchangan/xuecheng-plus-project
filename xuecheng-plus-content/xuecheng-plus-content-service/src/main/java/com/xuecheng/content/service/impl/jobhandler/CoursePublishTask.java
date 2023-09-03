package com.xuecheng.content.service.impl.jobhandler;

import com.xuecheng.base.exception.XueChengPlusException;
import com.xuecheng.content.model.po.CoursePublish;
import com.xuecheng.content.service.CoursePublishService;
import com.xuecheng.messagesdk.model.po.MqMessage;
import com.xuecheng.messagesdk.service.MessageProcessAbstract;
import com.xuecheng.messagesdk.service.MqMessageService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@Slf4j
public class CoursePublishTask extends MessageProcessAbstract {
    @Autowired
    CoursePublishService coursePublishService;

    @XxlJob("CoursePublishHandler")
    public void coursePublishHandler() {
        int shardIndex = XxlJobHelper.getShardIndex();
        int shardTotal = XxlJobHelper.getShardTotal();
        process(shardIndex,shardTotal,"course_publish",30,60);
    }

    @Override
    public boolean execute(MqMessage mqMessage) {
        //获取消息相关的业务信息
        String businessKey1 = mqMessage.getBusinessKey1();
        long courseId = Integer.parseInt(businessKey1);
        //课程静态化
        generateCourseHtml(mqMessage, courseId);
        //课程索引
        saveCourseIndex(mqMessage, courseId);
        //课程缓存
        saveCourseCache(mqMessage, courseId);
        return true;

    }

    private void saveCourseCache(MqMessage mqMessage, long courseId) {
    }

    private void saveCourseIndex(MqMessage mqMessage, long courseId) {

    }
    //页面静态化
    private void generateCourseHtml(MqMessage mqMessage, long courseId) {
        Long taskId = mqMessage.getId();
        MqMessageService mqMessageService = this.getMqMessageService();
        //生成html页面
        int stageOne = mqMessageService.getStageOne(taskId);
        if(stageOne>0){
            log.debug("课程静态化处理完成，无需处理");
            return;
        }
        File file=coursePublishService.generateCourseHtml(courseId);
        if(file==null){
            XueChengPlusException.cast("生成的静态页面为空");
        }
        //上传到minio
        coursePublishService.uploadCourseHtml(courseId,file);
        mqMessageService.completedStageOne(taskId);
    }
}
