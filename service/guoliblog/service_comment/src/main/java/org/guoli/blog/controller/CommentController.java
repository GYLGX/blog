package org.guoli.blog.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.guoli.blog.entity.Comment;
import org.guoli.blog.exceptionHandler.GuoliException;
import org.guoli.blog.mapper.MySqlServiceMapper;
import org.guoli.blog.service.CommentService;
import org.guoli.blog.utlis.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;
    @Resource
    MySqlServiceMapper mySqlServiceMapper;


    //根据blogid获取评论信息
    @GetMapping("/openup/{blogid}")
    private R getByBlogid(@PathVariable int blogid){
        List<Map<String, String>> bybloidInfo = mySqlServiceMapper.getBybloidInfo(blogid);
        return R.ok().data("bybloidInfo",bybloidInfo);
    }

    //根据blogid 和 puid 获取回复信息
    @GetMapping("/openup/{blogid}/{pid}/{puid}")
    private R getBybloidpuid(@PathVariable("blogid") int blogid,
                             @PathVariable("pid")  int pid,
                             @PathVariable("puid")  int puid){
        List<Map<String, String>> bybloidpuidInfo = mySqlServiceMapper.getBybloidpuidInfo(blogid, pid,puid);
        return R.ok().data("bybloidpuidInfo",bybloidpuidInfo);
    }

    //添加评论
    @PostMapping("/openup")
    private R addComment(@RequestBody Comment comment, @RequestHeader("token") String token){
        if(StringUtils.isEmpty(token)){
            throw new GuoliException(201,"请登录后在评论"+token);
        }
        int uid = commentService.verificationToken(token);
        if(comment.getUid() != uid){
            throw new GuoliException(201,"评论失败,用户信息错误！");
        }
        if(StringUtils.isEmpty(comment.getContent())){
            throw new GuoliException(201,"评论内容不能为空");
        }
        int commentVolume = mySqlServiceMapper.getByIdCommentVolume(comment.getBlogid());
        mySqlServiceMapper.setCommentVolume(commentVolume + 1, comment.getBlogid());
        boolean save = commentService.save(comment);
        if (save){
            return R.ok().message("评论成功");
        }else {
            return R.error().message("评论失败");
        }
    }
    //个人撤回自己的评论
    @DeleteMapping("/openup/{id}")
    private R deleteComment(@PathVariable int id, @RequestHeader("token") String token){
        if(StringUtils.isEmpty(token)){
            throw new GuoliException(201,"请登录后在撤回评论");
        }
        int uid = commentService.verificationToken(token);
        Comment comment = commentService.getById(id);
        if(comment.getUid() != uid){
            throw new GuoliException(201,"不允许对他人的评论进行该项操作");
        }
        int commentVolume = mySqlServiceMapper.getByIdCommentVolume(comment.getBlogid());
        mySqlServiceMapper.setCommentVolume(commentVolume - 1, comment.getBlogid());
        boolean remove = commentService.removeById(id);
        if (remove){
            return R.ok().message("撤回评论成功");
        }else {
            return R.error().message("撤回评论失败");
        }
    }

    //分页查询评论
    //分页查询获取用户列表
    @PostMapping("/page/{current}/{size}/{uid}")
    private R getUcenterList(
            @PathVariable long current,
            @PathVariable long size,
            @PathVariable long uid){
        Page<Comment> page = new Page<>(current, size);
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        if (uid > 0){
            wrapper.eq("uid",uid);
        }
        wrapper.orderBy(true,false,"comment_time");
        commentService.page(page,wrapper);
        long total = page.getTotal();
        List<Comment> records = page.getRecords();
        return R.ok().data("total",total).data("rows",records);
    }

    //批量删除评论
    @DeleteMapping("/batchdelete")
    private R batchdelete(@RequestBody List<String> ids){
        boolean remove = commentService.removeByIds(ids);
        if (remove){
            return R.ok().message("删除评论成功");
        }else {
            return R.error().message("删除评论失败");
        }
    }


}
