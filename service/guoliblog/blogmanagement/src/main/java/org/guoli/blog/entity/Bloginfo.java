package org.guoli.blog.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author testjava
 * @since 2020-04-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Bloginfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String title;

    private String content;

    @TableField("createTime")
    private Date createTime;

    @TableField("upateTime")
    private Date upateTime;

    @TableField("imgUrl")
    private String imgUrl;

    private Integer lid;

    private Boolean enabled;

    private int readingVolume;

    private int commentVolume;

}
