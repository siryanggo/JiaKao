package com.mj.jk.controller;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mj.jk.common.mapStruct.MapStructs;
import com.mj.jk.common.util.Constants;
import com.mj.jk.common.util.JsonVos;
import com.mj.jk.common.util.Streams;
import com.mj.jk.pojo.po.SysRole;
import com.mj.jk.pojo.result.CodeMsg;
import com.mj.jk.pojo.vo.DataJsonVo;
import com.mj.jk.pojo.vo.JsonVo;
import com.mj.jk.pojo.vo.PageJsonVo;
import com.mj.jk.pojo.vo.list.SysRoleVo;
import com.mj.jk.pojo.vo.req.page.SysRolePageReqVo;
import com.mj.jk.pojo.vo.req.save.SysRoleReqVo;
import com.mj.jk.service.SysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.function.Function;

@RestController
@RequestMapping("/sysRoles")
@Api(tags = "角色")
public class SysRoleController extends BaseController<SysRole, SysRoleReqVo> {
    @Autowired
    private SysRoleService service;

    @GetMapping("/ids")
    @ApiOperation("根据用户id获取角色id")
    @RequiresPermissions(Constants.Permisson.SYS_ROLE_LIST)
    public DataJsonVo<List<Short>> ids(Integer userId) {
        return JsonVos.ok(service.listIds(userId));
    }

    @GetMapping("/list")
    @ApiOperation("查询所有")
    @RequiresPermissions(Constants.Permisson.SYS_ROLE_LIST)
    public DataJsonVo<List<SysRoleVo>> list() {
        return JsonVos.ok(Streams.map(service.list(), MapStructs.INSTANCE::po2vo));
    }

    @GetMapping
    @ApiOperation("分页查询")
    @RequiresPermissions(Constants.Permisson.SYS_ROLE_LIST)
    public PageJsonVo<SysRoleVo> list(SysRolePageReqVo reqVo) {
        return JsonVos.ok(service.list(reqVo));
    }

    @Override
    @RequiresPermissions(value = {
            Constants.Permisson.SYS_ROLE_ADD,
            Constants.Permisson.SYS_ROLE_UPDATE
    }, logical = Logical.AND)
    public JsonVo save(SysRoleReqVo reqVo) {
        if (service.saveOrUpdate(reqVo)) {
            return JsonVos.ok(CodeMsg.SAVE_OK);
        } else {
            return JsonVos.raise(CodeMsg.SAVE_ERROR);
        }
    }

    @Override
    @RequiresPermissions(Constants.Permisson.SYS_ROLE_REMOVE)
    public JsonVo remove(String id) {
        return super.remove(id);
    }

    @Override
    protected IService<SysRole> getService() {
        return service;
    }

    @Override
    protected Function<SysRoleReqVo, SysRole> getFunction() {
        return MapStructs.INSTANCE::reqVo2po;
    }
}