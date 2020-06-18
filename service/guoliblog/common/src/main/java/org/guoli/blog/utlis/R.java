package org.guoli.blog.utlis;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
//统一返回结果类
public class R {
    private Boolean success;
    private Integer status;
    private String message;
    private Map<String, Object> data = new HashMap<String, Object>();
    private R(){}
    public static R ok(){
        R r = new R();
        r.setSuccess(true);
        r.setStatus(ResultCode.SUCCESS);
        r.setMessage("成功");
        return r;
    }
    public static R error(){
        R r = new R();
        r.setSuccess(false);
        r.setStatus(ResultCode.ERROR);
        r.setMessage("失败");
        return r;
    }
    public R success(Boolean success){
        this.setSuccess(success);
        return this;
    }
    public R message(String message){
        this.setMessage(message);
        return this;
    }
    public R status(Integer status){
        this.setStatus(status);
        return this;
    }
    public R data(String key, Object value){
        this.data.put(key, value);
        return this;
    }
    public R data(Map<String, Object> map){
        this.setData(map);
        return this;
    }
}
