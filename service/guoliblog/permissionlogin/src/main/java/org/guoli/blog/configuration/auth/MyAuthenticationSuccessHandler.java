package org.guoli.blog.configuration.auth;

import org.guoli.blog.components.TokenCache;
import org.guoli.blog.entity.SysFrontendMenuTable;
import org.guoli.blog.service.SysFrontendMenuTableService;
import org.guoli.blog.service.SysUserService;
import org.guoli.blog.utlis.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 登录成功操作
 */
@Component
public class MyAuthenticationSuccessHandler extends JSONAuthentication implements AuthenticationSuccessHandler {

    @Autowired
    SysFrontendMenuTableService service;
    @Autowired
    SysUserService sysUserService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        //取得账号信息
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //
        System.out.println("userDetails = " + userDetails);
        //取token
        //好的解决方案，登录成功后token存储到数据库中
        //只要token还在过期内，不需要每次重新生成
        //先去缓存中找
//        String token = TokenCache.getTokenFromCache(userDetails.getUsername());
//        if(token ==null) {
//            System.out.println("初次登录，token还没有，生成新token。。。。。。");
//            //如果token为空，则去创建一个新的token
//            //jwtTokenUtil = new JwtTokenUtil();
//            token = jwtTokenUtil.generateToken(userDetails);
//            //把新的token存储到缓存中
//            TokenCache.setToken(userDetails.getUsername(),token);
//        }
        String token = jwtTokenUtil.generateToken(userDetails);

        //加载前端菜单
        List<SysFrontendMenuTable> menus = service.getMenusByUserName(userDetails.getUsername());
        //
        Map<String, Object> map = new HashMap<>();
        map.put("username",userDetails.getUsername());
        map.put("auth",userDetails.getAuthorities());
        map.put("menus",menus);
        map.put("token",token);
        //装入token
        R data = R.ok().data(map);
        //输出
        this.WriteJSON(request, response, data);

    }
}
