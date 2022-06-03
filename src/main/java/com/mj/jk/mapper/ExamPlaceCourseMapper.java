package com.mj.jk.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.mj.jk.common.enhance.MpPage;
import com.mj.jk.pojo.po.ExamPlaceCourse;
import com.mj.jk.pojo.vo.list.ExamPlaceCourseVo;
import org.apache.ibatis.annotations.Param;

public interface ExamPlaceCourseMapper extends BaseMapper<ExamPlaceCourse> {
    MpPage<ExamPlaceCourseVo> selectPageVos(MpPage<ExamPlaceCourseVo> page,
                                            @Param(Constants.WRAPPER) Wrapper<ExamPlaceCourseVo> wrapper);
}