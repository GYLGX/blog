package org.guoli.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.jsonwebtoken.Claims;
import org.guoli.blog.entity.Ucenbter;
import org.guoli.blog.entity.vo.RegisterVo;
import org.guoli.blog.exceptionHandler.GuoliException;
import org.guoli.blog.mapper.UcenbterMapper;
import org.guoli.blog.service.UcenbterService;
import org.guoli.blog.utlis.JwtUtils;
import org.guoli.blog.utlis.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;

@Service
public class UcenbterServiceImpl extends ServiceImpl<UcenbterMapper, Ucenbter> implements UcenbterService {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Autowired
    private UcenbterService ucebterService;

    @Override
    public String login(Ucenbter ucenbter) {
        String mobile = ucenbter.getMobile();
        String password = ucenbter.getPassword();
        if(StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)){
            throw new GuoliException(201,"登录用户或密码错误");
        }

        //判断手机号是否正确
        QueryWrapper<Ucenbter> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile",mobile);
        Ucenbter ucenbterInfo = baseMapper.selectOne(wrapper);
        //判断查询对象是否为空
        if(ucenbterInfo == null) {//没有这个手机号
            throw new GuoliException(201,"该手机号未注册");
        }
        //判断密码
        //因为存储到数据库密码肯定加密的
        //把输入的密码进行加密，再和数据库密码进行比较
        //加密方式 MD5
        if(!MD5.encrypt(password).equals(ucenbterInfo.getPassword())) {
            throw new GuoliException(201,"密码错误");
        }
        //判断用户是否可用
        if(!ucenbterInfo.getState()) {
            throw new GuoliException(201,"该用户已被禁用");
        }
        //登录成功
        //生成token字符串，使用jwt工具类
        String jwtToken = JwtUtils.getJwtToken(ucenbterInfo.getId(), ucenbterInfo.getUserName());
        return jwtToken;
    }

    @Override
    public void register(RegisterVo registerVo) {
        //获取注册的数据
        String code = registerVo.getCode(); //验证码
        String mobile = registerVo.getMobile(); //手机号
        String userName = registerVo.getUserName(); //昵称
        String password = registerVo.getPassword(); //密码
        int sex = registerVo.getSex(); //性别

        //非空判断
        if(StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)
                || StringUtils.isEmpty(code) || StringUtils.isEmpty(userName)) {
            throw new GuoliException(201,"手机号或密码为空");
        }
        //判断验证码
        //获取redis验证码
        String redisCode = redisTemplate.opsForValue().get(mobile);
        if(!code.equals(redisCode)) {
            throw new GuoliException(201,"验证码错误");
        }

        //判断手机号是否重复，表里面存在相同手机号不进行添加
        QueryWrapper<Ucenbter> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile",mobile);
        Integer count = baseMapper.selectCount(wrapper);
        if(count > 0) {
            throw new GuoliException(201,"该用户号码已被注册");
        }

        //数据添加数据库中
        Ucenbter ucenbter = new Ucenbter();
        ucenbter.setMobile(mobile);
        ucenbter.setUserName(userName);
        ucenbter.setPassword(MD5.encrypt(password));//密码需要加密的
        ucenbter.setSex(sex);
        ucenbter.setState(true);//用户可用
        if(sex == 1){
            ucenbter.setUrl("https://guoli368.oss-cn-shenzhen.aliyuncs.com/blog/imgs/male.png");
        }else {
            ucenbter.setUrl("https://guoli368.oss-cn-shenzhen.aliyuncs.com/blog/imgs/female.png");
        }
        baseMapper.insert(ucenbter);
    }


    //验证token
    @Override
    public int verificationToken(String token) {
        Claims info = JwtUtils.getMemberIdByToken(token);
        Object id = info.get("id");
        int uid = (int)id;
        Ucenbter ucenbterInfo = ucebterService.getById(uid);
        if(!ucenbterInfo.getState()){
            throw new GuoliException(201,"该用户已被禁用，无法修改用户信息！");
        }
        int userid = ucenbterInfo.getId();
        return userid;
    }



    @Override
    public Boolean SetPassword(Map<String, String> info) {
        String mobile = info.get("mobile");
        String code = info.get("code");
        String password = info.get("password");
        //判断手机号是否正确
        QueryWrapper<Ucenbter> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile",mobile);
        Ucenbter ucenbter = baseMapper.selectOne(wrapper);
        //判断查询对象是否为空
        if(ucenbter == null) {//没有这个手机号
            throw new GuoliException(201,"该手机号未注册");
        }
        //判断验证码
        //获取redis验证码
        String redisCode = redisTemplate.opsForValue().get(mobile);
        if(!code.equals(redisCode)) {
            throw new GuoliException(201,"验证码错误");
        }
        ucenbter.setPassword(MD5.encrypt(password));
        boolean update = ucebterService.updateById(ucenbter);
        return update;
    }




}
