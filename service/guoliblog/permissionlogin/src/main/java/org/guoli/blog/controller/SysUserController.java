package org.guoli.blog.controller;

import com.baomidou.mybatisplus.extension.api.ApiController;
import org.guoli.blog.entity.SysUserEntity;
import org.guoli.blog.service.SysUserService;
import org.guoli.blog.utlis.R;
import org.guoli.blog.vo.SysUserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 登录接口
 */
@RestController
@RequestMapping("user")
public class SysUserController extends ApiController {

    @Autowired
    SysUserService userService;


    /**
     * 查询所有数据集
     * @return
     */
    @GetMapping("search")
    public R getList() {

        List<SysUserEntity> list =  userService.list();
        return  R.ok().data("info",list);
    }

    /**
     * 添加用户、用户自行注册。
     * @param userVo
     * @return
     */
    @PostMapping("register")
    public R register(@RequestBody(required = false) SysUserVo userVo) {
       try {
           System.out.println("registerVo = " + userVo);
           if(userService.register(userVo)){
               return  R.ok().message("添加新用户成功");
           }else {
               return R.error().message("添加新用户失败");
           }
       }catch (Exception e){
           return R.error().data("errror",e.getMessage());
       }
    }

    /**
     * 修改数据
     *
     * @param sysUserVo 实体对象
     * @return 修改结果
     */
    @PutMapping
    public R update(@RequestBody SysUserVo sysUserVo) {
        SysUserEntity po = this.userService.voToPo(sysUserVo);
        System.out.println("po = " + po);
        if(this.userService.updateById(po)){
            return R.ok().message("更新用户信息成功");
        }else {
            return R.error().message("更新用户信息失败");
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
        if(this.userService.removeByIds(idList)){
            return R.ok().message("删除用户成功");
        }else {
            return R.error().message("删除用户失败");
        }
    }
}
