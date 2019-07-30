package com.ffb.eureka_service.repository;


import com.ffb.eureka_service.dao.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by
 * 2017-12-09 21:41
 */
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Integer> ,CrudRepository<ProductCategory, Integer> {

    List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList);

}
