package com.xuecheng.content.api;

import com.xuecheng.content.model.dto.BindTeachplanMediaDto;
import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachPlanDto;
import com.xuecheng.content.service.TeachPlanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class TeachPlanController {
    @Resource
    TeachPlanService teachPlanService;

    @ApiOperation("查询课程计划表")
    @GetMapping("/teachplan/{courseId}/tree-nodes")
    public List<TeachPlanDto> getTreeNodes(@PathVariable Long courseId) {
        List<TeachPlanDto> teachPlanTree = teachPlanService.findTeachPlanTree(courseId);
        return teachPlanTree;
    }

    @ApiOperation("修改章节内容")
    @PostMapping("/teachplan")
    public void update(@RequestBody SaveTeachplanDto teachPlanDto) {
        teachPlanService.saveTeachPlan(teachPlanDto);
    }

    @ApiOperation("绑定媒资")
    @PostMapping("/teachplan/association/media")
    public void associationMedia(@RequestBody BindTeachplanMediaDto bindTeachplanMediaDto) {
        teachPlanService.associationMedia(bindTeachplanMediaDto);
    }

}
