package com.mj.jk.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mj.jk.common.cache.Caches;
import com.mj.jk.common.enhance.MpLambdaQueryWrapper;
import com.mj.jk.common.enhance.MpPage;
import com.mj.jk.common.mapStruct.MapStructs;
import com.mj.jk.common.util.*;
import com.mj.jk.mapper.SysUserMapper;
import com.mj.jk.pojo.dto.SysUserDto;
import com.mj.jk.pojo.po.SysResource;
import com.mj.jk.pojo.po.SysRole;
import com.mj.jk.pojo.po.SysUser;
import com.mj.jk.pojo.po.SysUserRole;
import com.mj.jk.pojo.result.CodeMsg;
import com.mj.jk.pojo.vo.LoginVo;
import com.mj.jk.pojo.vo.PageVo;
import com.mj.jk.pojo.vo.list.SysUserVo;
import com.mj.jk.pojo.vo.req.LoginReqVo;
import com.mj.jk.pojo.vo.req.page.SysUserPageReqVo;
import com.mj.jk.pojo.vo.req.save.SysUserReqVo;
import com.mj.jk.service.SysResourceService;
import com.mj.jk.service.SysRoleService;
import com.mj.jk.service.SysUserRoleService;
import com.mj.jk.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    @Autowired
    private SysUserRoleService userRoleService;
    @Autowired
    private SysRoleService roleService;
    @Autowired
    private SysResourceService resourceService;

    @Override
    @Transactional(readOnly = true)
    public PageVo<SysUserVo> list(SysUserPageReqVo reqVo) {
        MpLambdaQueryWrapper<SysUser> wrapper = new MpLambdaQueryWrapper<>();
        wrapper.like(reqVo.getKeyword(), SysUser::getNickname, SysUser::getUsername);
        wrapper.orderByDesc(SysUser::getId);
        return baseMapper
                .selectPage(new MpPage<>(reqVo), wrapper)
                .buildVo(MapStructs.INSTANCE::po2vo);
    }

    @Override
    public boolean saveOrUpdate(SysUserReqVo reqVo) {
        // 转成PO
        SysUser po = MapStructs.INSTANCE.reqVo2po(reqVo);
        po.setPassword(Md5.getEncode(reqVo.getPassword()));
        // 保存用户信息
        if (!saveOrUpdate(po)) return false;

        Integer id = reqVo.getId();
        if (id != null && id > 0) { // 如果是做更新
            // 将更新成功的用户从缓存中移除（让token失效，用户必须重新登录）
            Caches.removeToken(Caches.get(id));

            // 删除当前用户的所有角色信息
            userRoleService.removeByUserId(reqVo.getId());
        }

        // 保存角色信息
        String roleIdsStr = reqVo.getRoleIds();
        if (Strings.isEmpty(roleIdsStr)) return true;

        String[] roleIds = roleIdsStr.split(",");
        List<SysUserRole> userRoles = new ArrayList<>();
        Integer userId = po.getId();
        for (String roleId : roleIds) { // 构建SysUserRole对象
            SysUserRole userRole = new SysUserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(Short.parseShort(roleId));
            userRoles.add(userRole);
        }
        return userRoleService.saveBatch(userRoles);
    }

    @Override
    public LoginVo login(LoginReqVo reqVo) {
        // 根据用户名查询用户
        MpLambdaQueryWrapper<SysUser> wrapper = new MpLambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername, reqVo.getUsername());
        SysUser po = baseMapper.selectOne(wrapper);

        // 用户名不存在
        if (po == null) {
            return JsonVos.raise(CodeMsg.WRONG_USERNAME);
        }

        // 密码不正确
        if (!po.getPassword().equals(reqVo.getPassword())) {
            return JsonVos.raise(CodeMsg.WRONG_PASSWORD);
        }

        // 账号锁定
        if (po.getStatus() == Constants.SysUserStatus.LOCKED) {
            return JsonVos.raise(CodeMsg.USER_LOCKED);
        }

        // 更新登录时间
        po.setLoginTime(new Date());
        baseMapper.updateById(po);

        // 生成Token，发送Token给用户
        String token = UUID.randomUUID().toString();

        // 存储token到缓存中
        SysUserDto dto = new SysUserDto();
        dto.setUser(po);
        // 根据用户id查询所有的角色：sys_role，sys_user_role
        List<SysRole> roles = roleService.listByUserId(po.getId());

        // 根据角色id查询所有的资源：sys_resource、sys_role_resource
        if (!CollectionUtils.isEmpty(roles)) {
            dto.setRoles(roles);

            List<Short> roleIds = Streams.map(roles, SysRole::getId);
            List<SysResource> resources = resourceService.listByRoleIds(roleIds);
            dto.setResources(resources);
        }

        Caches.putToken(token, dto);

        // 返回给客户端的具体数据
        LoginVo vo = MapStructs.INSTANCE.po2loginVo(po);
        vo.setToken(token);
        return vo;
    }
}