package com.imooc.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.imooc.enums.ProductStatusEnum;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Description 前端展示商品信息
 * @Author GuanHuizhen
 * @Date 2018/8/2
 */
@Data
public class ProductInfoVo {

    @JsonProperty("id")
    private String productId;

    /** 名字. */
    @JsonProperty("name")
    private String productName;

    /** 单价. */
    @JsonProperty("price")
    private BigDecimal productPrice;

    /** 描述. */
    @JsonProperty("description")
    private String productDescription;

    /** 小图. */
    @JsonProperty("icon")
    private String productIcon;

    /** 类目编号. */
    private Integer categoryType;

}
