package com.mj.jk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mj.jk.pojo.vo.PageVo;
import com.mj.jk.pojo.vo.list.PlateRegionVo;
import com.mj.jk.pojo.vo.list.ProvinceVo;
import com.mj.jk.pojo.po.PlateRegion;
import com.mj.jk.pojo.vo.req.page.CityPageReqVo;
import com.mj.jk.pojo.vo.req.page.ProvincePageReqVo;

import java.util.List;

public interface PlateRegionService extends IService<PlateRegion> {
    List<ProvinceVo> listRegions();

    PageVo<PlateRegionVo> listProvinces(ProvincePageReqVo query);

    PageVo<PlateRegionVo> listCities(CityPageReqVo query);

    List<PlateRegionVo> listProvinces();
}