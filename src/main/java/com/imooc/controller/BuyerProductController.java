package com.imooc.controller;

import com.imooc.dataobject.ProductCategory;
import com.imooc.dataobject.ProductInfo;
import com.imooc.service.CategoryService;
import com.imooc.service.ProductService;
import com.imooc.service.impl.ProductServiceImpl;
import com.imooc.vo.CategoryProductVo;
import com.imooc.vo.ProductInfoVo;
import com.imooc.vo.ResultVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description 买家商品
 * @Author GuanHuizhen
 * @Date 2018/8/1
 */
@RestController
@RequestMapping("/buyer/product")
public class BuyerProductController {
    @Autowired
    ProductService productService;
    @Autowired
    CategoryService categoryService;

    @RequestMapping("/list")
    public ResultVo list() {

        List<ProductInfo> productInfoList = productService.findUpAll();//查询上架的商品信息
        List<ProductCategory> productCategoryList = categoryService.findAll();

        List<CategoryProductVo> data = new ArrayList<CategoryProductVo>();

        for(ProductCategory productCategory : productCategoryList) {
            CategoryProductVo productVo = new CategoryProductVo();
            productVo.setCategoryName(productCategory.getCategoryName());
            productVo.setCategoryType(productCategory.getCategoryType());
            List<ProductInfoVo> productInfoVoList = new ArrayList<ProductInfoVo>();
            for(ProductInfo productInfo : productInfoList) {
                if(productInfo.getCategoryType() == productCategory.getCategoryType()) {
                    ProductInfoVo tempVo = new ProductInfoVo();
                    BeanUtils.copyProperties(productInfo,tempVo);
                    productInfoVoList.add(tempVo);
                }
            }
            productVo.setProductInfoVoList(productInfoVoList);
            data.add(productVo);
        }
        return ResultVo.success(data);
    }


}
