package com.xuecheng.learning.service.Impl;

import com.alibaba.fastjson.JSON;
import com.xuecheng.base.model.RestResponse;
import com.xuecheng.base.utils.StringUtil;
import com.xuecheng.content.model.dto.TeachPlanDto;
import com.xuecheng.content.model.po.CoursePublish;
import com.xuecheng.content.model.po.TeachplanMedia;
import com.xuecheng.learning.feignclient.ContentServiceClient;
import com.xuecheng.learning.feignclient.MediaServiceClient;
import com.xuecheng.learning.model.dto.XcCourseTablesDto;
import com.xuecheng.learning.service.LearningService;
import com.xuecheng.learning.service.MyCourseTableService;
import freemarker.cache.ConditionalTemplateConfigurationFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class LearningServiceImpl implements LearningService {
    @Autowired
    MyCourseTableService myCourseTableService;
    @Autowired
    ContentServiceClient contentServiceClient;
    @Autowired
    MediaServiceClient mediaServiceClient;

    @Override
    public RestResponse<String> getVideo(String userId, Long courseId, Long teachplanId, String mediaId) {
        CoursePublish coursepublish = contentServiceClient.getCoursepublish(courseId);
        if (coursepublish == null) {
            return RestResponse.validfail("课程不存在");
        }
        //判断是否支持试学
        String teachplanJson = coursepublish.getTeachplan();
        List<TeachPlanDto> teachPlanDtos = JSON.parseArray(teachplanJson, TeachPlanDto.class);
        teachPlanDtos.forEach(teachPlanDto -> {
            String isPreview = teachPlanDto.getIsPreview();
            if (StringUtil.isNotBlank(isPreview)) {
                RestResponse<String> playUrlByMediaId = mediaServiceClient.getPlayUrlByMediaId(mediaId);
                return;
            }

        });
        //用户登录的情况，获取学习资格
        if (StringUtil.isNotBlank(userId)) {
            XcCourseTablesDto learningStatus = myCourseTableService.getLearningStatus(userId, courseId);
            String learnStatus = learningStatus.getLearnStatus();
            if ("701002".equals(learnStatus)) {
                return RestResponse.validfail("无法学习，没有选课或者没有支付");
            } else if ("701003".equals(learnStatus)) {
                return RestResponse.validfail("已过期，续费之后才能观看");
            } else {
                //远程调用媒资服务，获取视频的播放地址
                RestResponse<String> playUrlByMediaId = mediaServiceClient.getPlayUrlByMediaId(mediaId);
                return playUrlByMediaId;

            }
        }
        //用户没有登陆的情况
        String charge = coursepublish.getCharge();
        if ("201000".equals(charge)) {
            //有资格学习
            RestResponse<String> playUrlByMediaId = mediaServiceClient.getPlayUrlByMediaId(mediaId);
            return playUrlByMediaId;
        }

        //远程调用获取视频url
        return RestResponse.validfail("该课程需要付费");
    }
}
