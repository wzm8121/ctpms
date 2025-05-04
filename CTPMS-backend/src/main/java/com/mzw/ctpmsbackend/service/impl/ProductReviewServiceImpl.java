package com.mzw.ctpmsbackend.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.mzw.ctpmsbackend.annotation.AuditLog;
import com.mzw.ctpmsbackend.context.AuditLogContext;
import com.mzw.ctpmsbackend.dto.ProductReviewMessage;
import com.mzw.ctpmsbackend.entity.*;
import com.mzw.ctpmsbackend.exception.ServiceException;
import com.mzw.ctpmsbackend.mapper.ProductMapper;
import com.mzw.ctpmsbackend.mapper.UserMapper;
import com.mzw.ctpmsbackend.ollama.OllamaClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nd4j.common.io.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mzw.ctpmsbackend.dto.ProductReviewDTO;
import com.mzw.ctpmsbackend.mapper.ProductReviewMapper;
import com.mzw.ctpmsbackend.service.ProductReviewService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductReviewServiceImpl extends ServiceImpl<ProductReviewMapper, ProductReview>
        implements ProductReviewService {

    private final ProductMapper productMapper;
    private final ProductReviewMapper productReviewMapper;

    @Autowired
    private OllamaClient ollamaClient;
    @Autowired
    private UserMapper userMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    @AuditLog(value = "自动审核", type = "AUTO")
    public void autoReview(ProductReviewMessage message) throws ServiceException {
        try {
            // 1. 组装 prompt
            String prompt = "请审核以下商品信息是否合规（标题和描述），合规返回“通过”，不合规返回“不通过”，无法判断需要人工处理请返回“人工审核”：\n" +
                    "标题：" + message.getTitle() + "\n描述：" + message.getDescription() + "\n标签：" + message.getTags();

            // 2. 调用 DeepSeek
            String result = ollamaClient.chat(prompt);
            String reason = extractReason(result);
            String conclusion = extractConclusion(result);
            String cleanReason = reason.replaceAll("\\\\n", " ").replaceAll("\\n", " ");


            // 3. 初始化审核日志上下文（必须设置 targetId）
            AuditLogContext.setTargetId(message.getProductId());

            ProductReview review = new ProductReview();
            review.setProductId(message.getProductId());
            review.setReason(cleanReason);
            review.setType("AUTO");
            review.setReviewBy("系统审核");
            review.setCreatedAt(LocalDateTime.now());

            // 4. 根据审核结果处理
            if (conclusion.contains("通过")) {
                productMapper.updateStatus(message.getProductId(), 1);
                review.setStatus(1);
                AuditLogContext.setResult(1);
                AuditLogContext.setReason("审核通过");
            } else if (conclusion.contains("不通过")) {
                productMapper.updateStatus(message.getProductId(), 2);
                review.setStatus(2);
                AuditLogContext.setResult(2);
                AuditLogContext.setReason("审核不通过");
            } else if (conclusion.contains("人工审核")) {
                review.setStatus(0); // 未审核
                AuditLogContext.setResult(-1);
                AuditLogContext.setReason("需要人工审核");
            } else {
                throw new ServiceException("自动审核失败，返回结论无法识别");
            }

            // 5. 插入审核记录（不管通过、不通过还是人工审核都要记录）
            productReviewMapper.insert(review);

            log.info("商品审核完成，结果: {}", result);

        } catch (Exception e) {
            log.error("商品自动审核失败", e);
            throw new ServiceException("自动审核异常: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @AuditLog(value = "手动审核", type = "AUDIT")
    public void manualReview(ManualReviewRequest request) throws ServiceException {
        try {
            int userId = StpUtil.getLoginIdAsInt();
            if (userId == 0) {
                throw new ServiceException("无法获取当前登录用户");
            }

            User user = userMapper.selectById(userId);
            if (user == null) {
                throw new ServiceException("用户不存在");
            }

            if (request.getId() == null) {
                throw new ServiceException("审核记录ID不能为空");
            }

            ProductReview productReview = productReviewMapper.selectById(request.getId());
            if (productReview == null) {
                throw new ServiceException("审核记录不存在");
            }

            // 判断审核状态是否合法
            int status = request.getStatus();
            if (status != 1 && status != 2) {
                throw new ServiceException("非法审核状态");
            }

            // 更新商品状态（1=通过，2=失败）
            int updateCount = productMapper.updateStatus(productReview.getProductId(), status);
            if (updateCount == 0) {
                throw new ServiceException("更新商品状态失败");
            }

            productReview.setStatus(status);
            productReview.setType("AUDIT");
            productReview.setReviewBy(user.getUsername());
            productReview.setCreatedAt(LocalDateTime.now());
            productReview.setReason(
                    Optional.ofNullable(request.getReason()).orElse(status == 1 ? "审核通过" : "审核失败")
            );

            productReviewMapper.updateById(productReview);

            // 设置审核日志上下文
            AuditLogContext.setResult(status);
            AuditLogContext.setTargetId(productReview.getProductId());
            AuditLogContext.setReason(productReview.getReason());

            log.info("商品手动审核完成，商品ID: {}，结果: {}", productReview.getProductId(), status);
        } catch (Exception e) {
            log.error("商品手动审核失败：{}", e.getMessage(), e);
            throw new ServiceException("手动审核失败：" + e.getMessage());
        }
    }


    @Override
    public ProductReview getReview(Integer reviewId) throws ServiceException {
        try {
            ProductReview review = productReviewMapper.selectById(reviewId);
            if (review == null) {
                throw new ServiceException("地址不存在");
            }
            return review;
        } catch (Exception e) {
            throw new ServiceException("获取地址失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteReview(Integer reviewId) throws ServiceException {
        // 检查记录是否存在
        ProductReview review = productReviewMapper.selectById(reviewId);
        if (review == null) {
            throw new ServiceException("审核记录不存在");
        }

        try {
            productReviewMapper.deleteById(reviewId);
            return true;
        } catch (Exception e) {
            throw new ServiceException("审核记录删除失败：" + e.getMessage());
        }
    }

    @Override
    public IPage<ProductReview> getReviewPage(int page, int size) throws ServiceException {
        try {
            // 创建分页对象
            Page<ProductReview> pageParam = new Page<>(page, size);
            QueryWrapper<ProductReview> queryWrapper = new QueryWrapper<>();

            // 默认按创建时间倒序
            queryWrapper.orderByDesc("created_at");

            return productReviewMapper.selectPage(pageParam, queryWrapper);
        } catch (Exception e) {
            throw new ServiceException("获取审核列表失败：" + e.getMessage());
        }
    }

    @Override
    public IPage<ProductReview> searchReviewPage(int page, int size, String keyword) throws ServiceException {
        try {
            // 创建分页对象
            Page<ProductReview> pageParam = new Page<>(page, size);
            QueryWrapper<ProductReview> queryWrapper = new QueryWrapper<>();

            // 多字段模糊搜索
            if (StringUtils.hasText(keyword)) {
                queryWrapper.and(wrapper -> wrapper
                        .like("product_id", keyword)
                        .or().like("type", keyword)
                );
            }

            // 默认按创建时间倒序
            queryWrapper.orderByDesc("created_at");

            return productReviewMapper.selectPage(pageParam, queryWrapper);
        } catch (Exception e) {
            throw new ServiceException("搜索审核记录失败：" + e.getMessage());
        }
    }


    private String extractReason(String result) {
        Pattern pattern = Pattern.compile("<think>([\\s\\S]*?)</think>");
        Matcher matcher = pattern.matcher(result);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return "未提供原因";
    }

    private String extractConclusion(String result) {
        // 移除 <think> 标签内容后的剩余部分
        return result.replaceAll("<think>[\\s\\S]*?</think>", "").trim();
    }
}

