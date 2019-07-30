package com.ffb.eureka_service.service;

import com.ffb.eureka_service.common.DecreaseStockInput;
import com.ffb.eureka_service.common.ProductInfoOutput;
import com.ffb.eureka_service.dao.ProductInfo;

import java.util.List;

public interface ProductService {

    /**
     * 查询所有在架商品列表
     */
    List<ProductInfo> findUpAll();

    /**
     * 查询商品列表
     * @param productIdList
     * @return
     */
    List<ProductInfoOutput> findList(List<String> productIdList);

    /**
     *
     * 扣库存,事务控制
     * @param decreaseStockInputList
     */
    void decreaseStock(List<DecreaseStockInput> decreaseStockInputList);

}
