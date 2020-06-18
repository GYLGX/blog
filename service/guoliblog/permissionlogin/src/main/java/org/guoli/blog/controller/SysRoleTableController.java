package org.guoli.blog.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.guoli.blog.entity.SysRoleTable;
import org.guoli.blog.service.SysRoleBackendApiTableService;
import org.guoli.blog.service.SysRoleFrontendMenuTableService;
import org.guoli.blog.service.SysRoleTableService;
import org.guoli.blog.service.SysRoleUserTableService;
import org.guoli.blog.utlis.R;
import org.guoli.blog.vo.SysRoleAndPermissionVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * (SysRoleTable)表控制层
 * 该类由EasyCode工具生成
 * @since 2020-03-07 14:31:50
 */
@RestController
@RequestMapping("sysRoleTable")
public class SysRoleTableController extends ApiController {
    /**
     * 角色
     */
    @Resource
    private SysRoleTableService sysRoleTableService;

    /**
     * 角色-用户
     */
    @Autowired
    SysRoleUserTableService sysRoleUserTableService;

    /**
     * 角色-前端菜单
     */
    @Autowired
    SysRoleFrontendMenuTableService sysRoleFrontendMenuTableService;

    /**
     * 角色-API
     */
    @Autowired
    SysRoleBackendApiTableService sysRoleBackendApiTableService;

    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @param sysRoleTable 查询实体
     * @return 所有数据
     */
    @GetMapping("{page}/{limit}")
    public R index(@PathVariable Long page, @PathVariable Long limit, SysRoleTable sysRoleTable) {
        Page<SysRoleTable> pageParam = new Page<>(page, limit);
        QueryWrapper<SysRoleTable> wrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(sysRoleTable.getRoleName())) {
            wrapper.like("role_name",sysRoleTable.getRoleName());
        }
        this.sysRoleTableService.page(pageParam, wrapper);
        return R.ok().data("items", pageParam.getRecords()).data("total", pageParam.getTotal());
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public R selectOne(@PathVariable Serializable id) {
        return R.ok().data("info",this.sysRoleTableService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param sysRoleTable 实体对象
     * @return 新增结果
     */
    @PostMapping
    public R insert(@RequestBody SysRoleTable sysRoleTable) {
        if(this.sysRoleTableService.save(sysRoleTable)){
            return R.ok().message("添加角色成功");
        }else {
            return R.error().message("添加角色失败");
        }
    }

    /**
     * 修改数据
     *
     * @param sysRoleTable 实体对象
     * @return 修改结果
     */
    @PutMapping
    public R update(@RequestBody SysRoleTable sysRoleTable) {
        if(this.sysRoleTableService.updateById(sysRoleTable)){
            return R.ok().message("修改角色信息成功");
        }else {
            return R.error().message("修改角色信息失败");
        }
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    public R delete(@RequestParam("idList") List<String> idList) {
        if(this.sysRoleTableService.removeByIds(idList)){
            return R.ok().message("删除角色成功");
        }else {
            return R.error().message("删除角色失败");
        }
    }

    //-----------------------------------------------------------------------
    /**
     * 对应前端：系统管理->用户角色设置->右边用户查询
     * @param roleId
     * @return
     */
    @GetMapping("/roleUser/{roleId}")
    public R getRoleAndUserList(@PathVariable("roleId") String roleId) {
        return R.ok().data("list",sysRoleTableService.getRoleAndUserList(roleId));
    }

    @PostMapping("/roleUser/set")
    public R saveRoleUser(@RequestParam("roleId") String roleId,
                          @RequestBody SysRoleAndPermissionVo[] sysRoleAndPermissionVos){
        if (sysRoleUserTableService.saveRoleUser(roleId, sysRoleAndPermissionVos)){
            return R.ok().message("设置授权成功");
        }else {
            return R.error().message("设置授权失败");
        }
    }

    //-----------------------------------------------------------------------
    /**
     * 对应前端：系统管理->菜单角色设置->右边菜单查询
     * @param roleId
     * @return
     */
    @GetMapping("/roleMenu/{roleId}")
    public R getRoleAndMenuList(@PathVariable("roleId") String roleId) {
        return R.ok().data("list",sysRoleTableService.getRoleAndMenuList(roleId));
    }


    @PostMapping("/roleMenu/set")
    public R saveRoleMenu(@RequestParam("roleId") String roleId,
                                @RequestBody SysRoleAndPermissionVo[] sysRoleAndPermissionVos){
        if (sysRoleFrontendMenuTableService.saveRoleMenu(roleId,sysRoleAndPermissionVos)){
            return R.ok().message("设置授权成功");
        }else {
            return R.error().message("设置授权失败");
        }
    }

    //-----------------------------------------------------------------------
    /**
     * 对应前端：系统管理->菜单角色设置->右边api查询
     * @param roleId
     * @return
     */
    @GetMapping("/roleApi/{roleId}")
    public R getRoleAndApiList(@PathVariable("roleId") String roleId) {
        return R.ok().data("list",sysRoleTableService.getRoleAndApiList(roleId));
    }


    @PostMapping("/roleApi/set")
    public R saveRoleApi(@RequestParam("roleId") String roleId,
                          @RequestBody SysRoleAndPermissionVo[] sysRoleAndPermissionVos){
        if (sysRoleBackendApiTableService.saveRoleAip(roleId,sysRoleAndPermissionVos)){
            return R.ok().message("设置授权成功");
        }else {
            return R.error().message("设置授权失败");
        }
    }



}