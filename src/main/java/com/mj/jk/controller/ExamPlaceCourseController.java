package com.mj.jk.controller;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mj.jk.common.mapStruct.MapStructs;
import com.mj.jk.common.util.Constants;
import com.mj.jk.common.util.JsonVos;
import com.mj.jk.pojo.po.ExamPlaceCourse;
import com.mj.jk.pojo.result.CodeMsg;
import com.mj.jk.pojo.vo.JsonVo;
import com.mj.jk.pojo.vo.PageJsonVo;
import com.mj.jk.pojo.vo.list.ExamPlaceCourseVo;
import com.mj.jk.pojo.vo.req.page.ExamPlaceCoursePageReqVo;
import com.mj.jk.pojo.vo.req.save.ExamPlaceCourseReqVo;
import com.mj.jk.service.ExamPlaceCourseService;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

@RestController
@RequestMapping("/examPlaceCourses")
public class ExamPlaceCourseController extends BaseController<ExamPlaceCourse, ExamPlaceCourseReqVo> {
    @Autowired
    private ExamPlaceCourseService service;

    @Override
    protected IService<ExamPlaceCourse> getService() {
        return service;
    }

    @Override
    protected Function<ExamPlaceCourseReqVo, ExamPlaceCourse> getFunction() {
        return MapStructs.INSTANCE::reqVo2po;
    }

    @GetMapping
    @ApiOperation("分页查询")
    @RequiresPermissions(Constants.Permisson.EXAM_PLACE_COURSE_LIST)
    public PageJsonVo<ExamPlaceCourseVo> list(ExamPlaceCoursePageReqVo query) {
        return JsonVos.ok(service.list(query));
    }

    @Override
    @RequiresPermissions(value = {
            Constants.Permisson.EXAM_PLACE_COURSE_ADD,
            Constants.Permisson.EXAM_PLACE_COURSE_UPDATE
    }, logical = Logical.AND)
    public JsonVo save(ExamPlaceCourseReqVo courseReqVo) {
        if (service.saveOrUpdate(courseReqVo)) {
            return JsonVos.ok(CodeMsg.SAVE_OK);
        } else {
            return JsonVos.raise(CodeMsg.SAVE_ERROR);
        }
    }

    @Override
    @RequiresPermissions(Constants.Permisson.EXAM_PLACE_COURSE_REMOVE)
    public JsonVo remove(String id) {
        List<String> idStrs = Arrays.asList(id.split(","));
        if (CollectionUtils.isEmpty(idStrs)) {
            return JsonVos.raise(CodeMsg.REMOVE_ERROR);
        }

        boolean ret = true;
        for (String idStr : idStrs) {
            if (!service.removeById(idStr)) {
                ret = false;
            }
        }

        return ret ? JsonVos.ok(CodeMsg.REMOVE_OK) : JsonVos.raise(CodeMsg.REMOVE_ERROR);
    }
}