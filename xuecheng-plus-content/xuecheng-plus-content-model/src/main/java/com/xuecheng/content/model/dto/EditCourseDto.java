package com.xuecheng.content.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class EditCourseDto extends CourseBaseInfoDto{
    @ApiModelProperty(value="课程Id",required = true)
    private Long id;
}
