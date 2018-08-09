package com.imooc.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @Description 按类目展示商品  （vo注要用于页面上需要显示的数据）
 * @Author GuanHuizhen
 * @Date 2018/8/2
 */
@Data
public class CategoryProductVo {
    @JsonProperty("name")
    private String categoryName;//商品类目名称

    @JsonProperty("type")
    private Integer categoryType;//商品类型

    @JsonProperty("foods")
    private List<ProductInfoVo> productInfoVoList;//类目下的商品列表
}
