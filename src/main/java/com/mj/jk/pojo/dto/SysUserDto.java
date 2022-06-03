package com.mj.jk.pojo.dto;

import com.mj.jk.pojo.po.SysResource;
import com.mj.jk.pojo.po.SysRole;
import com.mj.jk.pojo.po.SysUser;
import lombok.Data;

import java.util.List;

@Data
public class SysUserDto {
    private SysUser user;
    private List<SysRole> roles;
    private List<SysResource> resources;
}
