package com.imooc.service.impl;

import com.imooc.dataobject.OrderDetail;
import com.imooc.dataobject.OrderMaster;
import com.imooc.dto.OrderDto;
import com.imooc.enums.OrderStatusEnum;
import com.imooc.service.OrderService;
import freemarker.cache.OrMatcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.awt.print.Printable;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderServiceImplTest {
    @Autowired
    OrderService orderService;

    @Test
    public void create() {
        OrderDto orderDto = new OrderDto();
        orderDto.setBuyerAddress("幕课网");
        orderDto.setBuyerName("廖师兄");
        orderDto.setBuyerOpenid("abc123");
        orderDto.setBuyerPhone("15800000000");
        List<OrderDetail> orderDetailList = new ArrayList<OrderDetail>();
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setProductId("10001");
        orderDetail.setProductQuantity(2);
        orderDetailList.add(orderDetail);
        orderDto.setOrderDetailList(orderDetailList);
        orderService.create(orderDto);

    }

    @Test
    public void findOrderByOrderId() {
      orderService.findOrderByOrderId("1533721165169236000");
    }

    @Test
    public void findListByBueryOpenid() {
        PageRequest pageRequest = new PageRequest(0,2);
        orderService.findListByBueryOpenid("abc123",pageRequest);
    }

    @Test
    public void cancel() {
        OrderDto orderDto =orderService.findOrderByOrderId("101533290885858572791");
       // orderDto.setOrderStatus(OrderStatusEnum.NEW.getCode());
        List<OrderDetail> orderDetailList = new ArrayList<OrderDetail>();
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setProductId("10001");
        orderDetail.setProductQuantity(2);
        //orderDetailList.add(orderDetail);
        //orderDto.setOrderDetailList(orderDetailList);
        orderService.cancel(orderDto);
    }

    @Test
    public void finish() {
        OrderDto orderDto =orderService.findOrderByOrderId("101533290885858572791");
        orderService.finish(orderDto);
    }

    @Test
    public void paid() {
    }

    @Test
    public void findList() {
    }
}