package org.guoli.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.guoli.blog.entity.CarouselImg;
import org.guoli.blog.mapper.CarouselImgMapper;
import org.guoli.blog.service.CarouselImgService;
import org.springframework.stereotype.Service;

@Service
public class CarouselImgServiceImpl  extends ServiceImpl<CarouselImgMapper, CarouselImg> implements CarouselImgService {
}
