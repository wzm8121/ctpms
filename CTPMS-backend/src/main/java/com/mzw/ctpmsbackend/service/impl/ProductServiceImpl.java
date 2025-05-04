/*
 * @Author: David Ma
 * @FilePath: \campus-trading-platform-management-system\CTPMS-backend\src\main\java\com\mzw\ctpmsbackend\service\impl\ProductServiceImpl.java
 * @LastEditors: David Ma
 * @Description: 该文件用于...
 * @Date: 2025-02-17 14:16:18
 */
package com.mzw.ctpmsbackend.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mzw.ctpmsbackend.dto.ProductCreateDTO;
import com.mzw.ctpmsbackend.dto.ProductDTO;
import com.mzw.ctpmsbackend.dto.ProductReviewMessage;
import com.mzw.ctpmsbackend.entity.Product;
import com.mzw.ctpmsbackend.entity.ProductImage;
import com.mzw.ctpmsbackend.exception.ServiceException;
import com.mzw.ctpmsbackend.mapper.ProductImageMapper;
import com.mzw.ctpmsbackend.mapper.ProductMapper;
import com.mzw.ctpmsbackend.rabbitmq.ProductMessageSender;
import com.mzw.ctpmsbackend.service.ProductImageService;
import com.mzw.ctpmsbackend.service.ProductService;
import com.mzw.ctpmsbackend.vo.ProductImageVO;
import com.mzw.ctpmsbackend.vo.ProductVO;
import lombok.extern.slf4j.Slf4j;
import org.nd4j.common.io.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    private final ProductMapper productMapper;


    @Resource
    private ProductImageMapper productImageMapper;
    @Autowired
    private ProductMessageSender productMessageSender;
    @Autowired
    private ProductImageService productImageService;

    public ProductServiceImpl(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createProduct(ProductCreateDTO dto) throws ServiceException {
        try {
            Integer UserId = StpUtil.getLoginIdAsInt();
            // 1. 保存商品
            Product product = new Product();
            BeanUtils.copyProperties(dto, product);
            product.setUserId(UserId);
            product.setStatus(0); // 未审核
            this.save(product);

            // 2. 保存图片
            if (dto.getImages() != null && !dto.getImages().isEmpty()) {
                List<ProductImage> images = dto.getImages().stream().map(imageDTO -> {
                    ProductImage image = new ProductImage();
                    image.setProductId(product.getProductId());
                    image.setImageUrl(imageDTO.getImageUrl());
                    image.setIsMain(imageDTO.getIsMain() != 1 ? 0 : 1);
                    return image;
                }).collect(Collectors.toList());

                System.out.println("测试插入图片中");
                productImageService.saveBatch(images);
            }

            // 3. 发送 RabbitMQ 审核消息
            ProductReviewMessage message = new ProductReviewMessage();
            message.setProductId(product.getProductId());
            message.setTitle(product.getTitle());
            message.setDescription(product.getDescription());
            productMessageSender.sendProductForReview(message); // 发送审核消息

        } catch (Exception e) {
            log.error("添加商品失败", e);
            throw new ServiceException("添加商品失败: " + e.getMessage());
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteProduct(Integer id) throws ServiceException {
        try {
            // 参数校验
            if (id == null) {
                throw new ServiceException("商品ID不能为空");
            }

            // 1. 检查商品是否存在
            Product product = productMapper.selectById(id);
            if (product == null) {
                throw new ServiceException("商品不存在");
            }

            // 2. 修改状态为 "已下架"（而不是物理删除）
            product.setStatus(2); // 2表示已下架
            productMapper.updateById(product);

            log.info("商品删除成功，ID: {}", id);
            return true;
        } catch (Exception e) {
            log.error("商品删除失败，ID: {}", id, e);
            throw new ServiceException("商品删除失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Product updateProduct(ProductDTO productDTO) throws ServiceException {
        try {
            // 参数校验
            if (productDTO == null || productDTO.getProductId() == null) {
                throw new ServiceException("商品信息不完整");
            }

            // 检查商品是否存在
            Product existingProduct = this.getById(productDTO.getProductId());
            if (existingProduct == null) {
                throw new ServiceException("商品不存在");
            }

            // 转换实体
            Product product = convertToEntity(productDTO);
            product.setUpdatedAt(LocalDateTime.now());

            // 更新商品
            if (!this.updateById(product)) {
                throw new ServiceException("商品更新失败");
            }

            log.info("商品更新成功，ID: {}", product.getProductId());
            return product;
        } catch (Exception e) {
            log.error("商品更新失败: {}", e.getMessage());
            throw new ServiceException("商品更新失败: " + e.getMessage());
        }
    }

    @Override
    public ProductVO getProduct(Integer id) throws ServiceException {
        try {
            if (id == null) {
                throw new ServiceException("商品ID不能为空");
            }

            Product product = this.getById(id);
            if (product == null) {
                throw new ServiceException("商品不存在");
            }

            // 转换为包含图片的 VO 对象
            return convertToProductVO(product);

        } catch (Exception e) {
            log.error("获取商品失败，ID: {}", id, e);
            throw new ServiceException("获取商品失败: " + e.getMessage());
        }
    }

    @Override
    public IPage<ProductVO> getProductList(int page, int size) throws ServiceException {
        try {
            if (page < 1) page = 1;
            if (size < 1 || size > 100) size = 10;

            Page<Product> productPage = this.page(
                    new Page<>(page, size),
                    new QueryWrapper<Product>().orderByDesc("created_at")
            );

            List<ProductVO> productVOList = convertToProductVOList(productPage.getRecords());

            return new Page<ProductVO>()
                    .setRecords(productVOList)
                    .setCurrent(productPage.getCurrent())
                    .setSize(productPage.getSize())
                    .setTotal(productPage.getTotal())
                    .setPages(productPage.getPages());

        } catch (Exception e) {
            log.error("获取商品列表失败", e);
            throw new ServiceException("获取商品列表失败: " + e.getMessage());
        }
    }

    @Override
    public IPage<ProductVO> searchProducts(int page, int size, String keyword) throws ServiceException {
        try {
            if (page < 1) page = 1;
            if (size < 1 || size > 100) size = 10;

            QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
            if (StringUtils.hasText(keyword)) {
                if (keyword.length() > 50) {
                    throw new ServiceException("搜索关键词过长");
                }
                queryWrapper.like("title", keyword)
                        .or().like("description", keyword)
                        .or().eq("product_id", keyword);
            }

            Page<Product> productPage = this.page(
                    new Page<>(page, size),
                    queryWrapper.orderByDesc("created_at")
            );

            List<ProductVO> productVOList = convertToProductVOList(productPage.getRecords());

            return new Page<ProductVO>()
                    .setRecords(productVOList)
                    .setCurrent(productPage.getCurrent())
                    .setSize(productPage.getSize())
                    .setTotal(productPage.getTotal())
                    .setPages(productPage.getPages());

        } catch (Exception e) {
            log.error("搜索商品失败", e);
            throw new ServiceException("搜索商品失败: " + e.getMessage());
        }
    }




    private List<ProductImageVO> getImagesByProductId(Integer productId) {
        return productImageMapper.selectList(
                new QueryWrapper<ProductImage>().eq("product_id", productId)
        ).stream().map(img -> {
            ProductImageVO imageVO = new ProductImageVO();
            imageVO.setImageId(img.getImageId());
            imageVO.setImageUrl(img.getImageUrl());
            imageVO.setIsMain(img.getIsMain() != null && img.getIsMain() == 1);
            return imageVO;
        }).collect(Collectors.toList());
    }

    private List<ProductVO> convertToProductVOList(List<Product> products) {
        return products.stream().map(product -> {
            ProductVO vo = new ProductVO();
            BeanUtils.copyProperties(product, vo);
            vo.setImages(getImagesByProductId(product.getProductId()));
            return vo;
        }).collect(Collectors.toList());
    }

    private ProductVO convertToProductVO(Product product) {
        ProductVO vo = new ProductVO();
        BeanUtils.copyProperties(product, vo);
        vo.setImages(getImagesByProductId(product.getProductId()));
        return vo;
    }



    /**
     * DTO 转 Entity 方法
     */
    private Product convertToEntity(ProductDTO dto) {
        Product product = new Product();
        BeanUtils.copyProperties(dto, product);
        return product;
    }
}

