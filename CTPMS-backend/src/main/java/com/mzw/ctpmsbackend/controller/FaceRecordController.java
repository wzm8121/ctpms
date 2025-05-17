package com.mzw.ctpmsbackend.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mzw.ctpmsbackend.annotation.OperationLog;
import com.mzw.ctpmsbackend.common.utils.DataResult;
import com.mzw.ctpmsbackend.dto.FaceRecordDTO;
import com.mzw.ctpmsbackend.exception.ServiceException;
import com.mzw.ctpmsbackend.service.FaceRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 人脸识别记录控制器
 * 提供人脸识别记录的增删改查及统计功能
 */
@Api(tags = "人脸识别记录管理")
@RestController
@RequestMapping("/api/face-record")
public class FaceRecordController {

    @Resource
    private FaceRecordService faceRecordService;

    /**
     * 添加人脸识别记录
     * @param faceRecordDTO 人脸识别记录信息
     * @return 操作结果
     */
    @SaCheckLogin
    @PostMapping("/add")
    @OperationLog(type = "FACE_RECORD", value = "添加人脸记录")
    @ApiOperation("添加人脸识别记录")
    public DataResult<String> addFaceRecord(@RequestBody FaceRecordDTO faceRecordDTO) {
        try {
            faceRecordService.addFaceRecord(faceRecordDTO);
            return DataResult.success("人脸记录添加成功");
        } catch (ServiceException e) {
            return DataResult.error(e.getMessage());
        }
    }

    /**
     * 根据用户ID获取人脸识别记录
     * @param userId 用户ID
     * @param page 当前页码
     * @param size 每页数量
     * @return 人脸识别记录列表
     */
    @SaCheckLogin
    @GetMapping("/user/{userId}")
    @ApiOperation("根据用户ID获取人脸记录")
    public DataResult<IPage<FaceRecordDTO>> getFaceRecordsByUser(
            @PathVariable Integer userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            IPage<FaceRecordDTO> result = faceRecordService.getFaceRecordsByUser(userId, page, size);
            return DataResult.success(result);
        } catch (ServiceException e) {
            return DataResult.error(e.getMessage());
        }
    }

    /**
     * 获取人脸识别统计信息
     * @return 统计信息
     */
    @SaCheckLogin
    @SaCheckRole("admin")
    @GetMapping("/statistics")
    @ApiOperation("获取人脸识别统计信息")
    public DataResult<List<Map<String, Object>>> getFaceRecordStatistics() {
        try {
            List<Map<String, Object>> result = faceRecordService.getFaceRecordStatistics();
            return DataResult.success(result);
        } catch (ServiceException e) {
            return DataResult.error(e.getMessage());
        }
    }

    /**
     * 根据时间范围查询人脸记录
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param page 当前页码
     * @param size 每页数量
     * @return 人脸识别记录列表
     */
    @SaCheckLogin
    @SaCheckRole("admin")
    @GetMapping("/time-range")
    @ApiOperation("按时间范围查询人脸记录")
    public DataResult<IPage<FaceRecordDTO>> getFaceRecordsByTimeRange(
            @RequestParam String startTime,
            @RequestParam String endTime,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            IPage<FaceRecordDTO> result = faceRecordService.getFaceRecordsByTimeRange(startTime, endTime, page, size);
            return DataResult.success(result);
        } catch (ServiceException e) {
            return DataResult.error(e.getMessage());
        }
    }

    /**
     * 删除人脸记录
     * @param id 记录ID
     * @return 操作结果
     */
    @SaCheckLogin
    @SaCheckRole("admin")
    @DeleteMapping("/{id}")
    @ApiOperation("删除人脸记录")
    public DataResult<String> deleteFaceRecord(@PathVariable Integer id) {
        try {
            faceRecordService.deleteFaceRecord(id);
            return DataResult.success("人脸记录删除成功");
        } catch (ServiceException e) {
            return DataResult.error(e.getMessage());
        }
    }

    @SaCheckLogin
    @SaCheckRole("admin")
    @GetMapping("/list")
    @ApiOperation("获取所有人脸识别记录（默认列表）")
    public DataResult<IPage<FaceRecordDTO>> getAllFaceRecords(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            IPage<FaceRecordDTO> result = faceRecordService.searchFaceRecords(page, size, null, null);
            return DataResult.success(result);
        } catch (ServiceException e) {
            return DataResult.error(e.getMessage());
        }
    }
    /**
     * 搜索人脸记录
     * @param keyword 搜索关键字
     * @param page 当前页码
     * @param size 每页数量
     * @return 人脸识别记录列表
     */
    @SaCheckLogin
    @SaCheckRole("admin")
    @GetMapping("/search/{type}")
    @ApiOperation("搜索人脸记录")
    public DataResult<IPage<FaceRecordDTO>> searchFaceRecords(
            @PathVariable("type") String type,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            IPage<FaceRecordDTO> result = faceRecordService.searchFaceRecords( page, size,keyword, type);
            return DataResult.success(result);
        } catch (ServiceException e) {
            return DataResult.error(e.getMessage());
        }
    }

}
