package com.imooc.controller;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.imooc.converter.OrderForm2OrderDto;
import com.imooc.dto.OrderDto;
import com.imooc.enums.ResultEnum;
import com.imooc.form.OrderForm;
import com.imooc.service.OrderService;
import com.imooc.vo.ResultVo;
import jdk.nashorn.internal.parser.JSONParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description 买家订单
 * @Author GuanHuizhen
 * @Date 2018/8/8
 */
@Slf4j
@RestController
@RequestMapping("/buyer/order")
public class BuyerOrderController {
    @Autowired
    OrderService orderService;

    //创建订单（购物车）
    @PostMapping("/create")
    public ResultVo<Map<String,String>> create(@Valid @RequestBody OrderForm orderForm, Model model) {
        OrderDto orderDto = OrderForm2OrderDto.convert(orderForm);
        if(CollectionUtils.isEmpty(orderDto.getOrderDetailList())) {
            log.error("BuyerOrderController.create>>>>>>orderForm={}", ResultEnum.CART_EMPTY.getMessage(),orderForm);
            return ResultVo.failure(ResultEnum.CART_EMPTY.getMessage());
        }
        orderDto = orderService.create(orderDto);
        Map<String,String> data = new HashMap<>();
        data.put("orderId",orderDto.getOrderId());
        return ResultVo.success(data);
    }

    //订单列表

    //订单详情


    //取消订单


    //支付订单
    //完成订单
}
