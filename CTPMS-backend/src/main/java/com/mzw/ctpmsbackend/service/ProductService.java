/*
 * @Author: David Ma
 * @FilePath: \campus-trading-platform-management-system\CTPMS-backend\src\main\java\com\mzw\ctpmsbackend\service\ProductService.java
 * @LastEditors: David Ma
 * @Description: 该文件用于...
 * @Date: 2025-02-17 14:15:10
 */
package com.mzw.ctpmsbackend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mzw.ctpmsbackend.dto.ProductCreateDTO;
import com.mzw.ctpmsbackend.dto.ProductDTO;
import com.mzw.ctpmsbackend.entity.Product;
import com.mzw.ctpmsbackend.exception.ServiceException;
import com.mzw.ctpmsbackend.vo.ProductVO;
import org.springframework.stereotype.Service;

/**
 * 商品服务接口
 * 提供商品的增删改查及搜索功能
 */
public interface ProductService {

    /**
     * 添加商品
     * @param dto 商品信息传输对象
     * @return 添加成功的商品信息
     * @throws ServiceException 当添加失败或参数无效时抛出
     */
    void createProduct(ProductCreateDTO dto) throws ServiceException;

    /**
     * 删除商品
     * @param id 商品ID
     * @return 是否删除成功
     * @throws ServiceException 当商品不存在或删除失败时抛出
     */
    boolean deleteProduct(Integer id) throws ServiceException;

    /**
     * 更新商品信息
     * @param productDTO 商品信息传输对象（需包含商品ID）
     * @return 更新后的商品信息
     * @throws ServiceException 当商品不存在或更新失败时抛出
     */
    Product updateProduct(ProductDTO productDTO) throws ServiceException;

    /**
     * 根据ID获取商品详情
     * @param id 商品ID
     * @return 商品信息
     * @throws ServiceException 当商品不存在或查询失败时抛出
     */
    ProductVO getProduct(Integer id) throws ServiceException;

    /**
     * 分页获取商品列表
     * @param page 页码（从1开始）
     * @param size 每页数量
     * @return 分页商品列表
     * @throws ServiceException 当查询失败时抛出
     */
    IPage<ProductVO> getProductList(int page, int size) throws ServiceException;

    /**
     * 搜索商品
     * @param page 页码（从1开始）
     * @param size 每页数量
     * @param keyword 搜索关键词（可匹配商品名称、描述等字段）
     * @return 分页搜索结果
     * @throws ServiceException 当查询失败时抛出
     */
    IPage<ProductVO> searchProducts(int page, int size, String keyword) throws ServiceException;


}
