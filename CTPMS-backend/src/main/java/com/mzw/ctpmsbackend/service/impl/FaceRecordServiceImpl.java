package com.mzw.ctpmsbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mzw.ctpmsbackend.common.utils.DataResult;
import com.mzw.ctpmsbackend.dto.FaceRecordDTO;
import com.mzw.ctpmsbackend.entity.FaceRecord;
import com.mzw.ctpmsbackend.exception.ServiceException;
import com.mzw.ctpmsbackend.mapper.FaceRecordMapper;
import com.mzw.ctpmsbackend.service.FaceRecordService;
import org.nd4j.common.io.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 人脸识别记录服务实现类
 * 提供人脸识别记录的增删改查及统计功能实现
 */
@Service
public class FaceRecordServiceImpl extends ServiceImpl<FaceRecordMapper, FaceRecord> implements FaceRecordService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addFaceRecord(FaceRecordDTO faceRecordDTO) throws ServiceException {
        try {
            // 参数校验
            if (faceRecordDTO == null || faceRecordDTO.getUserId() == null) {
                throw new ServiceException("人脸记录信息不能为空");
            }

            FaceRecord faceRecord = convertToEntity(faceRecordDTO);
            faceRecord.setCreatedAt(LocalDateTime.now());

            if (!this.save(faceRecord)) {
                throw new ServiceException("人脸记录添加失败");
            }
        } catch (Exception e) {
            throw new ServiceException("人脸记录添加失败：" + e.getMessage());
        }
    }

    @Override
    public IPage<FaceRecordDTO> getFaceRecordsByUser(Integer userId, int page, int size) throws ServiceException {
        try {
            if (userId == null) {
                throw new ServiceException("用户ID不能为空");
            }

            Page<FaceRecord> pageParam = new Page<>(page, size);
            QueryWrapper<FaceRecord> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", userId)
                    .orderByDesc("created_at");

            IPage<FaceRecord> faceRecordPage = this.page(pageParam, queryWrapper);
            return faceRecordPage.convert(this::convertToDTO);
        } catch (Exception e) {
            throw new ServiceException("获取人脸记录失败：" + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getFaceRecordStatistics() throws ServiceException {
        try {
            QueryWrapper<FaceRecord> queryWrapper = new QueryWrapper<>();
            queryWrapper.select("result, count(*) as count")
                    .groupBy("result");

            return this.listMaps(queryWrapper);
        } catch (Exception e) {
            throw new ServiceException("获取统计信息失败：" + e.getMessage());
        }
    }

    @Override
    public IPage<FaceRecordDTO> getFaceRecordsByTimeRange(String startTime, String endTime, int page, int size) throws ServiceException {
        try {
            if (!StringUtils.hasText(startTime) || !StringUtils.hasText(endTime)) {
                throw new ServiceException("时间范围不能为空");
            }

            Page<FaceRecord> pageParam = new Page<>(page, size);
            QueryWrapper<FaceRecord> queryWrapper = new QueryWrapper<>();
            queryWrapper.between("created_at", startTime, endTime)
                    .orderByDesc("created_at");

            IPage<FaceRecord> faceRecordPage = this.page(pageParam, queryWrapper);
            return faceRecordPage.convert(this::convertToDTO);
        } catch (Exception e) {
            throw new ServiceException("按时间查询失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFaceRecord(Integer id) throws ServiceException {
        try {
            if (id == null) {
                throw new ServiceException("记录ID不能为空");
            }

            if (!this.removeById(id)) {
                throw new ServiceException("人脸记录不存在或删除失败");
            }
        } catch (Exception e) {
            throw new ServiceException("删除失败：" + e.getMessage());
        }
    }

    @Override
    public IPage<FaceRecordDTO> searchFaceRecords(String keyword, int page, int size) throws ServiceException {
        try {
            Page<FaceRecord> pageParam = new Page<>(page, size);
            QueryWrapper<FaceRecord> queryWrapper = new QueryWrapper<>();

            if (StringUtils.hasText(keyword)) {
                queryWrapper.like("user_id", keyword)
                        .or().like("result", keyword)
                        .orderByDesc("created_at");
            } else {
                queryWrapper.orderByDesc("created_at");
            }

            IPage<FaceRecord> faceRecordPage = this.page(pageParam, queryWrapper);
            return faceRecordPage.convert(this::convertToDTO);
        } catch (Exception e) {
            throw new ServiceException("搜索失败：" + e.getMessage());
        }
    }

    /**
     * DTO 转实体对象
     */
    private FaceRecord convertToEntity(FaceRecordDTO dto) {
        FaceRecord record = new FaceRecord();
        record.setUserId(dto.getUserId());
        record.setDeviceType(dto.getDeviceType());
        record.setImageUrl(dto.getImageUrl());
        record.setResult(dto.getResult());
        record.setConfidence(dto.getConfidence());
        record.setCreatedAt(dto.getCreatedAt());
        return record;
    }

    /**
     * 实体对象转 DTO
     */
    private FaceRecordDTO convertToDTO(FaceRecord record) {
        FaceRecordDTO dto = new FaceRecordDTO();
        dto.setRecordId(record.getRecordId());
        dto.setUserId(record.getUserId());
        dto.setResult(record.getResult());
        dto.setImageUrl(record.getImageUrl());
        dto.setConfidence(record.getConfidence());
        dto.setCreatedAt(record.getCreatedAt());
        dto.setConfidence(record.getConfidence());
        dto.setDeviceType(record.getDeviceType());
        return dto;
    }
}
