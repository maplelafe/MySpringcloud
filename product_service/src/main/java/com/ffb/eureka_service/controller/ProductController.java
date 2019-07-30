package com.ffb.eureka_service.controller;
import com.ffb.eureka_service.VO.ProductInfoVO;
import com.ffb.eureka_service.VO.ProductVO;
import com.ffb.eureka_service.VO.ResultVO;
import com.ffb.eureka_service.common.DecreaseStockInput;
import com.ffb.eureka_service.common.ProductInfoOutput;
import com.ffb.eureka_service.dao.ProductCategory;
import com.ffb.eureka_service.dao.ProductInfo;
import com.ffb.eureka_service.service.CategoryService;
import com.ffb.eureka_service.service.ProductService;
import com.ffb.eureka_service.utils.ResultVOUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/product")
@RefreshScope
public class ProductController {


    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Value("${server.port}")
    private String port;

    @Value("${env}")
    private String env;

    /**
     * 查询所有上架商品信息(product,category)
     * @return
     */
    @RequestMapping(value = "list")
    public ResultVO<ProductVO> getAll(){
        //查询所有上架商品
        List<ProductInfo> productUpAll = productService.findUpAll();
        //获取商品的目录分类ID
        List<Integer> categoryIdlist = productUpAll.stream().map(e -> {
            return e.getCategoryType();
        }).collect(Collectors.toList());
         //获取分类列表信息
         List<ProductCategory> categorylist = categoryService.findByCategoryTypeIn(categoryIdlist);
         //组装结果集
        List<ProductVO> ProductVOlist = new ArrayList<>();
//        1.遍历目录
        for (ProductCategory pc : categorylist) {
            ProductVO productVO = new ProductVO();
            productVO.setCategoryType(pc.getCategoryType());
            productVO.setCategoryName(pc.getCategoryName());

            List<ProductInfoVO> pinfolist = new ArrayList<>();
            for (ProductInfo productInfo:productUpAll) {
                if(productInfo.getCategoryType().equals(productVO.getCategoryType())){
                    ProductInfoVO productInfoVO = new ProductInfoVO();
                    BeanUtils.copyProperties(productInfo,productInfoVO);
                    pinfolist.add(productInfoVO);
                }
            }
            productVO.setProductInfoVOList(pinfolist);
            ProductVOlist.add(productVO);
        }
        return ResultVOUtil.success(ProductVOlist);
    }
    /**
     * 根据商品ID列表查询商品
     * Stream
     * forEach() 使用该方法迭代流中的每个数据
     * sorted() 使用该方法排序数据
     * filter()：使用该方法过滤
     * limit()：使用该方法截断
     * skip()：与limit互斥，使用该方法跳过元素
     * distinct()：使用该方法去重，注意：必须重写对应泛型的hashCode()和equals()方法
     * max，min，sum，avg，count
     * map()：接收一个方法作为参数，该函数会被应用到每个元素上，并将其映射成一个新的元素
     */

    @PostMapping("listForOrder")
    public List<ProductInfoOutput> listForOrder(@RequestBody List<String> productIdList){
        //测试服务熔断降级
/*        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        List<ProductInfoOutput> productInfoOutputs = productService.findList(productIdList);
        return productInfoOutputs;
    }

    /**
     * 根据商品ID和商品数量减少库存
     * @param decreaseStockInputList
     */
    @PostMapping("decreaseStock")
    public void decreaseStock(@RequestBody List<DecreaseStockInput> decreaseStockInputList){
        productService.decreaseStock(decreaseStockInputList);
    }
}


;