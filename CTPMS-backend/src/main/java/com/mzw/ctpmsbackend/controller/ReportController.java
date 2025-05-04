/*
 * @Author: David Ma
 * @FilePath: \campus-trading-platform-management-system\CTPMS-backend\src\main\java\com\mzw\ctpmsbackend\controller\ReportController.java
 * @LastEditors: David Ma
 * @Description: 该文件用于...
 * @Date: 2025-03-21 17:12:47
 */
package com.mzw.ctpmsbackend.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mzw.ctpmsbackend.annotation.OperationLog;
import com.mzw.ctpmsbackend.common.utils.DataResult;
import com.mzw.ctpmsbackend.dto.ReportDTO;
import com.mzw.ctpmsbackend.entity.Report;
import com.mzw.ctpmsbackend.exception.ServiceException;
import com.mzw.ctpmsbackend.service.ReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@Api(tags = "投诉管理")
@RestController
@RequestMapping("/api/report")
@SaCheckLogin
public class ReportController {

    @Resource
    private ReportService reportService;

    @ApiOperation("添加投诉记录")
    @PostMapping
    @OperationLog(type = "REPORT", value = "添加投诉")
    public DataResult<Report> addReport(@RequestBody @Valid ReportDTO reportDTO) {
        try {
            Report report = reportService.addReport(reportDTO);
            return DataResult.success("投诉提交成功", report);
        } catch (ServiceException e) {
            return DataResult.error(e.getMessage());
        }
    }

    @ApiOperation("删除投诉记录")
    @DeleteMapping("/{id}")
    @OperationLog(type = "REPORT", value = "删除投诉")
    public DataResult<Void> deleteReport(@PathVariable Integer id) {
        try {
            reportService.deleteReport(id);
            return DataResult.success("投诉删除成功");
        } catch (ServiceException e) {
            return DataResult.error(e.getMessage());
        }
    }

    @ApiOperation("更新投诉记录")
    @PutMapping
    @OperationLog(type = "REPORT", value = "更新投诉")
    public DataResult<Report> updateReport(@RequestBody @Valid ReportDTO reportDTO) {
        try {
            Report report = reportService.updateReport(reportDTO);
            return DataResult.success("投诉更新成功", report);
        } catch (ServiceException e) {
            return DataResult.error(e.getMessage());
        }
    }

    @ApiOperation("根据ID获取投诉记录")
    @GetMapping("/{id}")
    public DataResult<Report> getReport(@PathVariable Integer id) {
        try {
            Report report = reportService.getReport(id);
            return DataResult.success(report);
        } catch (ServiceException e) {
            return DataResult.error(e.getMessage());
        }
    }

    @ApiOperation("分页获取投诉记录(管理员)")
    @GetMapping("/list")
    @SaCheckRole("admin")
    public DataResult<IPage<Report>> getReportList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            IPage<Report> result = reportService.getReportList(page, size);
            return DataResult.success(result);
        } catch (ServiceException e) {
            return DataResult.error(e.getMessage());
        }
    }


    @ApiOperation("分页搜索投诉记录(管理员)")
    @GetMapping("/search")
    @SaCheckRole("admin")
    public DataResult<IPage<Report>> searchReports(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam String keyword) {
        try {
            IPage<Report> result = reportService.searchReports(page, size, keyword);
            return DataResult.success(result);
        } catch (ServiceException e) {
            return DataResult.error(e.getMessage());
        }
    }

    @ApiOperation("通过投诉(管理员)")
    @PutMapping("/approve/{reportId}")
    @SaCheckRole("admin")
    public DataResult<Boolean> approveReport(
            @PathVariable Integer reportId) {
        try {
            boolean result = reportService.approveReport(reportId);
            if (result) {
                return DataResult.success("投诉通过成功",true);
            } else {
                return DataResult.fail("投诉通过失败",false);
            }
        } catch (ServiceException e) {
            return DataResult.error(e.getMessage());
        }
    }

    @ApiOperation("拒绝投诉(管理员)")
    @PutMapping("/reject/{reportId}")
    @SaCheckRole("admin")
    public DataResult<Boolean> rejectReport(
            @PathVariable Integer reportId) {
        try {
            boolean result = reportService.rejectReport(reportId);
            if (result) {
                return DataResult.success("投诉拒绝成功",true);
            } else {
                return DataResult.fail("投诉拒绝失败",false);
            }
        } catch (ServiceException e) {
            return DataResult.error(e.getMessage());
        }
    }
}
