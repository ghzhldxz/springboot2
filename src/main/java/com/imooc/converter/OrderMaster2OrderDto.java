package com.imooc.converter;

import com.imooc.dataobject.OrderMaster;
import com.imooc.dto.OrderDto;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description OrderMaster对象转换为OrderDto
 * @Author GuanHuizhen
 * @Date 2018/8/7
 */
public class OrderMaster2OrderDto {

    public static OrderDto convert(OrderMaster source) {
        OrderDto target = new OrderDto();
        BeanUtils.copyProperties(source,target);
        return target;
    }

    public static List<OrderDto> convert(List<OrderMaster> source) {
        List<OrderDto> target = new ArrayList<>();
        source.forEach(e->
            target.add(OrderMaster2OrderDto.convert(e))
        );
        return target;
    }
}
