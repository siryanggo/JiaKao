package com.mj.jk.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mj.jk.common.enhance.MpLambdaQueryWrapper;
import com.mj.jk.common.enhance.MpPage;
import com.mj.jk.common.mapStruct.MapStructs;
import com.mj.jk.mapper.DictItemMapper;
import com.mj.jk.pojo.po.DictItem;
import com.mj.jk.pojo.vo.PageVo;
import com.mj.jk.pojo.vo.list.DictItemVo;
import com.mj.jk.pojo.vo.req.page.DictItemPageReqVo;
import com.mj.jk.service.DictItemService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DictItemServiceImpl extends ServiceImpl<DictItemMapper, DictItem> implements DictItemService {

    @Override
    @Transactional(readOnly = true)
    public PageVo<DictItemVo> list(DictItemPageReqVo query) {
        // 查询条件
        MpLambdaQueryWrapper<DictItem> wrapper = new MpLambdaQueryWrapper<>();
        wrapper.like(query.getKeyword(), DictItem::getName, DictItem::getValue);
        Integer typeId = query.getTypeId();
        if (typeId != null && typeId > 0) {
            wrapper.eq(DictItem::getTypeId, typeId);
        }

        // 排序
        wrapper.orderByDesc(DictItem::getId);

        // 查询
        return baseMapper
                .selectPage(new MpPage<>(query), wrapper)
                .buildVo(MapStructs.INSTANCE::po2vo);
    }
}