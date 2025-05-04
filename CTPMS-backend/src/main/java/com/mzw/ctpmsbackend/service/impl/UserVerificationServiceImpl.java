package com.mzw.ctpmsbackend.service.impl;


import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mzw.ctpmsbackend.annotation.AuditLog;
import com.mzw.ctpmsbackend.context.AuditLogContext;
import com.mzw.ctpmsbackend.dto.UserVerificationDTO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mzw.ctpmsbackend.entity.User;
import com.mzw.ctpmsbackend.entity.UserVerification;
import com.mzw.ctpmsbackend.exception.ServiceException;
import com.mzw.ctpmsbackend.mapper.UserMapper;
import com.mzw.ctpmsbackend.mapper.UserVerificationMapper;
import com.mzw.ctpmsbackend.service.ImageUploadService;
import com.mzw.ctpmsbackend.service.UserVerificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class UserVerificationServiceImpl extends ServiceImpl<UserVerificationMapper, UserVerification> implements UserVerificationService {



    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserVerification addUserVerification(UserVerificationDTO verificationDTO) throws ServiceException {
        try {
            if (verificationDTO == null) {
                throw new ServiceException("认证信息不能为空");
            }

            UserVerification verification = convertToEntity(verificationDTO);
            verification.setStatus(0); // 未审核
            verification.setCreatedAt(LocalDateTime.now());

            if (!this.save(verification)) {
                throw new ServiceException("认证添加失败");
            }

            log.info("添加认证成功，ID: {}", verification.getVerificationId());
            return verification;
        } catch (Exception e) {
            log.error("添加认证失败: {}", e.getMessage(), e);
            throw new ServiceException("添加认证失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteUserVerification(Integer verificationId) throws ServiceException {
        try {
            if (verificationId == null) {
                throw new ServiceException("认证ID不能为空");
            }

            boolean result = this.removeById(verificationId);
            if (!result) {
                throw new ServiceException("认证删除失败");
            }
            log.info("删除认证成功，ID: {}", verificationId);
            return true;
        } catch (Exception e) {
            log.error("删除认证失败，ID: {}", verificationId, e);
            throw new ServiceException("删除认证失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteUserVerification(List<Integer> ids) throws ServiceException {
        if (ids == null || ids.isEmpty()) {
            throw new ServiceException("ID列表不能为空");
        }
        try {
            boolean result = this.removeBatchByIds(ids);
            if (!result) {
                throw new ServiceException("删除失败，可能没有符合条件的记录");
            }
            log.info("批量删除用户证件认证记录成功，数量: {}", ids.size());
            return true;
        } catch (Exception e) {
            log.error("批量删除用户证件认证记录失败", e);
            throw new ServiceException("批量删除用户证件认证记录失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUserVerification(UserVerificationDTO verificationDTO) throws ServiceException {
        try {
            if (verificationDTO == null || verificationDTO.getVerificationId() == null) {
                throw new ServiceException("认证信息不完整");
            }

            UserVerification verification = convertToEntity(verificationDTO);
            verification.setVerifiedAt(LocalDateTime.now());

            boolean result = this.updateById(verification);
            if (!result) {
                throw new ServiceException("认证更新失败");
            }

            log.info("更新认证成功，ID: {}", verification.getVerificationId());
            return true;
        } catch (Exception e) {
            log.error("更新认证失败: {}", e.getMessage(), e);
            throw new ServiceException("更新认证失败: " + e.getMessage());
        }
    }

    @Override
    public IPage<UserVerification> getUserVerificationList(int page, int size) throws ServiceException {
        try {
            if (page < 1) page = 1;
            if (size < 1 || size > 100) size = 10;

            return this.page(new Page<>(page, size), new QueryWrapper<UserVerification>().orderByDesc("created_at"));
        } catch (Exception e) {
            log.error("获取认证列表失败", e);
            throw new ServiceException("获取认证列表失败: " + e.getMessage());
        }
    }

    @Override
    public IPage<UserVerification> searchUserVerifications(int page, int size, String keyword) throws ServiceException {
        try {
            if (page < 1) page = 1;
            if (size < 1 || size > 100) size = 10;

            QueryWrapper<UserVerification> queryWrapper = new QueryWrapper<>();
            if (StringUtils.hasText(keyword)) {
                queryWrapper.lambda()
                        .like(UserVerification::getIdCardName, keyword)
                        .or()
                        .like(UserVerification::getIdCardNumber, keyword)
                        .or()
                        .like(UserVerification::getStudentId, keyword);
            }

            return this.page(new Page<>(page, size), queryWrapper.orderByDesc("created_at"));
        } catch (Exception e) {
            log.error("搜索用户认证记录失败", e);
            throw new ServiceException("搜索用户认证记录失败: " + e.getMessage());
        }
    }

    @Override
    public List<UserVerification> getAllPendingUserVerifications() throws ServiceException {
        try {
            return this.list(new QueryWrapper<UserVerification>().eq("status", 0));
        } catch (Exception e) {
            log.error("获取待审核认证失败", e);
            throw new ServiceException("获取待审核认证失败: " + e.getMessage());
        }
    }

    @Override
    public UserVerification getUserVerificationById(Integer id) throws ServiceException {
        try {
            if (id == null) {
                throw new ServiceException("认证ID不能为空");
            }

            UserVerification verification = this.getById(id);
            if (verification == null) {
                throw new ServiceException("认证信息不存在");
            }

            return verification;
        } catch (Exception e) {
            log.error("获取认证信息失败，ID: {}", id, e);
            throw new ServiceException("获取认证信息失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @AuditLog(type = "USER", value = "审核通过")
    public boolean approveUserVerification(Integer verificationId) throws ServiceException {
        try {
            int verifiedBy = StpUtil.getLoginIdAsInt();
            if (verificationId == null) {
                throw new ServiceException("核验记录ID不能为空");
            }
            if (verifiedBy == 0) {
                throw new ServiceException("获取不到审核人信息");

            }

            UserVerification verification = this.getById(verificationId);
            if (verification == null) {
                throw new ServiceException("认证信息不存在");
            }

            verification.setStatus(1); // 通过
            verification.setVerifiedAt(LocalDateTime.now());
            verification.setVerifiedBy(verifiedBy);
            verification.setReason("审核通过");

            boolean result = this.updateById(verification);
            if (!result) {
                log.error("审核通过更新数据库失败，ID: {}", verificationId);
                return false;
            }
            // 设置审核结果（通过）
            AuditLogContext.setResult(1);
            AuditLogContext.setTargetId(verificationId);
            AuditLogContext.setReason("审核通过");

            log.info("审核通过成功，ID: {}", verificationId);
            return true;
        } catch (Exception e) {
            log.error("审核通过失败，ID: {}", verificationId, e);
            throw new ServiceException("审核通过失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @AuditLog(type = "USER", value = "审核通过")
    public boolean rejectUserVerification(Integer verificationId, String reason) throws ServiceException {
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

            UserVerification verification = this.getById(verificationId);
            if (verification == null) {
                throw new ServiceException("认证信息不存在");
            }

            verification.setStatus(2); // 拒绝
            verification.setVerifiedAt(LocalDateTime.now());
            verification.setReason(reason);

            boolean result = this.updateById(verification);
            if (!result) {
                log.error("审核拒绝更新数据库失败，ID: {}", verificationId);
                return false;
            }

            // 设置审核结果（失败）
            AuditLogContext.setResult(2);
            AuditLogContext.setTargetId(verificationId);
            AuditLogContext.setReason(reason);
            log.info("审核拒绝成功，ID: {}", verificationId);
            return true;
        } catch (Exception e) {
            log.error("审核拒绝失败，ID: {}", verificationId, e);
            throw new ServiceException("审核拒绝失败: " + e.getMessage());
        }
    }

    @Override
    public boolean getUserVerificationStatus(Integer userId) throws ServiceException {
        try {
            User user = userMapper.selectById(userId);
            if (user == null) {
                throw new ServiceException("查询用户信息失败");
            }

            Integer status = user.getUserVerified();
            return status != null && status != 0;
        } catch (Exception e) {
            log.error("查询用户证件认证状态失败，用户ID: {}", userId, e);
            throw new ServiceException("查询用户证件认证状态失败: " + e.getMessage());
        }
    }

    /**
     * DTO 转 Entity 方法
     */
    private UserVerification convertToEntity(UserVerificationDTO dto) {
        UserVerification entity = new UserVerification();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }
}


