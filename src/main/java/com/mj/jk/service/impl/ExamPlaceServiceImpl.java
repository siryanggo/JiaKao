package com.mj.jk.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mj.jk.common.enhance.MpPage;
import com.mj.jk.common.enhance.MpLambdaQueryWrapper;
import com.mj.jk.common.mapStruct.MapStructs;
import com.mj.jk.mapper.ExamPlaceMapper;
import com.mj.jk.pojo.vo.PageVo;
import com.mj.jk.pojo.vo.list.ExamPlaceVo;
import com.mj.jk.pojo.vo.list.ProvinceVo;
import com.mj.jk.pojo.po.ExamPlace;
import com.mj.jk.pojo.vo.req.page.ExamPlacePageReqVo;
import com.mj.jk.service.ExamPlaceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ExamPlaceServiceImpl extends ServiceImpl<ExamPlaceMapper, ExamPlace> implements ExamPlaceService {

    @Override
    @Transactional(readOnly = true)
    public PageVo<ExamPlaceVo> list(ExamPlacePageReqVo query) {
        // 查询条件
        MpLambdaQueryWrapper<ExamPlace> wrapper = new MpLambdaQueryWrapper<>();
        wrapper.like(query.getKeyword(), ExamPlace::getName, ExamPlace::getAddress);

        // 城市
        Integer cityId = query.getCityId();
        Integer provinceId = query.getProvinceId();
        if (cityId != null && cityId > 0) {
            wrapper.eq(ExamPlace::getCityId, cityId);
        } else if (provinceId != null && provinceId > 0) {
            wrapper.eq(ExamPlace::getProvinceId, provinceId);
        }

        // 排序
        wrapper.orderByDesc(ExamPlace::getId);

        // 查询
        return baseMapper
                .selectPage(new MpPage<>(query), wrapper)
                .buildVo(MapStructs.INSTANCE::po2vo);
    }

    @Override
    public List<ProvinceVo> listRegionExamPlaces() {
        return baseMapper.selectRegionExamPlaces();
    }
}