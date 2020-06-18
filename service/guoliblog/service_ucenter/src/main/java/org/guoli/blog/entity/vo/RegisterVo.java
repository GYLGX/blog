package org.guoli.blog.entity.vo;

import lombok.Data;

@Data
public class RegisterVo {
    private String userName;
    private String mobile;
    private String password;
    private int sex;
    private String code;
}
