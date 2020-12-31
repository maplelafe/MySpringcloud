package com.ffb.eureka_service.client;
import com.ffb.eureka_service.common.DecreaseStockInput;
import com.ffb.eureka_service.common.ProductInfoOutput;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

//import org.springframework.cloud.netflix.feign.FeignClient;


/**
 * 提供调用product服务的负载均衡客户端feignClient,name:eruka服务注册的名称
 * Created by ffb
 * 2017-12-10 21:04
 */
@FeignClient(name = "product-service", fallback = ProductClient.ProductClientFallback.class)
public interface ProductClient {

    @PostMapping("/api/v1/product/listForOrder")
    List<ProductInfoOutput> listForOrder(@RequestBody List<String> productIdList);

    @PostMapping("/api/v1/product/decreaseStock")
    void decreaseStock(@RequestBody List<DecreaseStockInput> decreaseStockInputList);

    @Component
    static class ProductClientFallback implements ProductClient {

        @Override
        public List<ProductInfoOutput> listForOrder(List<String> productIdList) {
            System.out.println("fallback:listForOrder");
            return null;
        }

        @Override
        public void decreaseStock(List<DecreaseStockInput> decreaseStockInputList) {
            System.out.println("fallback:decreaseStock");
        }
    }
}
