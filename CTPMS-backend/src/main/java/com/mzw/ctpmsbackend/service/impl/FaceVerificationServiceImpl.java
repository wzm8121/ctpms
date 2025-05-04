package com.mzw.ctpmsbackend.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mzw.ctpmsbackend.annotation.AuditLog;
import com.mzw.ctpmsbackend.context.AuditLogContext;
import com.mzw.ctpmsbackend.dto.FaceVerificationDTO;
import com.mzw.ctpmsbackend.dto.UserVerificationDTO;
import com.mzw.ctpmsbackend.entity.FaceReview;
import com.mzw.ctpmsbackend.entity.User;
import com.mzw.ctpmsbackend.entity.UserVerification;
import com.mzw.ctpmsbackend.exception.ServiceException;
import com.mzw.ctpmsbackend.mapper.FaceVerificationMapper;
import com.mzw.ctpmsbackend.mapper.UserMapper;
import com.mzw.ctpmsbackend.service.FaceVerificationService;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.nd4j.common.io.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class FaceVerificationServiceImpl extends ServiceImpl<FaceVerificationMapper, FaceReview>
        implements FaceVerificationService {


    private final UserMapper userMapper;
    private final FaceVerificationMapper faceVerificationMapper;

    public FaceVerificationServiceImpl(UserMapper userMapper, FaceVerificationMapper faceVerificationMapper) {
        this.userMapper = userMapper;
        this.faceVerificationMapper = faceVerificationMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FaceReview createVerification(FaceVerificationDTO verificationDTO) throws ServiceException {
        try {
            // 参数校验
            if (verificationDTO == null) {
                throw new ServiceException("核验信息不能为空");
            }
            if (verificationDTO.getUserId() == null) {
                throw new ServiceException("学生ID不能为空");
            }
            if (!StringUtils.hasText(verificationDTO.getFaceImageUrl())) {
                throw new ServiceException("人脸图片URL不能为空");
            }


            FaceReview verification = new FaceReview();
            BeanUtils.copyProperties(verificationDTO, verification);
            verification.setStatus(0);
            verification.setCreatedAt(LocalDateTime.now());

            if (!save(verification)) {
                throw new ServiceException("创建核验记录失败");
            }

            log.info("创建人脸核验记录成功，ID：{}", verification.getUserId());
            return verification;
        } catch (Exception e) {
            log.error("创建人脸核验记录失败：{}", e.getMessage());
            throw new ServiceException("创建核验记录失败：" + e.getMessage());
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteFaceVerification(Integer verificationId) throws ServiceException {
        if (verificationId == null) {
            throw new ServiceException("认证ID不能为空");
        }
        try {
            boolean result = this.removeById(verificationId);
            if (result) {
                log.info("删除人脸认证成功，ID: {}", verificationId);
            } else {
                log.warn("删除人脸认证失败，ID: {}", verificationId);
            }
            return result;
        } catch (Exception e) {
            log.error("删除人脸认证异常，ID: {}", verificationId, e);
            throw new ServiceException("删除人脸认证异常", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteFaceVerification(List<Integer> ids) throws ServiceException {
        if (ids == null || ids.isEmpty()) {
            throw new ServiceException("ID列表不能为空");
        }
        try {
            int deletedCount = faceVerificationMapper.deleteBatchIds(ids);
            if (deletedCount > 0) {
                log.info("批量删除人脸认证记录成功，数量: {}", deletedCount);
                return true;
            } else {
                log.warn("批量删除人脸认证记录失败，无符合条件的记录");
                return false;
            }
        } catch (Exception e) {
            log.error("批量删除人脸认证记录异常", e);
            throw new ServiceException("批量删除人脸认证记录异常", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateFaceVerification(FaceVerificationDTO verificationDTO) throws ServiceException {
        if (verificationDTO == null || verificationDTO.getVerifiedBy() == null) {
            throw new ServiceException("认证信息不完整");
        }
        try {
            FaceReview verification = BeanUtil.copyProperties(verificationDTO, FaceReview.class);
            verification.setVerifiedAt(LocalDateTime.now());
            boolean result = this.updateById(verification);
            if (result) {
                log.info("更新人脸认证成功，ID: {}", verification.getFaceReviewId());
            } else {
                log.warn("更新人脸认证失败，ID: {}", verification.getFaceReviewId());
            }
            return result;
        } catch (Exception e) {
            log.error("更新人脸认证异常", e);
            throw new ServiceException("更新人脸认证异常", e);
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    @AuditLog(type = "FACE", value = "审核通过")
    public boolean approveVerification(Integer verificationId) throws ServiceException {
        try {
            int verifiedBy = StpUtil.getLoginIdAsInt();
            if (verificationId == null) {
                throw new ServiceException("核验记录ID不能为空");
            }
            if (verifiedBy == 0) {
                throw new ServiceException("获取不到审核人信息");
            }
            FaceReview verification = getById(verificationId);
            if (verification == null) {
                throw new ServiceException("核验记录不存在");
            }
            if (verification.getStatus() != 0) {
                throw new ServiceException("当前状态不允许审批");
            }

            verification.setStatus(1);
            verification.setVerifiedBy(verifiedBy);
            verification.setVerifiedAt(LocalDateTime.now());
            verification.setReason("审核通过");

            if (!updateById(verification)) {
                log.error("审核通过更新数据库失败，ID: {}", verificationId);
                return false;
            }

            // 设置审核结果（通过）
            AuditLogContext.setResult(1);
            AuditLogContext.setTargetId(verificationId);
            AuditLogContext.setReason("审核通过");
            log.info("人脸核验记录审批通过，ID：{}", verificationId);
            User user = userMapper.selectById(verification.getUserId());
            user.setFaceVerified(1);
            userMapper.updateById(user);
            return true;
        } catch (Exception e) {
            log.error("审批人脸核验记录失败：{}", e.getMessage());
            throw new ServiceException("审批失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @AuditLog(type = "FACE", value = "审核失败")
    public boolean rejectVerification(Integer verificationId, String reason) throws ServiceException {
        try {
            int verifiedBy = StpUtil.getLoginIdAsInt();
            if (verificationId == null) {
                throw new ServiceException("核验记录ID不能为空");
            }
            if (verifiedBy == 0) {
                throw new ServiceException("获取不到审核人信息");
            }
            if (reason == null) {
                throw new ServiceException("驳回原因不能为空");
            }

            FaceReview verification = getById(verificationId);
            if (verification == null) {
                throw new ServiceException("核验记录不存在");
            }
            if (verification.getStatus() != 0) {
                throw new ServiceException("当前状态不允许驳回");
            }

            verification.setStatus(2);
            verification.setVerifiedBy(verifiedBy);
            verification.setVerifiedAt(LocalDateTime.now());
            verification.setReason(reason);

            if (!updateById(verification)) {
                log.error("审核拒绝更新数据库失败，ID: {}", verificationId);
                return false;
            }
            // 设置审核结果（失败）
            AuditLogContext.setResult(2);
            AuditLogContext.setTargetId(verificationId);
            AuditLogContext.setReason(reason);

            log.info("人脸核验记录驳回，ID：{}，原因：{}", verificationId, reason);
            return true;
        } catch (Exception e) {
            log.error("驳回人脸核验记录失败：{}", e.getMessage());
            throw new ServiceException("驳回失败：" + e.getMessage());
        }
    }

    @Override
    public List<FaceReview> getAllPendingVerifications() throws ServiceException {
        try {
            QueryWrapper<FaceReview> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("status", 0)
                    .orderByAsc("created_at"); // 按创建时间升序处理
            return list(queryWrapper);
        } catch (Exception e) {
            log.error("获取待处理核验记录失败：{}", e.getMessage());
            throw new ServiceException("获取待处理记录失败：" + e.getMessage());
        }
    }

    @Override
    public FaceReview getVerificationById(Integer id) throws ServiceException {
        try {
            if (id == null) {
                throw new ServiceException("记录ID不能为空");
            }

            FaceReview verification = getById(id);
            if (verification == null) {
                throw new ServiceException("核验记录不存在");
            }
            return verification;
        } catch (Exception e) {
            log.error("获取核验记录详情失败：{}", e.getMessage());
            throw new ServiceException("获取记录详情失败：" + e.getMessage());
        }
    }

    @Override
    public IPage<FaceReview> getVerificationList(int page, int size) throws ServiceException {
        try {
            if (page < 1) page = 1;
            if (size < 1 || size > 100) size = 10;

            return page(new Page<>(page, size),
                    new QueryWrapper<FaceReview>()
                            .orderByDesc("created_at"));
        } catch (Exception e) {
            log.error("获取核验记录列表失败：{}", e.getMessage());
            throw new ServiceException("获取记录列表失败：" + e.getMessage());
        }
    }

    @Override
    public IPage<FaceReview> searchVerifications(int page, int size, String keyword) throws ServiceException {
        try {
            if (page < 1) page = 1;
            if (size < 1 || size > 100) size = 10;

            QueryWrapper<FaceReview> queryWrapper = new QueryWrapper<>();
            if (StringUtils.hasText(keyword)) {
                if (keyword.length() > 50) {
                    throw new ServiceException("搜索关键词过长");
                }
                queryWrapper.like("user_id", keyword)
                        .or().like("face_image_url", keyword)
                        .or().like("verified_by", keyword);
            }
            return page(new Page<>(page, size),
                    queryWrapper.orderByDesc("created_at"));
        } catch (Exception e) {
            log.error("搜索核验记录失败：{}", e.getMessage());
            throw new ServiceException("搜索记录失败：" + e.getMessage());
        }
    }

    public boolean getUserFaceVerificationStatus(Integer userId) throws ServiceException {
        try {
            User user = userMapper.selectById(userId);
            if (user == null) {
                throw new ServiceException("查询用户信息失败");
            }

            Integer status = user.getFaceVerified();
            return status != 0;

        }catch (Exception e) {
            log.error("查询用户人脸认证状态失败，用户ID: {}", userId, e);
            throw new ServiceException("查询用户人脸认证状态失败：: " + e.getMessage());
        }
    }
}
