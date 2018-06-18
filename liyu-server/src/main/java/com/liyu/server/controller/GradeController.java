package com.liyu.server.controller;

import com.liyu.server.service.GradeService;
import com.liyu.server.tables.pojos.Grade;
import com.liyu.server.utils.APIResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Api(value = "年级", description = "年级操作", tags = {"年级接口"})
@RestController
@Slf4j
@RequestMapping(value = "/api/grades")
public class GradeController {
    @Resource
    private GradeService gradeService;

    @ApiOperation(value = "获取年级列表", notes = "")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public APIResponse list() {
        List<Grade> list = gradeService.list();
        return APIResponse.success(list);
    }

    @ApiOperation(value = "创建年级", notes = "")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public APIResponse create(@RequestBody Grade newGrade) {
        Grade grade = gradeService.create(newGrade);
        return APIResponse.success(grade);
    }

    @ApiOperation(value = "修改年级", notes = "")
    @RequestMapping(value = "/{gradeId}", method = RequestMethod.PUT)
    public APIResponse create(@PathVariable String gradeId,
                              @RequestBody Grade newGrade) {
        Grade grade = gradeService.update(gradeId, newGrade);
        return APIResponse.success(grade);
    }

    @ApiOperation(value = "删除年级", notes = "")
    @RequestMapping(value = "/gradeId", method = RequestMethod.DELETE)
    public APIResponse delete(@PathVariable String gradeId) {
        gradeService.delete(gradeId);
        return APIResponse.success("success");
    }
}
