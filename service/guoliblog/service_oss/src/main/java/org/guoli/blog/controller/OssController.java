package org.guoli.blog.controller;

import org.guoli.blog.exceptionHandler.GuoliException;
import org.guoli.blog.service.OssService;
import org.guoli.blog.service.UcenbterService;
import org.guoli.blog.utlis.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/blogoss/fileoss")
@CrossOrigin
public class OssController {

    @Autowired
    private OssService ossService;

    @Autowired
    private UcenbterService ucebterService;

    //上传图片
    @PostMapping
    public R uploadOssFile(MultipartFile file){
        //获取上传文件
        String url = ossService.uploadFileAvatar(file);
        if(!StringUtils.isEmpty(url)){
            return R.ok().data("url",url).message("上传图片成功");
        }else {
            return R.error().message("上传图片失败");
        }
    }
    //上传头像
    @PostMapping("uploadavatar")
    public R uploadAvatar(MultipartFile file, @RequestHeader("token") String token){
        if(StringUtils.isEmpty(token)){
            throw new GuoliException(201,"请登录后在修改");
        }
        int uid = ucebterService.verificationToken(token);
        if(uid > 0){
            //获取上传文件
            String url = ossService.uploadFileAvatar(file);
            return R.ok().data("url",url).message("上传头像成功");
        }
        return R.error().message("上传头像失败");
    }
}
