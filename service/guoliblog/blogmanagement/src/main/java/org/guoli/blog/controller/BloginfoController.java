package org.guoli.blog.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.guoli.blog.entity.Bloginfo;
import org.guoli.blog.entity.vo.BlogQuery;
import org.guoli.blog.mapper.MySqlServiceMapper;
import org.guoli.blog.service.BloginfoService;
import org.guoli.blog.utlis.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-04-06
 */

@RestController
@RequestMapping("/bloginfo")   //http://localhost:8010/service/bloginfo/findAll
public class BloginfoController {
    //把service注入
    @Autowired
    private BloginfoService bloginfoService;

    @Resource
     MySqlServiceMapper mySqlServiceMapper;

    @GetMapping
    private R getBlogData(){
        int totalSize = bloginfoService.list(null).size();
        QueryWrapper<Bloginfo> releaseWrapper = new QueryWrapper<>();
        releaseWrapper.eq("enabled",true);
        int releaseSize = bloginfoService.list(releaseWrapper).size();
        QueryWrapper<Bloginfo> unpublishedWrapper = new QueryWrapper<>();
        unpublishedWrapper.eq("enabled",false);
        int unpublishedSize = bloginfoService.list(unpublishedWrapper).size();
        HashMap<String, Integer> hashMap = new HashMap<>();
        hashMap.put("totalSize",totalSize);
        hashMap.put("releaseSize",releaseSize);
        hashMap.put("unpublishedSize",unpublishedSize);
        return R.ok().data("BlogData",hashMap);
    }

    //根据lid查询所有博客
    @GetMapping("/getLabedBlog/{lid}")
    public R findAllBlog(@PathVariable int lid){
        return R.ok().data("blog",mySqlServiceMapper.findByLid(lid));
    }

    //根据id删除数据
    @DeleteMapping("/delete/{id}")
    public R removeBlog(@PathVariable int id){
        boolean flag = bloginfoService.removeById(id);
        if (flag){
            return R.ok().message("删除该博客成功");
        }else {
            return R.error().message("删除该博客失败");
        }
    }

    //分页查询已经发布的博客
    @PostMapping("/pageBlog/releaser/{current}/{size}")
    public R pageListBlog(
            @PathVariable long current,
            @PathVariable long size,
            @RequestBody BlogQuery blogQuery){
        int lid = blogQuery.getLid();
        String content = blogQuery.getContent();
        String createTime = blogQuery.getCreateTime();
        //创建page对象
        Page<Bloginfo> pageBlog = new Page<>(current, size);
        //调用方法实现分页
        //调用方法时候，底层封装，把分页说有数据封装到pageBlog对象里面
        QueryWrapper<Bloginfo> wrapper = new QueryWrapper<>();
        wrapper.eq("enabled", true);
        if (!StringUtils.isEmpty(content)){
            wrapper.like("content",content);
        }
        if (lid > 0){
            wrapper.eq("lid",lid);
        }
        if (!StringUtils.isEmpty(createTime)){
            wrapper.like("createTime",createTime);
        }
        wrapper.orderBy(true,false,"createTime");
        bloginfoService.page(pageBlog,wrapper);
        long total = pageBlog.getTotal(); //获取总条数
        List<Bloginfo> records = pageBlog.getRecords();//数据list集合
        return R.ok().data("total",total).data("rows",records);
    }


    //分页条件查询博客
    @PostMapping("/pageBlogCondition/{current}/{size}")
    public R pageBlogCondition(
            @PathVariable long current,
            @PathVariable long size,
            @RequestBody(required = false) BlogQuery blogQuery){
        //创建page对象
        Page<Bloginfo> pageBlog = new Page<>(current, size);
        //构建条件
        QueryWrapper<Bloginfo> wrapper = new QueryWrapper<>();
        //wrapper
        int lid = blogQuery.getLid();
        String title = blogQuery.getTitle();
        String createTime = blogQuery.getCreateTime();
        if (lid > 0){
            wrapper.eq("lid",lid);
        }
        if (!StringUtils.isEmpty(title)){
            wrapper.like("title",title);
        }
        if (!StringUtils.isEmpty(createTime)){
            wrapper.like("createTime",createTime);
        }
        wrapper.orderBy(true,false,"createTime");
        //调用方法实现条件查询分页
        bloginfoService.page(pageBlog,wrapper);
        long total = pageBlog.getTotal(); //获取总条数
        List<Bloginfo> records = pageBlog.getRecords();//数据list集合
        return R.ok().data("total",total).data("rows",records);
    }


    //根据lid查询博客列表
    @GetMapping("/pageBlogByLid/{current}/{size}/{lid}")
    public R pageBlogByLid(
            @PathVariable long current,
            @PathVariable long size,
            @PathVariable long lid){
        //创建page对象
        Page<Bloginfo> pageBlog = new Page<>(current, size);
        //构建条件
        QueryWrapper<Bloginfo> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(lid)){
            wrapper.eq("lid",lid);
        }
        wrapper.orderBy(true,false,"createTime");
        bloginfoService.page(pageBlog,wrapper);
        long total = pageBlog.getTotal(); //获取总条数
        List<Bloginfo> records = pageBlog.getRecords();//数据list集合
        return R.ok().data("total",total).data("rows",records);
    }


    //添加新博客
    @PostMapping("/addBlog")
    public R addBlog(@RequestBody Bloginfo bloginfo){
        boolean save = bloginfoService.save(bloginfo);
        if (save) {
            return R.ok().message("您已经发布了一条新的博客");
        }else {
            return R.error().message("添加新博客失败");
        }
    }
    //根据id查询博客
    @GetMapping("/getReleaseBlog/{id}")
    public R getReleaseBlog(
            @PathVariable int id){
        Bloginfo blog = bloginfoService.getById(id);
        int readingvolume = mySqlServiceMapper.getReadingvolume(id);
        mySqlServiceMapper.setReadingVolume(readingvolume+1,id);
        int commentVolume = mySqlServiceMapper.getByIdCommentVolume(id);
        mySqlServiceMapper.setCommentVolume(commentVolume , id);
        Boolean enabled = blog.getEnabled();
        if(enabled){
            return R.ok().data("blog",blog);
        }else {
            return R.error().message("该博客已下架");
        }
    }
    //根据id查询博客
    @GetMapping("/getBlog/{id}")
    public R getBlog(
            @PathVariable int id){
        Bloginfo blog = bloginfoService.getById(id);
            return R.ok().data("blog",blog);
    }

    //修改博客
    @PutMapping("/updateBlog")
    public R updateBlog(@RequestBody Bloginfo bloginfo){
        bloginfo.setUpateTime(new Date());
        boolean flag = bloginfoService.updateById(bloginfo);
        if (flag) {
            return R.ok().message("修改博客成功");
        }else {
            return R.error().message("修改博客失败");
        }
    }

    //修改博客状态
    @PutMapping("/updateBlogState")
    public R updateBlogState(@RequestBody Bloginfo bloginfo){
        boolean flag = bloginfoService.updateById(bloginfo);
        if (flag) {
            if(bloginfo.getEnabled()){
                return R.ok().message("博客重新上架成功");
            }else {
                return R.ok().message("博客已经下架成功");
            }
        }else {
            return R.error().message("修改博客状态失败");
        }
    }
}

