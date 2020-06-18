package org.guoli.blog.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.guoli.blog.service.OssService;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
public class OssServiceImpl implements OssService {

    //上传文件到oss
    @Override
    public String uploadFileAvatar(MultipartFile file) {

        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = "oss-cn-shenzhen.aliyuncs.com";
// 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建。
        String accessKeyId = "accessKeyId";
        String accessKeySecret = "accessKeySecret";
        String bucketName = "bucketName";

        try {

            // 创建OSSClient实例。
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

            //生成随机值
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            //获取日期
            String datePath = new DateTime().toString("yyyy/MM/dd");

            //获取上传文件输入流
            InputStream inputStream = file.getInputStream();

            // 获取文件名称
            String filename = file.getOriginalFilename();


            filename ="blog/"+ datePath+"/" + uuid + filename;

            //调用oss方法实现上传
            //第一个参数 bucketName
            //第二个参数 上传到oss文件路径和文件名称
            //第三个参数 上传文件输入流
            ossClient.putObject(bucketName, filename, inputStream);

            // 关闭OSSClient。
            ossClient.shutdown();

            //把上传之后文件路径返回
            //需要把上传到oss路径手动拼接出来
            String url = "https://"+bucketName+"."+endpoint+"/"+filename;
            return url;
        }catch (Exception e){

        }


        return null;
    }
}
