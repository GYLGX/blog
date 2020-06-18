package org.guoli.blog.controller;

import org.guoli.blog.mapper.MySqlServiceMapper;
import org.guoli.blog.utlis.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/totalnumber")
public class TotalNumber {


    @Resource
    MySqlServiceMapper mySqlServiceMapper;

    //获取总数据信息
    @GetMapping()
    private R gettotalnumber(){
        int commentVolume = mySqlServiceMapper.getCommentVolume();
        int userVolume = mySqlServiceMapper.getUserVolume();
        List<String> list = mySqlServiceMapper.getReadingVolume();

        return R.ok().data("userVolume",userVolume).data("commentVolume",commentVolume).data("ReadingList",list);
    }
}
