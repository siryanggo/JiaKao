package com.mj.jk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mj.jk.pojo.po.SysUserRole;

public interface SysUserRoleService extends IService<SysUserRole> {
    boolean removeByUserId(Integer userId);
}