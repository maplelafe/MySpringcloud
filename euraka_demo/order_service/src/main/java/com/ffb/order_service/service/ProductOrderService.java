package com.ffb.order_service.service;

import com.ffb.order_service.domain.ProductOrder;
import com.ffb.order_service.dto.OrderDTO;
import org.springframework.stereotype.Service;

/**
 * 订单业务类
 */

public interface ProductOrderService {


    /**
     * 下单接口
     * @param userId
     * @param productId
     * @return
     */
     ProductOrder save(int userId, int productId);

    /**
     * 创建订单
     * @param orderDTO
     * @return
     */
    OrderDTO create(OrderDTO orderDTO);

    /**
     * 完结订单(只能卖家操作)接收订单
     * @param orderId
     * @return
     */
    OrderDTO finish(String orderId);


}
