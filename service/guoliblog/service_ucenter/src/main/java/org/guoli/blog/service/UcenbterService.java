package org.guoli.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.guoli.blog.entity.Ucenbter;
import org.guoli.blog.entity.vo.RegisterVo;

import java.util.Map;

public interface UcenbterService extends IService<Ucenbter> {
    //验证token信息
    int verificationToken(String token);

    //登录的方法
    String login(Ucenbter ucenbter);

    //注册的方法
    void register(RegisterVo registerVo);

    Boolean SetPassword(Map<String, String> info);
}
