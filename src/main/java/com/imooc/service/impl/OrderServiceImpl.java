package com.imooc.service.impl;

import com.imooc.converter.OrderMaster2OrderDto;
import com.imooc.dataobject.OrderDetail;
import com.imooc.dataobject.OrderMaster;
import com.imooc.dataobject.ProductInfo;
import com.imooc.dto.CartDto;
import com.imooc.dto.OrderDto;
import com.imooc.enums.OrderStatusEnum;
import com.imooc.enums.PayStatusEnum;
import com.imooc.enums.ResultEnum;
import com.imooc.exception.GlobalException;
import com.imooc.repository.OrderDetailRepository;
import com.imooc.repository.OrderMasterRepository;
import com.imooc.service.OrderService;
import com.imooc.service.ProductService;
import com.imooc.util.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description 订单服务
 * @Author GuanHuizhen
 * @Date 2018/8/2
 */
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Autowired
    ProductService productService;
    @Autowired
    OrderMasterRepository orderMasterRepository;
    @Autowired
    OrderDetailRepository orderDetailRepository;

    /**
     * 生成订单号
     * 查询库存商品（取价格，判断库存，要将外围上送的订单详情补充完整）
     * 计算总金额
     * 保存订单明细
     * 保存订单主表信息
     * @param orderDto
     * @return
     */
    @Override
    @Transactional
    public OrderDto create(OrderDto orderDto) {

        //遍历订单明细列表获取商品id
        List<String> productIds = orderDto.getOrderDetailList().stream().map(e -> e.getProductId()).collect(Collectors.toList());
        //根据商品id列表查询所有相关的商品信息
        String orderId = KeyUtil.getUniqueKey();
        BigDecimal totalAmount = new BigDecimal(0);
        List<ProductInfo> productInfoList = productService.findByProductIds(productIds);
        for(OrderDetail orderDetail : orderDto.getOrderDetailList()) {
            for(ProductInfo productInfo : productInfoList) {
                if(orderDetail.getProductId().equals(productInfo.getProductId())) {
                    BeanUtils.copyProperties(productInfo,orderDetail);
                    orderDetail.setOrderId(orderId);
                    //生成订单明细ID
                    orderDetail.setDetailId(KeyUtil.getUniqueKey());
                    //累计订单总价
                    totalAmount = productInfo.getProductPrice().multiply(new BigDecimal(orderDetail.getProductQuantity())).add(totalAmount);
                    break;//找到对应商品信息后就可以跳出最内层循环
                }
            }
            orderDetailRepository.save(orderDetail);//保存订单明细
        }

        OrderMaster orderMaster = new OrderMaster();
        orderDto.setOrderId(orderId);
        orderDto.setOrderAmount(totalAmount);
        BeanUtils.copyProperties(orderDto,orderMaster,"orderStatus","payStatus","orderDetailList");
        orderMasterRepository.save(orderMaster);//保存订单主表信息

        //减库存
        List<CartDto> cartDtoList = orderDto.getOrderDetailList().stream().map(e-> new CartDto(e.getProductId(),e.getProductQuantity())).collect(Collectors.toList());
        productService.deductStock(cartDtoList);
        return orderDto;
    }

    @Override
    public OrderDto findOrderByOrderId(String orderId) {

        OrderMaster orderMaster = orderMasterRepository.findOne(orderId);
        if(orderMaster == null) {
            log.error("OrderServiceImpl.findOrderByOrderId>>>>>orderId={}"+ResultEnum.ORDER_NOT_EXIST.getMessage(),orderId);
            throw new GlobalException(ResultEnum.ORDER_NOT_EXIST);
        }
        List<OrderDetail> orderDetailList = orderDetailRepository.findByOrderId(orderId);
        if(orderDetailList.isEmpty()) {
            log.error("OrderServiceImpl.findOrderByOrderId>>>>>orderId={}"+ResultEnum.ORDER_DETAIL_EMPTY,orderId);
        }
        OrderDto orderDto = new OrderDto();
        BeanUtils.copyProperties(orderMaster,orderDto);
        orderDto.setOrderDetailList(orderDetailList);
        return orderDto;
    }

    @Override
    public Page<OrderDto> findListByBueryOpenid(String buyerOpenid, Pageable pageable) {
        Page<OrderMaster> orderMasterList = orderMasterRepository.findByBuyerOpenid(buyerOpenid,pageable);
        List<OrderDto> orderDtoList = OrderMaster2OrderDto.convert(orderMasterList.getContent());
        Page<OrderDto> page = new PageImpl<OrderDto>(orderDtoList,pageable,orderMasterList.getTotalElements());
        return page;
    }

    @Override
    @Transactional
    public OrderDto cancel(OrderDto orderDto) {
        //判断订单的状态，如果是新订单，那么应该都没有取消按钮呀，所以能点取消的应该都是可取消的才对，但是在这里还是判断一下
        if(!OrderStatusEnum.NEW.getCode().equals(orderDto.getOrderStatus())) {
            log.error("OrderServiceImpl.cancel>>>>>orderStatus={}"+ResultEnum.ORDER_STATUS_ERROR.getMessage(),orderDto.getOrderStatus());
            throw new GlobalException(ResultEnum.ORDER_STATUS_ERROR);
        }
        //修改订单状态
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDto,orderMaster);
        orderMaster.setOrderStatus(OrderStatusEnum.CANCEL.getCode());
        OrderMaster updateResult = orderMasterRepository.save(orderMaster);
        if(updateResult == null) {
            log.error("OrderServiceImpl.cancel>>>>>orderMaster={}"+ResultEnum.ORDER_UPDATE_FAIL.getMessage(),orderMaster);
            throw new GlobalException(ResultEnum.ORDER_UPDATE_FAIL);
        }

        //返回库存
        if(orderDto.getOrderDetailList() == null || orderDto.getOrderDetailList().isEmpty()) {
            log.error("OrderServiceImpl.cancel>>>>>orderDto={}"+ResultEnum.ORDER_DETAIL_EMPTY.getMessage(),orderDto);
            throw new GlobalException(ResultEnum.ORDER_DETAIL_EMPTY);
        }
        List<CartDto> cartDtoList = orderDto.getOrderDetailList().stream().map(e-> new CartDto(e.getProductId(),e.getProductQuantity())).collect(Collectors.toList());
        productService.addStock(cartDtoList);

        //如果已支付，需要退款
        if(OrderStatusEnum.FINISHED.getCode().equals(orderDto.getOrderStatus())) {
            //TODO
        }
        return orderDto;
    }

    @Override
    @Transactional
    public OrderDto finish(OrderDto orderDto) {
        //判断是否支付，没支付的订单不能点完成
        if(!PayStatusEnum.SUCCESS.getCode().equals(orderDto.getPayStatus())) {
            log.error("OrderServiceImpl.paid>>>>>orderId={},payStatus={}"+ResultEnum.ORDER_NO_PAY.getMessage(),orderDto.getOrderId(),orderDto.getPayStatus());
            throw new GlobalException(ResultEnum.ORDER_NO_PAY);
        }

        //判断订单的状态，如果是新订单，那么应该都没有取消按钮呀，所以能点取消的应该都是可取消的才对，但是在这里还是判断一下
        if(!OrderStatusEnum.NEW.getCode().equals(orderDto.getOrderStatus())) {
            log.error("OrderServiceImpl.cancel>>>>>orderId={},orderStatus={}"+ResultEnum.ORDER_STATUS_ERROR.getMessage(),orderDto.getOrderId(),orderDto.getOrderStatus());
            throw new GlobalException(ResultEnum.ORDER_STATUS_ERROR);
        }

        //修改订单状态
        orderDto.setOrderStatus(OrderStatusEnum.FINISHED.getCode());
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDto, orderMaster);
        OrderMaster updateResult = orderMasterRepository.save(orderMaster);
        if (updateResult == null) {
            log.error("OrderServiceImpl.cancel>>>>>orderMaster={}"+ResultEnum.ORDER_UPDATE_FAIL, orderMaster);
            throw new GlobalException(ResultEnum.ORDER_UPDATE_FAIL);
        }

        return orderDto;
    }

    @Override
    @Transactional
    public OrderDto paid(OrderDto orderDto) {
        //判断订单的状态，如果不是新订单，则不应该支付
        if(!OrderStatusEnum.NEW.getCode().equals(orderDto.getOrderStatus())) {
            log.error("OrderServiceImpl.paid>>>>>orderId={},orderStatus={}"+ResultEnum.ORDER_STATUS_ERROR.getMessage(),orderDto.getOrderId(),orderDto.getOrderStatus());
            throw new GlobalException(ResultEnum.ORDER_STATUS_ERROR);
        }

        //判断支付状态(避免重复支付)
        if(PayStatusEnum.SUCCESS.getCode().equals(orderDto.getPayStatus())) {
            log.error("OrderServiceImpl.paid>>>>>orderId={},payStatus={}"+ResultEnum.ORDER_PAY_STATUS_ERROR.getMessage(),orderDto.getOrderId(),orderDto.getPayStatus());
            throw new GlobalException(ResultEnum.ORDER_PAY_STATUS_ERROR);
        }

        //修改支付状态为已支付（支付状态更新失败要退款？还是说重新操作）
        orderDto.setPayStatus(PayStatusEnum.SUCCESS.getCode());
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDto, orderMaster);
        OrderMaster updateResult = orderMasterRepository.save(orderMaster);
        if (updateResult == null) {
            log.error("OrderServiceImpl.cancel>>>>>orderMaster={}"+ResultEnum.ORDER_UPDATE_FAIL, orderMaster);
            throw new GlobalException(ResultEnum.ORDER_UPDATE_FAIL);
            //TODO 我觉得这里不能用全局异常，因为需要前端捕获，然后不扣款？？
        }

        return orderDto;
    }

    @Override
    public Page<OrderDto> findList(Pageable pageable) {
        Page<OrderMaster> orderMasterList = orderMasterRepository.findAll(pageable);
        List<OrderDto> orderDtoList = OrderMaster2OrderDto.convert(orderMasterList.getContent());
        Page<OrderDto> page = new PageImpl<OrderDto>(orderDtoList,pageable,orderMasterList.getTotalElements());
        return page;
    }
}
