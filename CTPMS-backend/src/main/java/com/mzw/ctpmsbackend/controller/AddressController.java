/*
 * @Author: David Ma
 * @FilePath: \campus-trading-platform-management-system\CTPMS-backend\src\main\java\com\mzw\ctpmsbackend\controller\AddressController.java
 * @LastEditors: David Ma
 * @Description: 该文件用于...
 * @Date: 2025-03-22 13:08:03
 */
package com.mzw.ctpmsbackend.controller;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mzw.ctpmsbackend.annotation.OperationLog;
import com.mzw.ctpmsbackend.common.utils.DataResult;
import com.mzw.ctpmsbackend.dto.AddressDTO;
import com.mzw.ctpmsbackend.entity.Address;
import com.mzw.ctpmsbackend.exception.ServiceException;
import com.mzw.ctpmsbackend.service.AddressService;
import cn.dev33.satoken.annotation.SaCheckLogin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

/**
 * 地址控制器
 */
@RestController
@RequestMapping("/api/address")
@Api(tags = "地址管理")
@SaCheckLogin
public class AddressController {

    @Resource
    private AddressService addressService;

    @PostMapping
    @ApiOperation("添加地址")
    @OperationLog(type = "ADDRESS", value = "添加地址")
    public DataResult<String> addAddress(@RequestBody AddressDTO addressDTO) {
        try {
            Integer loginUserId = StpUtil.getLoginIdAsInt();
            // 确保添加的地址属于当前登录用户
            if (!loginUserId.equals(addressDTO.getUserId())) {
                return DataResult.error("只能添加自己的地址");
            }
            String result = addressService.addAddress(addressDTO);
            return DataResult.success(result);
        } catch (ServiceException e) {
            return DataResult.error(e.getMessage());
        }
    }


    @DeleteMapping("/{addressId}")
    @ApiOperation("删除地址")
    @OperationLog(type = "ADDRESS", value = "删除地址")
    public DataResult<String> deleteAddress(@PathVariable Integer addressId) {
        try {
            Integer loginUserId = StpUtil.getLoginIdAsInt();
            Address address = addressService.getAddress(addressId);
            // 确保删除的地址属于当前登录用户
            if (!loginUserId.equals(address.getUserId())) {
                return DataResult.error("只能删除自己的地址");
            }
            addressService.deleteAddress(addressId);
            return DataResult.success("地址删除成功");
        } catch (ServiceException e) {
            return DataResult.error(e.getMessage());
        }
    }

    @PutMapping
    @ApiOperation("修改地址")
    @OperationLog(type = "ADDRESS", value = "修改地址")
    public DataResult<String> updateAddress(@RequestBody AddressDTO addressDTO) {
        try {
            Integer loginUserId = StpUtil.getLoginIdAsInt();
            // 确保修改的地址属于当前登录用户
            if (!loginUserId.equals(addressDTO.getUserId())) {
                return DataResult.error("只能修改自己的地址");
            }
            addressService.updateAddress(addressDTO);
            return DataResult.success("地址更新成功");
        } catch (ServiceException e) {
            return DataResult.error(e.getMessage());
        }
    }

    @GetMapping("/{addressId}")
    @ApiOperation("获取地址详情")
    public DataResult<Address> getAddress(@PathVariable Integer addressId) {
        try {
            Integer loginUserId = StpUtil.getLoginIdAsInt();
            Address address = addressService.getAddress(addressId);
            // 确保查询的地址属于当前登录用户
            if (!loginUserId.equals(address.getUserId())) {
                return DataResult.error("只能查看自己的地址");
            }
            return DataResult.success(address);
        } catch (ServiceException e) {
            return DataResult.error(e.getMessage());
        }
    }

    @GetMapping("/list")
    @ApiOperation("获取用户地址列表")
    public DataResult<IPage<Address>> listAddresses(
            @RequestParam Integer uid,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        try {
            Integer loginUserId = StpUtil.getLoginIdAsInt();
            // 确保查询的是当前登录用户的地址
            if (!loginUserId.equals(uid)) {
                return DataResult.error("只能查看自己的地址列表");
            }
            IPage<Address> result = addressService.getAddressListByUserId(page, size, uid);
            return DataResult.success(result);
        } catch (ServiceException e) {
            return DataResult.error(e.getMessage());
        }
    }

    @GetMapping("/search")
    @ApiOperation("模糊搜索用户地址")
    public DataResult<IPage<Address>> searchAddresses(
            @RequestParam Integer uid,
            @RequestParam String keyword,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        try {
            Integer loginUserId = StpUtil.getLoginIdAsInt();
            // 确保搜索的是当前登录用户的地址
            if (!loginUserId.equals(uid)) {
                return DataResult.error("只能搜索自己的地址");
            }
            IPage<Address> result = addressService.searchAddressList(page, size, uid, keyword);
            return DataResult.success(result);
        } catch (ServiceException e) {
            return DataResult.error(e.getMessage());
        }
    }

    @PutMapping("/default/{addressId}")
    @ApiOperation("设置默认地址")
    @OperationLog(type = "ADDRESS", value = "设置默认地址")
    public DataResult<String> setDefaultAddress(@PathVariable Integer addressId) {
        try {
            Integer loginUserId = StpUtil.getLoginIdAsInt();
            Address address = addressService.getAddress(addressId);
            // 确保设置的是当前登录用户的地址
            if (!loginUserId.equals(address.getUserId())) {
                return DataResult.error("只能设置自己的默认地址");
            }
            addressService.setDefaultAddress(addressId);
            return DataResult.success("默认地址设置成功");
        } catch (ServiceException e) {
            return DataResult.error(e.getMessage());
        }
    }
}

