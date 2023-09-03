package com.xuecheng.content;

import com.xuecheng.content.mapper.TeachplanMapper;
import com.xuecheng.content.model.dto.TeachPlanDto;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
public class TeachPlanMapperTest {
    @Resource
    TeachplanMapper teachplanMapper;

    @Test
    public void test() {
        List<TeachPlanDto> teachPlanDto = teachplanMapper.selectTreeNodes(117L);
        System.out.println(teachPlanDto);
    }
}
