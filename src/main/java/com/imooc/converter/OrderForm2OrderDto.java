package com.imooc.converter;

import com.imooc.dataobject.OrderDetail;
import com.imooc.dto.CartDto;
import com.imooc.dto.OrderDto;
import com.imooc.form.OrderForm;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description OrderForm对象转换为OrderDto
 * @Author GuanHuizhen
 * @Date 2018/8/8
 */
public class OrderForm2OrderDto {

    public static OrderDto convert(OrderForm orderForm) {
        OrderDto orderDto = new OrderDto();
        orderDto.setBuyerName(orderForm.getName());
        orderDto.setBuyerAddress(orderForm.getAddress());
        orderDto.setBuyerPhone(orderForm.getPhone());
        orderDto.setBuyerOpenid(orderForm.getOpenid());
        List<OrderDetail> orderDetail = orderForm.getItem().stream().map(e-> {
            OrderDetail orderDetail1 = new OrderDetail();
            BeanUtils.copyProperties(e,orderDetail1);
            return orderDetail1;
        }).collect(Collectors.toList());
        orderDto.setOrderDetailList(orderDetail);
        return orderDto;
    }
}
