package com.ffb.eureka_service.repository;
import com.ffb.eureka_service.dao.ProductInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * ProductInfo 给JPA的返回参数，跟daoobject 的属性一致
 * Created by
 * 2017-12-09 21:29
 */
public interface ProductInfoRepository extends JpaRepository<ProductInfo, String>{

    List<ProductInfo> findByProductStatus(Integer productStatus);

    /**
     * jpa的in查询
     * @param productIdList
     * @return
     */
    List<ProductInfo> findByProductIdIn(List<String> productIdList);
}
