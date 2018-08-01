package com.imooc.repository;

import com.imooc.dataobject.ProductCategory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductCategoryRepositoryTest {
    @Autowired
    ProductCategoryRepository productCategoryRepository;

    @Test
    @Transactional  //测试中，通过后回滚，这样测试数据就不会插入到数据库中
    public void testFind() {
        List<Integer> paramList = new ArrayList<Integer>();
        paramList.add(10);
        List<ProductCategory> result = productCategoryRepository.findByCategoryTypeIn(paramList);
        result.get(0);
    }

}