package org.guoli.blog.controller;



import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.api.ApiController;
import org.guoli.blog.entity.SysBackendApiTable;
import org.guoli.blog.service.SysBackendApiTableService;
import org.guoli.blog.utlis.R;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * (SysBackendApiTable)表控制层
 * @author 小明哥
 * @since 2020-03-07 13:46:35
 */
@RestController
@RequestMapping("sysBackendApiTable")
public class SysBackendApiTableController extends ApiController {
    /**
     * 服务对象
     */
    @Resource
    private SysBackendApiTableService sysBackendApiTableService;

    /**
     * 分页查询所有数据
     * @return 所有数据
     */
    @GetMapping
    public R selectAll() {
        return R.ok().data("list",this.sysBackendApiTableService.getAllApiList());
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public R selectOne(@PathVariable Serializable id) {
        return R.ok().data("lists",this.sysBackendApiTableService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param sysBackendApiTable 实体对象
     * @return 新增结果
     */
    @PostMapping
    public R insert(@RequestBody SysBackendApiTable sysBackendApiTable) {
        //如果没有父级，设置为url=none
        if (StringUtils.isEmpty(sysBackendApiTable.getPid())) {
            sysBackendApiTable.setBackendApiUrl("none");
        }
        if(this.sysBackendApiTableService.save(sysBackendApiTable)){
            return R.ok().message("添加新的API成功");
        }else {
            return R.error().message("添加新的API失败");
        }
    }

    /**
     * 修改数据
     *
     * @param sysBackendApiTable 实体对象
     * @return 修改结果
     */
    @PutMapping
    public R update(@RequestBody SysBackendApiTable sysBackendApiTable) {
        if(this.sysBackendApiTableService.updateById(sysBackendApiTable)){
            return R.ok().message("修改菜API息成功");
        }else {
            return R.error().message("修改API信息失败");
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
        if(this.sysBackendApiTableService.removeByIds(idList)){
            return R.ok().message("删除API成功");
        }else {
            return R.error().message("删除API失败");
        }

    }
}