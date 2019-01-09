package com.ffb.order_service.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.ffb.eureka_service.client.ProductClient;
import com.ffb.eureka_service.common.DecreaseStockInput;
import com.ffb.eureka_service.common.ProductInfoOutput;
import com.ffb.order_service.dao.OrderDetail;
import com.ffb.order_service.dao.OrderMaster;
import com.ffb.order_service.domain.ProductOrder;
import com.ffb.order_service.dto.OrderDTO;
import com.ffb.order_service.enums.OrderStatusEnum;
import com.ffb.order_service.enums.PayStatusEnum;
import com.ffb.order_service.enums.ResultEnum;
import com.ffb.order_service.exception.OrderException;
import com.ffb.order_service.respository.OrderDetailRepository;
import com.ffb.order_service.respository.OrderMasterRepository;
import com.ffb.order_service.fallback.ProductOldClient;
import com.ffb.order_service.service.ProductOrderService;
import com.ffb.order_service.utils.JsonUtils;
import com.ffb.order_service.utils.KeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ffb
 * @create 2018-09-28 15:29
 */
@Service
public class ProductOrderServiceImpl implements ProductOrderService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private LoadBalancerClient loadBalancer;

    //@FeignClient，可以自动装配，
/*    @Autowired
    private ProductOldClient productclient;*/

    @Autowired
    private ProductClient productclient1;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private OrderMasterRepository orderMasterRepository;

    private  final Logger log = LoggerFactory.getLogger(getClass());
    @Override
    public ProductOrder save(int userId, int productId) {
        log.info("ProductOrder save");

        //restTemplate软负载均衡，调用方式一
        //Map<String,Object> map= restTemplate.getForObject("http://product-service/api/v1/product/find?id="+productId, Map.class);

        //调用方式二RestTemplate
     /*   ServiceInstance instance = loadBalancer.choose("product-service");
        URI storesUri = URI.create(String.format("http://%s:%s/api/v1/product/find?id="+productId, instance.getHost(), instance.getPort()));
        RestTemplate restTemplate1 = new RestTemplate();
        Map<String,Object> map= restTemplate1.getForObject(storesUri, Map.class);
        System.out.println(map);*/

/*        //调用方式三feign客户端调用
        String response = productclient.findById(productId);
        JsonNode jsonNode = JsonUtils.str2JsonNode(response);

        ProductOrder productOrder = new ProductOrder();
        productOrder.setCreateTime(new Date());
        productOrder.setUserId(userId);
        productOrder.setTradeNo(UUID.randomUUID().toString());
      //  productOrder.setProductName(map.get("name").toString());
     //   productOrder.setPrice(Integer.parseInt(map.get("price").toString()));
        productOrder.setProductName(jsonNode.get("name").toString());
        productOrder.setPrice(Integer.parseInt(jsonNode.get("price").toString()));
        return productOrder;*/
        return null;
    }

    @Override
    @Transactional
    public OrderDTO create(OrderDTO orderDTO) {
        String key = KeyUtil.genUniqueKey();
  //orderDTO 取出Product ID的列表，校验商品ID是否正确，(调用商品服务)
        final List<String> productIdList = orderDTO.getOrderDetailList().stream().map(e -> e.getProductId()
        ).collect(Collectors.toList());
        List<ProductInfoOutput> productInfoOutputList = productclient1.listForOrder(productIdList);
        if(CollectionUtils.isEmpty(productInfoOutputList)){
            throw new OrderException(ResultEnum.ORDER_PRODUCTID_NOT_EXIST);
        }
       if( productInfoOutputList.size()!=productInfoOutputList.size()){
           throw new OrderException(ResultEnum.ORDER_PRODUCTID_NOT_EXIST);
       }

     //保存Orderdetail(总价，商品详情)
        BigDecimal amount = new BigDecimal(BigInteger.ZERO);
        //遍历传入list，遍历商品列表list
        for (OrderDetail orderDetail: orderDTO.getOrderDetailList()) {
            for (ProductInfoOutput productInfoOutput:productInfoOutputList) {
                if(productInfoOutput.getProductId().equals(orderDetail.getProductId())){
                    amount= productInfoOutput.getProductPrice().multiply(new BigDecimal(orderDetail.getProductQuantity())).add(amount);
                    BeanUtils.copyProperties(productInfoOutput,orderDetail);
                    orderDetail.setOrderId(key);
                    orderDetail.setDetailId(KeyUtil.genUniqueKey());
                    //订单详情入库
                    orderDetailRepository.save(orderDetail);
                }
            }
        }

   //扣库存(调用商品服务),组装        List<DecreaseStockInput>(商品ID，商品数量)
        final List<DecreaseStockInput> decreaseStockInputList = orderDTO.getOrderDetailList().stream().map(e -> {
            return new DecreaseStockInput(e.getProductId(), e.getProductQuantity());
        }).collect(Collectors.toList());
        productclient1.decreaseStock(decreaseStockInputList);

        //订单入库
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDTO,orderMaster);
        orderMaster.setOrderId(key);
        orderMaster.setOrderAmount(amount);
        orderMaster.setOrderStatus(OrderStatusEnum.NEW.getCode());
        orderMaster.setPayStatus(PayStatusEnum.WAIT.getCode());
        orderMasterRepository.save(orderMaster);
        orderDTO.setOrderId(key);
        return orderDTO;
    }

    @Override
    public OrderDTO finish(String orderId) {
        final Optional<OrderMaster> byId = orderMasterRepository.findById(orderId);
        //判断订单是否存在
        if(!byId.isPresent()){
            throw new OrderException(ResultEnum.ORDER_NOT_EXIST);
        }
        //判断订单状态
        final OrderMaster orderMaster = byId.get();
        if(!orderMaster.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())){
            throw new OrderException(ResultEnum.ORDER_STATUS_ERROR);
        }
        //查询订单详情（是否下过订单）
        final List<OrderDetail> allByOrderId = orderDetailRepository.findAllByOrderId(orderMaster.getOrderId());
        if(CollectionUtils.isEmpty(allByOrderId)){
            throw new OrderException(ResultEnum.ORDER_DETAIL_NOT_EXIST);
        }
        //修改订单完成(未支付)
        orderMaster.setOrderStatus(OrderStatusEnum.FINISHED.getCode());
        orderMasterRepository.save(orderMaster);

        //
        OrderDTO orderdto = new OrderDTO();
        BeanUtils.copyProperties(orderMaster,orderdto);
        orderdto.setOrderDetailList(allByOrderId);
        return orderdto;
    }
}
