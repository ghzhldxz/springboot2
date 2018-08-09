package com.imooc.repository;

import com.imooc.dataobject.ProductInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductInfoRepositoryTest {
    @Autowired
    ProductInfoRepository productInfoRepository;

    @Test
    public void findByProductIdIn() {
        List<String> productIds = Arrays.asList("10001","10002");
        List<ProductInfo> productInfos = productInfoRepository.findByProductIdIn(productIds);
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>"+productIds.size()+">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }
}