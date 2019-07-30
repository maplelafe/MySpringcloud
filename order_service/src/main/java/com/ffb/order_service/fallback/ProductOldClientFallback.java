package com.ffb.order_service.fallback;

import org.springframework.stereotype.Component;

/**
 * 针对商品服务，服务降级反馈处理，{通知管理员处理服务}
 * @author ffb
 * @create 2018-09-29 10:56
 */
@Component
public class ProductOldClientFallback implements ProductOldClient {

    @Override
    public String findById(int id) {
        System.out.println("feign调用product异常！请联系管理员");
        return null;
    }
}
