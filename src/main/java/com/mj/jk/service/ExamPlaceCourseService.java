package com.mj.jk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mj.jk.pojo.po.ExamPlaceCourse;
import com.mj.jk.pojo.vo.PageVo;
import com.mj.jk.pojo.vo.list.ExamPlaceCourseVo;
import com.mj.jk.pojo.vo.req.page.ExamPlaceCoursePageReqVo;
import com.mj.jk.pojo.vo.req.save.ExamPlaceCourseReqVo;

public interface ExamPlaceCourseService extends IService<ExamPlaceCourse> {
    PageVo<ExamPlaceCourseVo> list(ExamPlaceCoursePageReqVo query);
    boolean saveOrUpdate(ExamPlaceCourseReqVo reqVo);
}