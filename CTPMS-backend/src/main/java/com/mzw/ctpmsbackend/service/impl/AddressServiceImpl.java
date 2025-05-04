package com.mzw.ctpmsbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mzw.ctpmsbackend.dto.AddressDTO;
import com.mzw.ctpmsbackend.entity.Address;
import com.mzw.ctpmsbackend.entity.User;
import com.mzw.ctpmsbackend.exception.ServiceException;
import com.mzw.ctpmsbackend.mapper.AddressMapper;
import com.mzw.ctpmsbackend.mapper.UserMapper;
import com.mzw.ctpmsbackend.service.AddressService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;


@Service
public class AddressServiceImpl implements AddressService {

    @Resource
    private AddressMapper addressMapper;

    @Resource
    private UserMapper userMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String addAddress(AddressDTO addressDTO) throws ServiceException {
        // 参数校验
        if (addressDTO == null || !StringUtils.hasText(addressDTO.getAddress())) {
            throw new ServiceException("地址信息不能为空");
        }

        // 检查用户是否存在
        User user = userMapper.selectById(addressDTO.getUserId());
        if (user == null) {
            throw new ServiceException("用户不存在");
        }

        try {
            Address address = new Address();
            BeanUtils.copyProperties(addressDTO, address);
            addressMapper.insert(address);
            return "地址添加成功";
        } catch (Exception e) {
            throw new ServiceException("地址添加失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAddress(Integer addressId) throws ServiceException {
        // 检查地址是否存在
        Address address = addressMapper.selectById(addressId);
        if (address == null) {
            throw new ServiceException("地址不存在");
        }

        try {
            addressMapper.deleteById(addressId);
        } catch (Exception e) {
            throw new ServiceException("地址删除失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAddress(AddressDTO addressDTO) throws ServiceException {
        // 参数校验
        if (addressDTO == null || addressDTO.getAddressId() == null) {
            throw new ServiceException("地址信息不能为空");
        }

        // 检查地址是否存在
        Address existingAddress = addressMapper.selectById(addressDTO.getAddressId());
        if (existingAddress == null) {
            throw new ServiceException("地址不存在");
        }

        try {
            Address address = new Address();
            BeanUtils.copyProperties(addressDTO, address);
            addressMapper.updateById(address);
        } catch (Exception e) {
            throw new ServiceException("地址更新失败：" + e.getMessage());
        }
    }

    @Override
    public Address getAddress(Integer addressId) throws ServiceException {
        try {
            Address address = addressMapper.selectById(addressId);
            if (address == null) {
                throw new ServiceException("地址不存在");
            }
            return address;
        } catch (Exception e) {
            throw new ServiceException("获取地址失败：" + e.getMessage());
        }
    }

    @Override
    public IPage<Address> getAddressListByUserId(int page, int size, Integer uid) throws ServiceException {
        try {
            // 创建分页对象
            Page<Address> pageParam = new Page<>(page, size);
            QueryWrapper<Address> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", uid);
            return addressMapper.selectPage(pageParam, queryWrapper);
        } catch (Exception e) {
            throw new ServiceException("获取地址列表失败：" + e.getMessage());
        }
    }

    @Override
    public IPage<Address> searchAddressList(int page, int size, Integer uid, String keyword) throws ServiceException {
        try {
            // 创建分页对象
            Page<Address> pageParam = new Page<>(page, size);
            QueryWrapper<Address> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", uid);

            // 多字段模糊查询
            if (StringUtils.hasText(keyword)) {
                queryWrapper.and(wrapper -> wrapper
                        .like("province", keyword)
                        .or().like("city", keyword)
                        .or().like("district", keyword)
                        .or().like("address", keyword)
                );
            }

            // 默认按更新时间倒序
            queryWrapper.orderByDesc("update_time");

            return addressMapper.selectPage(pageParam, queryWrapper);
        } catch (Exception e) {
            throw new ServiceException("获取地址列表失败：" + e.getMessage());
        }
    }

    @Override
    public void setDefaultAddress(Integer addressId) throws ServiceException {
        // 检查地址是否存在
        Address address = addressMapper.selectById(addressId);
        if (address == null) {
            throw new ServiceException("地址不存在");
        }

        try {
            // 先取消当前用户的默认地址
            addressMapper.update(null, new UpdateWrapper<Address>()
                    .eq("user_id", address.getUserId())
                    .set("address_status", 1));

            // 设置新的默认地址
            address.setAddressStatus(2);
            addressMapper.updateById(address);
        } catch (Exception e) {
            throw new ServiceException("默认地址设置失败：" + e.getMessage());
        }
    }
}
