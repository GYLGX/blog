package org.guoli.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.guoli.blog.entity.Comment;

public interface CommentService extends IService<Comment> {
    int verificationToken(String token);
}
