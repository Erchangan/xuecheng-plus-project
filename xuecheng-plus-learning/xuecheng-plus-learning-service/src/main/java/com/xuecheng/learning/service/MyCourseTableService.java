package com.xuecheng.learning.service;

import com.xuecheng.base.model.PageResult;
import com.xuecheng.learning.model.dto.MyCourseTableParams;
import com.xuecheng.learning.model.dto.XcChooseCourseDto;
import com.xuecheng.learning.model.dto.XcCourseTablesDto;
import com.xuecheng.learning.model.po.XcCourseTables;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


public interface MyCourseTableService {
    XcChooseCourseDto addChooseCourse(String userId, Long courseId);

    XcCourseTablesDto getLearningStatus(String userId, Long courseID);

    boolean saveChooseCourseSuccess(String chooseCourseId);

    PageResult<XcCourseTables> myCourseTbale(MyCourseTableParams myCourseTableParams);
}
