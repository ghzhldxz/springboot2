package com.imooc.dataobject;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * 类目
 * Created by 廖师兄
 * 2017-05-07 14:30
 */
@Entity
@DynamicUpdate  //动态更新updateTime，但是若更新的内容跟数据库中的一致，则会优化，不做更新
@Data     //用此注解可以不用在代码中写属性的setter和getter方法，在打包的时候会自动生成
public class ProductCategory {

    /** 类目id. */
    @Id
    @GeneratedValue
    private Integer categoryId;

    /** 类目名字. */
    private String categoryName;

    /** 类目编号. */
    private Integer categoryType;

    private Date createTime;

    private Date updateTime;

    public ProductCategory() {
    }

    public ProductCategory(String categoryName, Integer categoryType) {
        this.categoryName = categoryName;
        this.categoryType = categoryType;
    }

}
