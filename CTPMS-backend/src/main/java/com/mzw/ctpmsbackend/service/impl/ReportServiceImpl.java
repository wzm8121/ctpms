/*
 * @Author: David Ma
 * @FilePath: \campus-trading-platform-management-system\CTPMS-backend\src\main\java\com\mzw\ctpmsbackend\service\impl\ReportServiceImpl.java
 * @LastEditors: David Ma
 * @Description: 该文件用于...
 * @Date: 2025-03-21 17:15:16
 */
package com.mzw.ctpmsbackend.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mzw.ctpmsbackend.dto.BlacklistDTO;
import com.mzw.ctpmsbackend.dto.ReportDTO;
import com.mzw.ctpmsbackend.entity.Report;
import com.mzw.ctpmsbackend.entity.User;
import com.mzw.ctpmsbackend.exception.ServiceException;
import com.mzw.ctpmsbackend.mapper.ReportMapper;
import com.mzw.ctpmsbackend.mapper.UserMapper;
import org.nd4j.common.io.StringUtils;

import com.mzw.ctpmsbackend.service.ReportService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Slf4j
@Service
public class ReportServiceImpl extends ServiceImpl<ReportMapper, Report> implements ReportService {

    @Resource
    UserMapper userMapper;

    @Resource
    BlacklistServiceImpl blacklistService;

    public ReportServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Report addReport(ReportDTO reportDTO) throws ServiceException {
        try {
            if (reportDTO == null) {
                throw new ServiceException("举报信息不能为空");
            }

            Report report = convertToEntity(reportDTO);
            report.setStatus(0);
            report.setCreatedAt(LocalDateTime.now());

            if (!this.save(report)) {
                throw new ServiceException("举报添加失败");
            }

            log.info("举报添加成功，ID: {}", report.getReportId());
            return report;
        } catch (Exception e) {
            log.error("举报添加失败: {}", e.getMessage(), e);
            throw new ServiceException("举报添加失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteReport(Integer id) throws ServiceException {
        try {
            if (id == null) {
                throw new ServiceException("举报 ID 不能为空");
            }

            Report existing = this.getById(id);
            if (existing == null) {
                throw new ServiceException("举报不存在");
            }

            boolean result = this.removeById(id);
            if (!result) {
                throw new ServiceException("举报删除失败");
            }

            log.info("举报删除成功，ID: {}", id);
            return true;
        } catch (Exception e) {
            log.error("举报删除失败，ID: {}", id, e);
            throw new ServiceException("举报删除失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Report updateReport(ReportDTO reportDTO) throws ServiceException {
        try {
            if (reportDTO == null || reportDTO.getReportId() == null) {
                throw new ServiceException("举报信息不完整");
            }

            Report existing = this.getById(reportDTO.getReportId());
            if (existing == null) {
                throw new ServiceException("举报不存在");
            }

            Report report = convertToEntity(reportDTO);


            if (!this.updateById(report)) {
                throw new ServiceException("举报更新失败");
            }

            log.info("举报更新成功，ID: {}", report.getReportId());
            return report;
        } catch (Exception e) {
            log.error("举报更新失败: {}", e.getMessage(), e);
            throw new ServiceException("举报更新失败: " + e.getMessage());
        }
    }

    @Override
    public Report getReport(Integer id) throws ServiceException {
        try {
            if (id == null) {
                throw new ServiceException("举报 ID 不能为空");
            }

            Report report = this.getById(id);
            if (report == null) {
                throw new ServiceException("举报不存在");
            }

            return report;
        } catch (Exception e) {
            log.error("获取举报失败，ID: {}", id, e);
            throw new ServiceException("获取举报失败: " + e.getMessage());
        }
    }

    @Override
    public IPage<Report> getReportList(int page, int size) throws ServiceException {
        try {
            if (page < 1) page = 1;
            if (size < 1 || size > 100) size = 10;

            QueryWrapper<Report> queryWrapper = new QueryWrapper<>();
            return this.page(new Page<>(page, size),
                    queryWrapper);
        } catch (Exception e) {
            log.error("获取举报列表失败", e);
            throw new ServiceException("获取举报列表失败: " + e.getMessage());
        }
    }

    @Override
    public IPage<Report> searchReports(int page, int size, String keyword) throws ServiceException {
        try {
            if (page < 1) page = 1;
            if (size < 1 || size > 100) size = 10;

            QueryWrapper<Report> queryWrapper = new QueryWrapper<>();
            if (StringUtils.hasText(keyword)) {
                if (keyword.length() > 50) {
                    throw new ServiceException("搜索关键词过长");
                }

                queryWrapper.like("content", keyword)
                        .or().like("target_id", keyword)
                        .or().eq("report_id", keyword);
            }

            return this.page(new Page<>(page, size), queryWrapper);
        } catch (Exception e) {
            log.error("搜索举报失败", e);
            throw new ServiceException("搜索举报失败: " + e.getMessage());
        }
    }

    @Override
    public boolean approveReport(Integer reportId) throws ServiceException{
        Integer handledId = StpUtil.getLoginIdAsInt();
        Report report = this.getById(reportId);
        if (report == null) {
            throw new ServiceException("未查到投诉信息");
        }
        report.setStatus(1);
        report.setHandledAt(LocalDateTime.now());
        report.setHandledBy(handledId);

        //降低被投诉用户的信誉分，若低于60，加入黑名单

        Integer targetId = report.getTargetId();

        User user = userMapper.selectById(targetId);
        if (user == null) {
            throw new ServiceException("未找到被投诉用户信息");
        }

        user.setUserReputation(user.getUserReputation() - 10);

        if (user.getUserReputation() <= 60 ) {
            //加入黑名单
            BlacklistDTO blacklistDTO = new BlacklistDTO();
            blacklistDTO.setTargetId(targetId);
            blacklistDTO.setReason("信誉分过低，已加入黑名单");
            blacklistService.addToBlacklist(blacklistDTO);
            user.setStatus(0);
        }
        userMapper.updateById(user);
        return this.updateById(report);
    }

    @Override
    public boolean rejectReport(Integer reportId) throws ServiceException{
        Integer handledId = StpUtil.getLoginIdAsInt();
        Report report = this.getById(reportId);
        if (report == null) {
            throw new ServiceException("未查到投诉信息");
        }
        report.setStatus(2);
        report.setHandledAt(LocalDateTime.now());
        report.setHandledBy(handledId);

        return this.updateById(report);
    }
    /**
     * DTO → Entity 转换方法
     */
    private Report convertToEntity(ReportDTO dto) {
        Report report = new Report();
        BeanUtils.copyProperties(dto, report);
        return report;
    }
}


