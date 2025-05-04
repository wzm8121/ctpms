package com.mzw.ctpmsbackend.service.impl;

import cn.dev33.satoken.stp.StpInterface;

import com.mzw.ctpmsbackend.entity.User;
import com.mzw.ctpmsbackend.mapper.UserMapper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 自定义权限加载接口实现类
 */
@Component    // 保证此类被 SpringBoot 扫描，完成 Sa-Token 的自定义权限验证扩展
public class StpInterfaceImpl implements StpInterface {

    /**
     * 初始化方法，项目启动时会执行一次
     */
    @PostConstruct
    public void init() {
        System.out.println("✅ StpInterfaceImpl 已初始化，Spring 成功加载！");
    }
    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        // 本 list 仅做模拟，实际项目中要根据具体业务逻辑来查询权限
        List<String> list = new ArrayList<String>();
//        list.add("101");
//        list.add("user.add");
//        list.add("user.update");
//        list.add("user.get");
        // list.add("user.delete");
        list.add("art.*");
        return list;
    }

    @Resource
    private UserMapper userMapper;

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        // 将 loginId 转为字符串再解析为 Integer（根据 userId 的实际类型决定）
        Integer id = Integer.valueOf(loginId.toString());

        User user = userMapper.selectById(id);
        if (user == null) {
            return Collections.emptyList();
        }

        List<String> list = new ArrayList<>();
        if (user.getRole() != null) {
            list.add(user.getRole());  // 比如 user 或 admin
        }
        return list;
    }



}


