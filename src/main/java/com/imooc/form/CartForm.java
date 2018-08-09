package com.imooc.form;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * @Description 前端购物车
 * @Author GuanHuizhen
 * @Date 2018/8/8
 */
@Data
public class CartForm {
    @NotBlank(message = "订单明细商品信息异常")
    private String productId;

    @NotNull(message = "订单明细商品信息异常")
    private Integer productQuantity;
}
