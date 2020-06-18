package org.guoli.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.guoli.blog.entity.SysFrontendMenuTable;
import org.guoli.blog.vo.SysFrontendVo;

import java.util.List;

/**
 * (SysFrontendMenuTable)表服务接口
 * 该类由EasyCode工具生成
 * @since 2020-03-07 13:42:36
 */
public interface SysFrontendMenuTableService extends IService<SysFrontendMenuTable> {

    /**
     * 查所有的前端菜单（编辑使用）
     */
    List<SysFrontendVo> getAllMenuList();

    /**
     * 根据登录账号，获得前端展现的菜单
     * 控制前端菜单的权限
     * @param username
     * @return
     */
    List<SysFrontendMenuTable> getMenusByUserName(String username);
}