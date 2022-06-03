package com.mj.jk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mj.jk.pojo.po.DictType;
import com.mj.jk.pojo.vo.PageVo;
import com.mj.jk.pojo.vo.list.DictTypeVo;
import com.mj.jk.pojo.vo.req.page.DictTypePageReqVo;

public interface DictTypeService extends IService<DictType> {
    PageVo<DictTypeVo> list(DictTypePageReqVo query);
}