package com.ffb.order_service.service;

import com.ffb.order_service.OrderServiceApplicationTests;
import com.ffb.order_service.domain.ProductOrder;
import com.ffb.order_service.dto.OrderDTO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class ProductOrderServiceTest1 extends OrderServiceApplicationTests {
    @Autowired
    private ProductOrderService productOrderService;
    @Test
    public void save() {
        final ProductOrder save = productOrderService.save(1, 2);
    }

    @Test
    public void create() {
        OrderDTO orderDTO = new OrderDTO();
        final OrderDTO dto = productOrderService.create(orderDTO);
    }

    @Test
    public void finish() {
        final OrderDTO finish = productOrderService.finish("1");
    }
}