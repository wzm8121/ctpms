package com.mzw.ctpmsbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mzw.ctpmsbackend.common.utils.DataResult;
import com.mzw.ctpmsbackend.dto.BlacklistDTO;
import com.mzw.ctpmsbackend.entity.Blacklist;
import com.mzw.ctpmsbackend.entity.User;
import com.mzw.ctpmsbackend.exception.ServiceException;
import com.mzw.ctpmsbackend.mapper.BlacklistMapper;
import com.mzw.ctpmsbackend.mapper.UserMapper;
import com.mzw.ctpmsbackend.service.BlacklistService;
import com.mzw.ctpmsbackend.service.UserService;
import org.nd4j.common.io.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * 黑名单服务实现类
 * 提供黑名单的增删改查及搜索功能的具体实现
 */
@Service
public class BlacklistServiceImpl implements BlacklistService {

    @Autowired
    private BlacklistMapper blacklistMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addToBlacklist(BlacklistDTO blacklistDTO) throws ServiceException {
        // 参数校验
        if (blacklistDTO == null) {
            throw new ServiceException("黑名单信息不能为空");
        }

        try {
            Blacklist blacklist = new Blacklist();
            BeanUtils.copyProperties(blacklistDTO, blacklist);
            blacklist.setCreatedAt(LocalDateTime.now());
            blacklistMapper.insert(blacklist);
        } catch (Exception e) {
            throw new ServiceException("添加黑名单失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeFromBlacklist(Integer recordId) throws ServiceException {
        // 检查黑名单记录是否存在
        Blacklist blacklist = blacklistMapper.selectById(recordId);
        if (blacklist == null) {
            throw new ServiceException("黑名单记录不存在");
        }

        try {
            blacklistMapper.deleteById(recordId);
            User user = userMapper.selectById(blacklist.getTargetId());
            user.setUserReputation(100);
            user.setStatus(1);
            userMapper.updateById(user);
        } catch (Exception e) {
            throw new ServiceException("移除黑名单失败：" + e.getMessage());
        }
    }

    @Override
    public IPage<Blacklist> getBlacklist(int page, int size) throws ServiceException {
        try {
            Page<Blacklist> pageParam = new Page<>(page, size);
            QueryWrapper<Blacklist> queryWrapper = new QueryWrapper<>();
            queryWrapper.orderByDesc("created_at"); // 按更新时间倒序
            return blacklistMapper.selectPage(pageParam, queryWrapper);
        } catch (Exception e) {
            throw new ServiceException("获取黑名单列表失败：" + e.getMessage());
        }
    }

    @Override
    public boolean isInBlacklist(Integer targetId) throws ServiceException {
        try {
            QueryWrapper<Blacklist> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("target_id", targetId);
            Blacklist blacklist = blacklistMapper.selectOne(queryWrapper);
            return blacklist != null;
        } catch (Exception e) {
            throw new ServiceException("检查黑名单失败：" + e.getMessage());
        }
    }

    @Override
    public IPage<Blacklist> searchBlacklist(int page, int size, String keyword) throws ServiceException {
        try {
            Page<Blacklist> pageParam = new Page<>(page, size);
            QueryWrapper<Blacklist> queryWrapper = new QueryWrapper<>();

            // 多字段模糊查询
            if (StringUtils.hasText(keyword)) {
                queryWrapper.and(wrapper -> wrapper
                        .like("target_id", keyword)
                        .or().like("reason", keyword)
                );
            }

            queryWrapper.orderByDesc("created_at"); // 按更新时间倒序

            return blacklistMapper.selectPage(pageParam, queryWrapper);
        } catch (Exception e) {
            throw new ServiceException("搜索黑名单失败：" + e.getMessage());
        }
    }
}

