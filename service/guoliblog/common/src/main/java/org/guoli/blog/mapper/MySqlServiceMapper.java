package org.guoli.blog.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

public interface MySqlServiceMapper {
    //根据用户名查询用户状态
    @Select("SELECT id,title,createTime FROM bloginfo WHERE lid = #{lid} order by id desc;")
    List<Map<String , String>> findByLid(@Param("lid") Integer lid);


    //根据blogid获取评论信息
    @Select("SELECT c.id,blogid,content,uid,comment_time,user_name,url,sex\n" +
            "FROM comment c,ucenbter u \n" +
            "WHERE c.uid = u.id\n" +
            "AND c.puid = 0 \n" +
            "AND c.blogid = #{blogid}\n" +
            "order by comment_time desc;")
    List<Map<String,String>> getBybloidInfo(@Param("blogid") int blogid);

    //根据blogid和puid获取评论的回复信息
    @Select("SELECT c.id,blogid,head,content,uid,pid,puid,comment_time,user_name,url,sex\n" +
            "FROM comment c,ucenbter u \n" +
            "WHERE c.uid = u.id\n" +
            "AND c.pid = #{pid}\n" +
            "AND c.puid = #{puid}\n" +
            "AND c.blogid = #{blogid}\n" +
            "order by comment_time desc;")
    List<Map<String,String>> getBybloidpuidInfo(@Param("blogid") int blogid,@Param("pid") int pid,@Param("puid") int puid);

    //查询阅读量
    @Select("SELECT reading_volume FROM bloginfo WHERE id = #{id}")
    int getReadingvolume(@Param("id") int id);

    //添加阅读量
    @Update("UPDATE bloginfo SET reading_volume = #{readingvolume} WHERE id = #{id}")
    Boolean setReadingVolume(@Param("readingvolume") int readingvolume,@Param("id") int id);


    //查询评论量
    @Select("SELECT COUNT(*) FROM comment WHERE blogid =  #{blogid}")
    int getByIdCommentVolume(@Param("blogid") int blogid);

    //跟新评论量
    @Update("UPDATE bloginfo SET comment_volume = #{commentvolume} WHERE id = #{id}")
    Boolean setCommentVolume(@Param("commentvolume") int commentvolume,@Param("id") int id);

    //获取用户总数
    @Select("SELECT COUNT(*) FROM ucenbter")
    int getUserVolume();

    //获取评论总数
    @Select("SELECT COUNT(*) FROM comment")
    int getCommentVolume();

    //获取阅读数据
    @Select("SELECT reading_volume FROM bloginfo WHERE reading_volume > 0")
    List<String> getReadingVolume();

}
