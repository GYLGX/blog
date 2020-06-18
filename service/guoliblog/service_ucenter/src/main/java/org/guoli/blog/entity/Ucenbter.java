package org.guoli.blog.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Ucenbter implements Serializable {
    private int id;
    private String mobile;
    private String  userName;
    private String  password;
    private String  registerTime;
    private String  url;
    private  int sex;
    private Boolean state;
    private String warningContent;
}
