package com.mj.jk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mj.jk.pojo.po.SysUser;
import com.mj.jk.pojo.vo.LoginVo;
import com.mj.jk.pojo.vo.PageVo;
import com.mj.jk.pojo.vo.list.SysUserVo;
import com.mj.jk.pojo.vo.req.LoginReqVo;
import com.mj.jk.pojo.vo.req.page.SysUserPageReqVo;
import com.mj.jk.pojo.vo.req.save.SysUserReqVo;

public interface SysUserService extends IService<SysUser> {

    PageVo<SysUserVo> list(SysUserPageReqVo reqVo);

    boolean saveOrUpdate(SysUserReqVo reqVo);

    LoginVo login(LoginReqVo reqVo);
}