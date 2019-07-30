package com.ffb.eureka_service.controller;

import com.ffb.eureka_service.common.DecreaseStockInput;
import com.ffb.eureka_service.dao.ProductCategory;
import com.ffb.eureka_service.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author : ffb
 * @version TODO
 * @Project: mySpringCloud
 * @Description: TODO
 * @date Date : 2019年07月30日 11:29
 */
@Controller
@RequestMapping("/api/v1/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("batchSave")
    public void batchSave(@RequestBody List<ProductCategory> List){
        for (ProductCategory one:List) {
            categoryService.insert(one);
        }
    }

    @PostMapping("save")
    public void batchSave(@RequestBody ProductCategory productCategory){
        categoryService.insert(productCategory);
    }
}
