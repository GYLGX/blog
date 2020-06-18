package org.guoli.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.guoli.blog.entity.Classificati;
import org.guoli.blog.mapper.ClassificatiMapper;
import org.guoli.blog.service.ClassificatiService;
import org.springframework.stereotype.Service;

@Service
public class ClassificatiServiceImpl extends ServiceImpl<ClassificatiMapper, Classificati> implements ClassificatiService {
}
