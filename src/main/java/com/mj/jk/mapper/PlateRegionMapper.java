package com.mj.jk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mj.jk.pojo.vo.list.ProvinceVo;
import com.mj.jk.pojo.po.PlateRegion;

import java.util.List;

public interface PlateRegionMapper extends BaseMapper<PlateRegion> {
    List<ProvinceVo> selectRegions();
}