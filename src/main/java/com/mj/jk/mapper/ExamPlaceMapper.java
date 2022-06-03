package com.mj.jk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mj.jk.pojo.vo.list.ProvinceVo;
import com.mj.jk.pojo.po.ExamPlace;

import java.util.List;

public interface ExamPlaceMapper extends BaseMapper<ExamPlace> {

    List<ProvinceVo> selectRegionExamPlaces();
}