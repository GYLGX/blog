package org.guoli.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

@Data
public class Classificati  implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String groupame;
}
