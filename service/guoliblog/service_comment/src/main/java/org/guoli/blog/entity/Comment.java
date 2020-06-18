package org.guoli.blog.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Comment implements Serializable {
    private int id;
    private int pid;
    private int blogid;
    private String head;
    private String content;
    private int uid;
    public  int puid;
    private String commentTime;
}
