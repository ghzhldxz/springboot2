package com.imooc.service.impl;

import com.imooc.dataobject.ProductInfo;
import com.imooc.dto.CartDto;
import com.imooc.enums.ProductStatusEnum;
import com.imooc.enums.ResultEnum;
import com.imooc.exception.GlobalException;
import com.imooc.repository.ProductInfoRepository;
import com.imooc.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description 商品
 * @Author GuanHuizhen
 * @Date 2018/8/1
 */
@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductInfoRepository productInfoRepository;

    @Override
    public ProductInfo findOne(String productId) {
        return productInfoRepository.findOne(productId);
    }

    @Override
    public List<ProductInfo> findByProductIds(List<String> productIds) {
        return productInfoRepository.findByProductIdIn(productIds);
    }

    @Override
    public List<ProductInfo> findUpAll() {
        return productInfoRepository.findByProductStatus(ProductStatusEnum.UP.getCode());
    }

    @Override
    public Page<ProductInfo> findAll(Pageable pageable) {
        return productInfoRepository.findAll(pageable);
    }

    @Override
    public ProductInfo save(ProductInfo productInfo) {
        return productInfoRepository.save(productInfo);
    }

    /**
     * 觉得这里设计有点本末倒置了，应该是先有购物车，然后才有订单
     * 而不是先有订单，然后再组装成购物车列表的形式，有点奇怪
     * @param cartDtoList
     */
    @Transactional
    @Override
    public void addStock(List<CartDto> cartDtoList) {
        if(cartDtoList == null) {
            return ;
        }
        for(CartDto cartDto : cartDtoList ) {
            ProductInfo productInfo = productInfoRepository.findOne(cartDto.getProductId());
            if(productInfo == null ){
                throw new GlobalException(ResultEnum.CART_EMPTY);
            }
            int newStock = productInfo.getProductStock() + cartDto.getProductQuantity();
            productInfo.setProductStock(newStock);
            productInfoRepository.save(productInfo);
        }

    }

    @Transactional
    @Override
    public void deductStock(List<CartDto> cartDtoList) {
        if(cartDtoList == null && cartDtoList.isEmpty()) {
            return ;
        }
        cartDtoList.forEach(e-> {
            ProductInfo productInfo = productInfoRepository.findOne(e.getProductId());
            if(productInfo == null ){
                throw new GlobalException(ResultEnum.CART_EMPTY);
            }

            int newStock = productInfo.getProductStock() - e.getProductQuantity();
            if(newStock<0) {
                throw new GlobalException(ResultEnum.PRODUCT_STOCK_ERROR);
            }
            productInfo.setProductStock(newStock);
            productInfoRepository.save(productInfo);
        });

    }
}
