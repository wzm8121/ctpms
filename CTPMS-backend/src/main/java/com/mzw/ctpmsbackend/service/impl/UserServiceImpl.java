package com.mzw.ctpmsbackend.service.impl;


import com.mzw.ctpmsbackend.common.utils.EncryptUtil;
import com.mzw.ctpmsbackend.exception.ServiceException;
import com.mzw.ctpmsbackend.service.ImageUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.util.MultiValueMap;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Map;
import org.springframework.web.client.RestClientException;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mzw.ctpmsbackend.common.utils.CacheClient;
import com.mzw.ctpmsbackend.common.utils.DataResult;
import com.mzw.ctpmsbackend.entity.User;
import com.mzw.ctpmsbackend.mapper.UserMapper;
import com.mzw.ctpmsbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private CacheClient cacheClient;

    @Resource
    private JavaMailSender mailSender;

    @Value("${mzw.password-key}")
    private String passwordKey;

    private static final String VERIFICATION_CODE_PREFIX = "verification_code:";
    private static final int CODE_EXPIRE_MINUTES = 5;

    @Resource
    private ImageUploadService imageUploadService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer addUser(User user) throws ServiceException{
        try {

            if (userMapper.selectByPhone(user.getPhone()) != null) {
                throw new ServiceException("该手机号已被注册");
            }
            if (userMapper.selectByEmail(user.getEmail()) != null) {
                throw new ServiceException("该邮箱已被注册");
            }

            String studentId;
            do {
                studentId = generateStudentId();
            } while (userMapper.selectByStudentId(studentId) != null);

            user.setStudentId(studentId);

            String salt = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
            String finalKey = passwordKey + salt;
            String ciphertext = EncryptUtil.encrypt(finalKey, user.getPasswordHash());

            user.setSalt(salt);
            user.setPasswordHash(ciphertext);
            user.setCreatedAt(LocalDateTime.now());
            user.setUpdatedAt(LocalDateTime.now());

            userMapper.insert(user);
            log.info("用户添加成功: {}", user.getUserId());
            return user.getUserId();
        } catch (Exception e) {
            log.error("添加用户失败", e);
            throw new ServiceException("添加用户失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(Long userId) throws ServiceException{
        if (userId == null) {
            throw new ServiceException("用户ID不能为空");
        }

        try {
            User user = userMapper.selectById(userId);
            if (user == null) {
                throw new ServiceException("用户不存在");
            }

            int rows = userMapper.deleteById(userId);
            if (rows == 0) {
                throw new ServiceException("删除用户失败");
            }
            log.info("用户删除成功: {}", userId);
        } catch (Exception e) {
            log.error("删除用户失败: {}", userId, e);
            throw new ServiceException("删除用户失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(User user) throws ServiceException{
        try {
            User user1 = userMapper.selectById(user.getUserId());
            if (user1 == null) {
                throw new ServiceException("用户不存在");
            }
            // 如果需要更改密码
            if (user.getPasswordHash() != null) {
                String salt = user1.getSalt();
                String ciphertext = EncryptUtil.encrypt(user.getPasswordHash(), salt);
                user.setPasswordHash(ciphertext);
            }
            userMapper.updateById(user);
            log.info("用户信息更新成功: {}", user.getUserId());
        } catch (Exception e) {
            log.error("更新用户信息失败: {}", user.getUserId(), e);
            throw new ServiceException("更新用户失败", e);
        }
    }

    @Override
    public User getUserById(Integer userId) throws ServiceException{
        try {
            User user = userMapper.selectById(userId);
            if (user == null) {
                throw new ServiceException("用户不存在");
            }
            return user;
        } catch (Exception e) {
            log.error("查询用户失败: {}", userId, e);
            throw new ServiceException("查询用户失败", e);
        }
    }

    @Override
    public IPage<User> getUserList(int page, int size) throws ServiceException{
        try {
            return userMapper.selectPage(new Page<>(page, size), null);
        } catch (Exception e) {
            log.error("分页查询用户列表失败", e);
            throw new ServiceException("分页查询失败", e);
        }
    }

    @Override
    public IPage<User> searchUsers(int page, int size, String keyword) throws ServiceException{
        try {
            QueryWrapper<User> query = new QueryWrapper<>();
            if (keyword != null && !keyword.trim().isEmpty()) {
                query.like("user_id", keyword)
                        .or().like("real_name", keyword)
                        .or().like("phone", keyword);
            }
            return userMapper.selectPage(new Page<>(page, size), query);
        } catch (Exception e) {
            log.error("搜索用户失败: {}", keyword, e);
            throw new ServiceException("搜索用户失败", e);
        }
    }

    @Override
    public void sendVerificationCode(String email) throws ServiceException{
        try {
            if (email == null || email.trim().isEmpty()) {
                throw new ServiceException("邮箱不能为空");
            }

            if (userMapper.selectByEmail(email) != null) {
                throw new ServiceException("该邮箱已被注册");
            }

            String code = generateVerificationCode();
            cacheClient.set(VERIFICATION_CODE_PREFIX + email, code, (long) CODE_EXPIRE_MINUTES, TimeUnit.MINUTES);
            sendEmail(email, "您的验证码", "您的验证码是：" + code + "，5分钟内有效");

            log.info("验证码发送成功: {}", email);
        } catch (Exception e) {
            log.error("发送验证码失败: {}", email, e);
            throw new ServiceException("发送验证码失败", e);
        }
    }

    @Override
    public Integer register(User user, String code) throws ServiceException{
        try {
            String cachedCode = cacheClient.get(VERIFICATION_CODE_PREFIX + user.getEmail());
            if (cachedCode == null || !cachedCode.equals(code)) {
                throw new ServiceException("验证码错误或已过期");
            }
            
            String studentId;
            do {
                studentId = generateStudentId();
            } while (userMapper.selectByStudentId(studentId) != null);

            user.setStudentId(studentId);

            if (userMapper.selectByPhone(user.getPhone()) != null) {
                throw new ServiceException("该手机号已被注册");
            }

            String salt = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
            String ciphertext = EncryptUtil.encrypt(salt, user.getPasswordHash());
            user.setSchoolId(1);
            user.setIsSeller(0);
            user.setStatus(1);
            user.setRole("user");
            user.setAvatarUrl("http://123.60.139.233:40027/i/2025/03/18/67d903f4919da.jpg");
            user.setUserReputation(100);
            user.setCreatedAt(LocalDateTime.now());
            user.setUpdatedAt(LocalDateTime.now());
            user.setUserVerified(0);
            user.setFaceVerified(0);
            user.setPasswordHash(ciphertext);
            user.setSalt(salt);
            userMapper.insert(user);

            sendEmail(user.getEmail(), "注册成功", "您已成功注册校园交易平台");

            log.info("用户注册成功: {}", user.getUserId());
            return user.getUserId();
        } catch (Exception e) {
            log.error("用户注册失败: {}", user.getEmail(), e);
            throw new ServiceException("注册失败", e);
        }
    }

    @Override
    public String uploadAvatar(MultipartFile file, Integer userId) throws ServiceException{
        if (userId == null || file == null || file.isEmpty()) {
            throw new ServiceException("参数错误");
        }

        try {
            User user = userMapper.selectById(userId);
            if (user == null) {
                throw new ServiceException("用户不存在");
            }

            String avatarUrl = imageUploadService.uploadImage(file);
            user.setAvatarUrl(avatarUrl);
            userMapper.updateById(user);

            log.info("头像上传成功: {}", userId);
            return avatarUrl;
        } catch (Exception e) {
            log.error("上传头像失败: {}", userId, e);
            throw new ServiceException("上传头像失败", e);
        }
    }

    private String generateVerificationCode() {
        return String.format("%06d", new Random().nextInt(999999));
    }

    private String generateStudentId() {
        return "6321" + (10000000 + new Random().nextInt(90000000));
    }

    private void sendEmail(String to, String subject, String text) throws ServiceException{
        try {
            if (to == null || to.trim().isEmpty()) {
                throw new ServiceException("邮箱不能为空");
            }

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("yunlu29817@163.com");
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            mailSender.send(message);
        } catch (Exception e) {
            log.error("邮件发送失败: {}", to, e);
            throw new ServiceException("邮件发送失败", e);
        }
    }


}

