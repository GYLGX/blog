package org.guoli.blog.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.guoli.blog.entity.CarouselImg;
import org.guoli.blog.service.CarouselImgService;
import org.guoli.blog.utlis.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carouselImg")
public class CarouselImgController {

    @Autowired
    private CarouselImgService carouselImgService;

    @GetMapping
    private R getCarouselImgList(){
        return R.ok().data("list",carouselImgService.list(null));
    }
    @PostMapping
    private R addCarouselImg(@RequestBody CarouselImg carouselImg){
        QueryWrapper<CarouselImg> wrapper = new QueryWrapper<>();
        wrapper.eq("blogid",carouselImg.getBlogid());
        List<CarouselImg> list = carouselImgService.list(wrapper);
        if(list.size() > 0) return R.error().message("走马灯列表已经有该博客");
        int size = carouselImgService.list(null).size();
        if(size > 5) return R.error().message("走马灯列表已经满了");
        boolean save = carouselImgService.save(carouselImg);
        if (save){
            return R.ok().message("添加到走马灯列表成功");
        }else {
        return R.error().message("添加到走马灯列表失败");
        }
    }
    @DeleteMapping("{id}")
    private R deleteCarouselImg(@PathVariable int id ){
        boolean b = carouselImgService.removeById(id);
        if(b){
            return R.ok().message("删除走马灯成功");
        }else {
            return R.error().message("删除走马灯失败");
        }
    }
}
