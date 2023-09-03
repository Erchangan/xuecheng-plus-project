package com.xuecheng.content.service;

import com.xuecheng.content.model.dto.CoursePreviewDto;
import com.xuecheng.content.model.po.CoursePublish;

import java.io.File;

public interface CoursePublishService {
    /**
     * @param courseId 课程id
     * @return com.xuecheng.content.model.dto.CoursePreviewDto
     * @description 获取课程预览信息
     * @author Mr.M
     * @date 2022/9/16 15:36
     */
    public CoursePreviewDto getCoursePreviewInfo(Long courseId);

    /**
     * @param courseId 课程id
     * @return void
     * @description 提交审核
     * @author Mr.M
     * @date 2022/9/18 10:31
     */
    public void commitAudit(Long companyId, Long courseId);

    /**
     * @param companyId 机构id
     * @param courseId  课程id
     * @return void
     * @description 课程发布接口
     * @author Mr.M
     * @date 2022/9/20 16:23
     */
    public void publish(Long companyId, Long courseId);

    /**
     * 生成html页面
     * @param courseId
     */
    public File generateCourseHtml(Long courseId);

    /**
     * 上传html静态文件到minio
     * @param courseId
     * @param file
     */
    public void uploadCourseHtml(Long courseId, File file);

    /**
     * 查询课程发布信息
     * @param courseId
     * @return
     */
    CoursePublish getCoursePublish(Long courseId);
}
