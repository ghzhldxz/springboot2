package com.imooc.service;

import com.imooc.dataobject.ProductInfo;
import com.imooc.dto.CartDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {

    ProductInfo findOne(String productId);

    List<ProductInfo> findByProductIds(List<String> productIds);

    /**
     * 查询上架的商品
     * @return
     */
    List<ProductInfo> findUpAll();

    Page<ProductInfo> findAll(Pageable pageable);

    ProductInfo save(ProductInfo productInfo);

    //加库存
    void addStock(List<CartDto> cartDtoList);

    //减库存
    void deductStock(List<CartDto> cartDtoList);
}
