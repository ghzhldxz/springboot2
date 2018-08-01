package com.imooc.repository;

import com.imooc.dataobject.ProductCategory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 因为继承了JpaRepository，相当于继承Repository,所以这个对象会被spring容器实例化为一个实体
 * Created by 廖师兄
 * 2017-05-07 14:35
 */
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Integer> {

    List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList);

}
