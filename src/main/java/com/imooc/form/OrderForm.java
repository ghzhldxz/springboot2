package com.imooc.form;

import com.imooc.dto.CartDto;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import java.util.List;

/**
 * @Description 前端上送的订单信息
 * @Author GuanHuizhen
 * @Date 2018/8/8
 */

@Data
public class OrderForm {
    @NotBlank(message = "用户姓名不能为空")
    private String name;

    @NotBlank(message = "电话信息不能为空")
    private String phone;

    @NotBlank(message = "地址信息不能为空")
    private String address;

    @NotBlank(message = "openid不能为空")
    private String openid;

    @NotEmpty(message = "订单明细不能为空")
    @Valid//嵌套类型的校验要加上此注解
    private List<CartForm> item;

}
