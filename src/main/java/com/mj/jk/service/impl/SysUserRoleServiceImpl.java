package com.mj.jk.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mj.jk.common.enhance.MpLambdaQueryWrapper;
import com.mj.jk.mapper.SysUserRoleMapper;
import com.mj.jk.pojo.po.SysUserRole;
import com.mj.jk.service.SysUserRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements SysUserRoleService {
    @Override
    public boolean removeByUserId(Integer userId) {
        if (userId == null || userId <= 0) return false;
        MpLambdaQueryWrapper<SysUserRole> wrapper = new MpLambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUserId, userId);
        return baseMapper.delete(wrapper) > 0;
    }
}