package com.ffb.eureka_service.service.impl;

import com.ffb.eureka_service.dao.ProductCategory;
import com.ffb.eureka_service.repository.ProductCategoryRepository;
import com.ffb.eureka_service.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by 廖师兄
 * 2017-12-09 22:06
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Override
    public List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList) {
        return productCategoryRepository.findByCategoryTypeIn(categoryTypeList);
    }

    @Override
    public ProductCategory insert(ProductCategory productCategory) {
        return productCategoryRepository.save(productCategory);
    }
}
