package com.imooc.dto;

import lombok.Data;

/**
 * @Description 购物车
 * @Author GuanHuizhen
 * @Date 2018/8/3
 */
@Data
public class CartDto {
    private String productId;
    private Integer productQuantity;
    public CartDto(String productId, Integer productQuantity) {
        this.productId = productId;
        this.productQuantity = productQuantity;
    }
}
