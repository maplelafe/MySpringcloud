package com.ffb.order_service.respository;

import com.ffb.order_service.OrderServiceApplicationTests;
import com.ffb.order_service.dao.OrderDetail;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

public class OrderDetailRepositoryTest1 extends OrderServiceApplicationTests {
    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Test
    public void findAllByOrderId() {
        final List<OrderDetail> allByOrderId = orderDetailRepository.findAllByOrderId("1");
        Assert.assertTrue(allByOrderId.size()>0);
    }
}