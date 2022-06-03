package com.mj.jk.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mj.jk.common.enhance.MpLambdaQueryWrapper;
import com.mj.jk.common.enhance.MpPage;
import com.mj.jk.common.mapStruct.MapStructs;
import com.mj.jk.mapper.DictTypeMapper;
import com.mj.jk.pojo.po.DictType;
import com.mj.jk.pojo.vo.PageVo;
import com.mj.jk.pojo.vo.list.DictTypeVo;
import com.mj.jk.pojo.vo.req.page.DictTypePageReqVo;
import com.mj.jk.service.DictTypeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DictTypeServiceImpl extends ServiceImpl<DictTypeMapper, DictType> implements DictTypeService {
    @Override
    @Transactional(readOnly = true)
    public PageVo<DictTypeVo> list(DictTypePageReqVo query) {
        // 查询条件
        MpLambdaQueryWrapper<DictType> wrapper = new MpLambdaQueryWrapper<>();

        // 根据关键字查询
        wrapper.like(query.getKeyword(),
                DictType::getName,
                DictType::getValue,
                DictType::getIntro);

        // 按照id降序
        wrapper.orderByDesc(DictType::getId);

        // 分页查询
        return baseMapper
                .selectPage(new MpPage<>(query), wrapper)
                .buildVo(MapStructs.INSTANCE::po2vo);
    }
}