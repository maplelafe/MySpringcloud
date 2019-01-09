package com.ffb.eureka_service.Utils;

import com.ffb.eureka_service.ProductApplicationTests;
import com.ffb.eureka_service.common.ProductInfoOutput;
import com.ffb.eureka_service.utils.JsonUtil;
import org.junit.Test;

public class JsonUtilsT extends ProductApplicationTests{
    @Test
    public void test(){
        ProductInfoOutput out = new ProductInfoOutput();
        out.setProductId("11111");
        out.setProductDescription("description");
        System.out.println(JsonUtil.toJson(out));
    }
}
