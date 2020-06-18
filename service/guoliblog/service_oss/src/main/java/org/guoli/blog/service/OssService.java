package org.guoli.blog.service;

import org.springframework.web.multipart.MultipartFile;

public interface OssService {
    //上传头像到service
    String uploadFileAvatar(MultipartFile file);
}
