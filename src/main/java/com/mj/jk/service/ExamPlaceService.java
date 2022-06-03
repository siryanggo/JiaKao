package com.mj.jk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mj.jk.pojo.vo.PageVo;
import com.mj.jk.pojo.vo.list.ExamPlaceVo;
import com.mj.jk.pojo.vo.list.ProvinceVo;
import com.mj.jk.pojo.po.ExamPlace;
import com.mj.jk.pojo.vo.req.page.ExamPlacePageReqVo;

import java.util.List;

public interface ExamPlaceService extends IService<ExamPlace> {
    PageVo<ExamPlaceVo> list(ExamPlacePageReqVo query);

    List<ProvinceVo> listRegionExamPlaces();
}