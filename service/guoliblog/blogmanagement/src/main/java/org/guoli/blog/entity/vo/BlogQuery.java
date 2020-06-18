package org.guoli.blog.entity.vo;

import lombok.Data;


@Data
public class BlogQuery {
    private  int lid;
    private String title;
    private String content;
    private String createTime;//注意，这里使用的是String类型，前端传过来的数据无需进行类型转换
}
