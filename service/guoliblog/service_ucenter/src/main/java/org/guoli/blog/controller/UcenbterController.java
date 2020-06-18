package org.guoli.blog.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.jsonwebtoken.Claims;
import org.guoli.blog.entity.Ucenbter;
import org.guoli.blog.entity.vo.ModifyPersonalInformation;
import org.guoli.blog.entity.vo.RegisterVo;
import org.guoli.blog.exceptionHandler.GuoliException;
import org.guoli.blog.service.UcenbterService;
import org.guoli.blog.utlis.JwtUtils;
import org.guoli.blog.utlis.MD5;
import org.guoli.blog.utlis.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ucenter")
public class UcenbterController {

    @Autowired
    private UcenbterService ucebterService;

    //登录
    @PostMapping("/openup/login")
    public R loginUser(@RequestBody Ucenbter ucenbter) {
        //member对象封装手机号和密码
        //调用service方法实现登录
        //返回token值，使用jwt生成
        String token = ucebterService.login(ucenbter);
        Claims memberIdByToken = JwtUtils.getMemberIdByToken(token);
        Object id = memberIdByToken.get("id");
        int uid = (int)id;
        Ucenbter ucenbterInfo = ucebterService.getById(uid);
        ucenbterInfo.setPassword("");
        return R.ok().data("ucenbterInfo",ucenbterInfo).data("token",token).message("登录成功");
    }

    //注册
    @PostMapping("/openup/register")
    public R registerUser(@RequestBody RegisterVo registerVo) {
        ucebterService.register(registerVo);
        return R.ok().message("新用户注册成功");
    }

    //修改信息
    @PutMapping("/openup/updateinfo")
    public R updateInfo(@RequestBody ModifyPersonalInformation modifyPersonalInformation, @RequestHeader("token") String token){
        if(StringUtils.isEmpty(token)){
            throw new GuoliException(201,"请登录后在修改");
        }
        int uid = ucebterService.verificationToken(token);
        int id = modifyPersonalInformation.getId();
        if(uid != id){
            throw new GuoliException(201,"不允许修改他人信息");
        }
        Ucenbter ucenbter = new Ucenbter();
        ucenbter.setId(modifyPersonalInformation.getId());
        ucenbter.setUrl(modifyPersonalInformation.getUrl());
        ucenbter.setUserName(modifyPersonalInformation.getUserName());
        ucenbter.setSex(modifyPersonalInformation.getSex());
        boolean update = ucebterService.updateById(ucenbter);
        if (update){
            return R.ok().message("修改个人信息成功");
        }else {
            return R.error().message("修改个人信息失败");
        }
    }

    //修改密码
    @PutMapping("/openup/updatepassword")
    public R updatepassword(@RequestBody Map<String,String> password, @RequestHeader("token") String token){
        if(StringUtils.isEmpty(token)){
            throw new GuoliException(201,"请登录后在修改");
        }
        int uid = ucebterService.verificationToken(token);
        Ucenbter userInfo = ucebterService.getById(uid);
        String oldpassword = password.get("oldpassword");
        String newpassword = password.get("newpassword");
        if(!MD5.encrypt(oldpassword).equals(userInfo.getPassword())){
            throw new GuoliException(201,"密码错误");
        }
        userInfo.setPassword(MD5.encrypt(newpassword));
        boolean update = ucebterService.updateById(userInfo);
        if (update){
            return R.ok().message("修改密码成功");
        }else {
            return R.error().message("修改密码失败");
        }
    }

    //忘记密码
    @PutMapping("/openup/forgetpassword")
    private R ForgetPassword(@RequestBody Map<String,String> info){
        Boolean aBoolean = ucebterService.SetPassword(info);
        if (aBoolean){
            return R.ok().message("重置密码成功");
        }else {
            return R.error().message("重置密码失败");
        }
    }

    //分页查询获取用户列表
    @PostMapping("/page/{current}/{size}")
    private R getUcenterList(
            @PathVariable long current,
            @PathVariable long size,
            @RequestBody Map<String,String> likeInfo){
        String userName = likeInfo.get("userName");
        Page<Ucenbter> page = new Page<>(current, size);
        QueryWrapper<Ucenbter> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(userName)){
            wrapper.like("user_name",userName);
        }
        wrapper.orderBy(true,false,"register_time");
        ucebterService.page(page,wrapper);
        long total = page.getTotal();
        List<Ucenbter> records = page.getRecords();
        return R.ok().data("total",total).data("rows",records);
    }


    @PostMapping
    private R updateInfo(@RequestBody Ucenbter ucenbter){
        boolean update = ucebterService.updateById(ucenbter);
        if (update) {
            return R.ok().message("操作成功");
        }else {
            return R.error().message("操作失败");
        }
    }

    @GetMapping("/byuid/{uid}")
    private R getByUid(@PathVariable int uid){
        Ucenbter byId = ucebterService.getById(uid);
        return R.ok().data("userInfo",byId);
    }

    @DeleteMapping("/{id}")
    private R removeUcebter(@PathVariable int id){
        boolean flag = ucebterService.removeById(id);
        if (flag) {
            return R.ok().message("删除成功");
        }else {
            return R.error().message("删除失败");
        }
    }
}
