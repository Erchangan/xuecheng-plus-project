package com.xuecheng.content.service;

import com.xuecheng.content.model.dto.BindTeachplanMediaDto;
import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachPlanDto;

import java.util.List;

public interface TeachPlanService {
    /**
     * 查询课程计划
     * @param courseId 课程ID
     * @return
     */
    List<TeachPlanDto> findTeachPlanTree(Long courseId);

    /**
     * 新增/修改/保存
     * @param saveTeachplanDto 课程计划修改
     */
    void saveTeachPlan(SaveTeachplanDto saveTeachplanDto);

    /**
     * 绑定媒资
     * @param bindTeachplanMediaDto 绑定的美的信息
     */
    void associationMedia(BindTeachplanMediaDto bindTeachplanMediaDto);


}
