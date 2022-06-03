package com.mj.jk.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.promeg.pinyinhelper.Pinyin;
import com.mj.jk.common.enhance.MpLambdaQueryWrapper;
import com.mj.jk.common.enhance.MpPage;
import com.mj.jk.common.mapStruct.MapStructs;
import com.mj.jk.common.util.Streams;
import com.mj.jk.mapper.PlateRegionMapper;
import com.mj.jk.pojo.vo.PageVo;
import com.mj.jk.pojo.vo.list.PlateRegionVo;
import com.mj.jk.pojo.vo.list.ProvinceVo;
import com.mj.jk.pojo.po.PlateRegion;
import com.mj.jk.pojo.vo.req.page.CityPageReqVo;
import com.mj.jk.pojo.vo.req.page.ProvincePageReqVo;
import com.mj.jk.service.PlateRegionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PlateRegionServiceImpl extends ServiceImpl<PlateRegionMapper, PlateRegion> implements PlateRegionService {

    @Override
    public boolean save(PlateRegion entity) {
        processPinyin(entity);
        return super.save(entity);
    }

    @Override
    public boolean updateById(PlateRegion entity) {
        processPinyin(entity);
        return super.updateById(entity);
    }

    private void processPinyin(PlateRegion region) {
        String name = region.getName();
        if (name == null) return;

        region.setPinyin(Pinyin.toPinyin(name, "_"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProvinceVo> listRegions() {
        return baseMapper.selectRegions();
    }

//    @Override
//    public List<ProvinceDto> listRegions() {
//        List<PlateRegion> dbRegions = baseMapper.selectList(null);
//        List<ProvinceDto> regions = new ArrayList<>();
//        for (PlateRegion dbRegion : dbRegions) {
//
//        }
//        return regions;
//    }

    @Override
    @Transactional(readOnly = true)
    public PageVo<PlateRegionVo> listProvinces(ProvincePageReqVo query) {
        MpLambdaQueryWrapper<PlateRegion> wrapper = new MpLambdaQueryWrapper<>();
        wrapper.like(query.getKeyword(),
                PlateRegion::getName,
                PlateRegion::getPlate,
                PlateRegion::getPinyin);
        // 所有省份
        wrapper.eq(PlateRegion::getParentId, 0);
        wrapper.orderByDesc(PlateRegion::getId);
        return baseMapper
                .selectPage(new MpPage<>(query), wrapper)
                .buildVo(MapStructs.INSTANCE::po2vo);
    }

    @Override
    @Transactional(readOnly = true)
    public PageVo<PlateRegionVo> listCities(CityPageReqVo query) {
        MpLambdaQueryWrapper<PlateRegion> wrapper = new MpLambdaQueryWrapper<>();
        wrapper.like(query.getKeyword(),
                PlateRegion::getName,
                PlateRegion::getPlate,
                PlateRegion::getPinyin);
        Integer provinceId = query.getParentId();
        if (provinceId != null && provinceId > 0) { // provinceId下面的所有城市
            wrapper.eq(PlateRegion::getParentId, provinceId);
        } else { // 所有城市
            wrapper.ne(PlateRegion::getParentId, 0);
        }
        wrapper.orderByDesc(PlateRegion::getId);
        return baseMapper
                .selectPage(new MpPage<>(query), wrapper)
                .buildVo(MapStructs.INSTANCE::po2vo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlateRegionVo> listProvinces() {
        MpLambdaQueryWrapper<PlateRegion> wrapper = new MpLambdaQueryWrapper<>();
        wrapper.eq(PlateRegion::getParentId, 0);
        wrapper.orderByAsc(PlateRegion::getPinyin);
        return Streams.map(baseMapper.selectList(wrapper), MapStructs.INSTANCE::po2vo);
    }
}