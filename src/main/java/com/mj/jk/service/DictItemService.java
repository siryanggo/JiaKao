package com.mj.jk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mj.jk.pojo.po.DictItem;
import com.mj.jk.pojo.vo.PageVo;
import com.mj.jk.pojo.vo.list.DictItemVo;
import com.mj.jk.pojo.vo.req.page.DictItemPageReqVo;

public interface DictItemService extends IService<DictItem> {
    PageVo<DictItemVo> list(DictItemPageReqVo query);
}