package org.guoli.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.jsonwebtoken.Claims;
import org.guoli.blog.entity.Comment;
import org.guoli.blog.entity.Ucenbter;
import org.guoli.blog.exceptionHandler.GuoliException;
import org.guoli.blog.mapper.CommentMapper;
import org.guoli.blog.service.CommentService;
import org.guoli.blog.service.UcenbterService;
import org.guoli.blog.utlis.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {


    @Autowired
    private UcenbterService ucebterService;


    @Override
    public int verificationToken(String token) {
        Claims info = JwtUtils.getMemberIdByToken(token);
        Object id = info.get("id");
        int uid = (int)id;
        Ucenbter ucenbterInfo = ucebterService.getById(uid);
        if(!ucenbterInfo.getState()){
            throw new GuoliException(201,"该用户已被禁用！");
        }
        int userid = ucenbterInfo.getId();
        return userid;
    }
}
