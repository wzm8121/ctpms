package com.mzw.ctpmsbackend.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mzw.ctpmsbackend.annotation.OperationLog;
import com.mzw.ctpmsbackend.common.utils.DataResult;
import com.mzw.ctpmsbackend.dto.BlacklistDTO;
import com.mzw.ctpmsbackend.entity.Blacklist;
import com.mzw.ctpmsbackend.exception.ServiceException;
import com.mzw.ctpmsbackend.service.BlacklistService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 黑名单管理控制器
 * 包含黑名单的增删改查操作，使用MyBatisPlus进行分页查询
 * 所有请求都需要通过SaToken进行权限校验
 */
@Api(tags = "黑名单管理")
@RestController
@RequestMapping("/api/blacklist")
@SaCheckLogin
public class BlacklistController {

    @Resource
    private BlacklistService blacklistService;

    /**
     * 添加黑名单
     * @param blacklistDTO 黑名单信息
     * @return 操作结果
     */
    @SaCheckRole("admin")
    @PostMapping("/add")
    @OperationLog(type = "BLACKLIST", value = "添加黑名单")
    @ApiOperation("添加黑名单记录")
    public DataResult<String> addToBlacklist(@RequestBody BlacklistDTO blacklistDTO) {
        try {
            blacklistService.addToBlacklist(blacklistDTO);
            return DataResult.success("添加黑名单成功");
        } catch (ServiceException e) {
            return DataResult.error(e.getMessage());
        }
    }

    /**
     * 删除黑名单
     * @param recordId 记录ID
     * @return 操作结果
     */
    @SaCheckRole("admin")
    @DeleteMapping("/remove/{recordId}")
    @OperationLog(type = "BLACKLIST", value = "删除黑名单")
    @ApiOperation("删除黑名单记录")
    public DataResult<String> removeFromBlacklist(@PathVariable Integer recordId) {
        try {
            blacklistService.removeFromBlacklist(recordId);
            return DataResult.success("移除黑名单成功");
        } catch (ServiceException e) {
            return DataResult.error(e.getMessage());
        }
    }

    /**
     * 分页获取黑名单列表
     * @param page 当前页码
     * @param size 每页数量
     * @return 黑名单列表
     */
    @SaCheckRole("admin")
    @GetMapping("/list")
    @ApiOperation("获取黑名单分页列表")
    public DataResult<IPage<Blacklist>> getBlacklist(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            IPage<Blacklist> result = blacklistService.getBlacklist(page, size);
            return DataResult.success(result);
        } catch (ServiceException e) {
            return DataResult.error(e.getMessage());
        }
    }

    /**
     * 检查是否在黑名单中
     * @param targetId 目标ID
     * @return 检查结果
     */
    @GetMapping("/check")
    @ApiOperation("检查目标是否在黑名单中")
    public DataResult<Boolean> isInBlacklist(
            @RequestParam Integer targetId) {
        try {
            boolean result = blacklistService.isInBlacklist(targetId);
            return DataResult.success("查询成功",result);
        } catch (ServiceException e) {
            return DataResult.error(e.getMessage());
        }
    }

    /**
     * 搜索黑名单
     * @param page 当前页码
     * @param size 每页数量
     * @param keyword 搜索关键字
     * @return 搜索结果
     */
    @SaCheckRole("admin")
    @GetMapping("/search")
    @ApiOperation("搜索黑名单记录")
    public DataResult<IPage<Blacklist>> searchBlacklist(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword) {
        try {
            IPage<Blacklist> result = blacklistService.searchBlacklist(page, size, keyword);
            return DataResult.success(result);
        } catch (ServiceException e) {
            return DataResult.error(e.getMessage());
        }
    }
}
