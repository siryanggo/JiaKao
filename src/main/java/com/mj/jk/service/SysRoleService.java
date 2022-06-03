package com.mj.jk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mj.jk.pojo.po.SysRole;
import com.mj.jk.pojo.vo.PageVo;
import com.mj.jk.pojo.vo.list.SysRoleVo;
import com.mj.jk.pojo.vo.req.page.SysRolePageReqVo;
import com.mj.jk.pojo.vo.req.save.SysRoleReqVo;

import java.util.List;

public interface SysRoleService extends IService<SysRole> {

    PageVo<SysRoleVo> list(SysRolePageReqVo reqVo);

    List<Short> listIds(Integer userId);

    boolean saveOrUpdate(SysRoleReqVo reqVo);

    List<SysRole> listByUserId(Integer userId);
}