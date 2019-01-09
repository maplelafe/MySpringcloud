package com.ffb.eureka_service.service.impl;

import com.ffb.eureka_service.common.DecreaseStockInput;
import com.ffb.eureka_service.common.ProductInfoOutput;
import com.ffb.eureka_service.dao.ProductInfo;
import com.ffb.eureka_service.enums.ProductStatusEnum;
import com.ffb.eureka_service.enums.ResultEnum;
import com.ffb.eureka_service.exception.ProductException;
import com.ffb.eureka_service.repository.ProductInfoRepository;
import com.ffb.eureka_service.service.ProductService;
import com.ffb.eureka_service.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private  final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private ProductInfoRepository productInfoRepository;

    @Autowired
    private AmqpTemplate amqpTemplate;

    /**
     * 查询所有在线商品
     * @return
     */
    @Override
    public List<ProductInfo> findUpAll() {
        //自动补全代码 ctrl+alt+v
        List<ProductInfo> upAll = productInfoRepository.findByProductStatus(ProductStatusEnum.UP.getCode());
        return upAll;
    }

    /**
     * 根据查询所有商品
     * @param productIdList
     * @return  ProductInfoOutput
     */
    @Override
    public List<ProductInfoOutput> findList(List<String> productIdList) {

/*        List<ProductInfoOutput> piolist= new ArrayList<>();
        List<ProductInfo> byProductIdIn = productInfoRepository.findByProductIdIn(productIdList);
        return piolist;*/

//换一种写法,stream 数据流
        List<ProductInfoOutput> byProductIdIn = productInfoRepository.findByProductIdIn(productIdList)
                .stream().map(e ->{
                    ProductInfoOutput output = new ProductInfoOutput();
                    BeanUtils.copyProperties(e,output);
                    return output;
                }).collect(Collectors.toList());
        return  byProductIdIn;
    }


    @Override
    public void decreaseStock(List<DecreaseStockInput> decreaseStockInputList) {

        List<ProductInfo> productInfolist = decreaseStockProcess(decreaseStockInputList);

        //发送mq消息
        List<ProductInfoOutput> productInfoOutputList = productInfolist.stream().map(e -> {
            ProductInfoOutput output = new ProductInfoOutput();
            BeanUtils.copyProperties(e, output);
            return output;
        }).collect(Collectors.toList());

        amqpTemplate.convertAndSend("productInfo", JsonUtil.toJson(productInfoOutputList));
    }
    /**
     * DecreaseStockInput(数量和id)
     * @param decreaseStockInputList
     */
    @Transactional
    private List<ProductInfo> decreaseStockProcess(List<DecreaseStockInput> decreaseStockInputList) {
          /*        @Transactional有两个不同的包。在Spring的事务管理中应该使用
        org.springframework.transaction.annotation.Transactional
        在Java EE 7 应用中，应该使用javax.transaction.Transactional。*/
        List<ProductInfo> productInfos = new ArrayList<>();
        for (DecreaseStockInput decreaseStockInput:decreaseStockInputList) {
            Optional<ProductInfo> productInfoOptional = productInfoRepository.findById(decreaseStockInput.getProductId());

//            商品不存在
           if (!productInfoOptional.isPresent()){
               throw new ProductException(ResultEnum.PRODUCT_NOT_EXIST);
           }
           final ProductInfo productInfo = productInfoOptional.get();
            Integer i = productInfo.getProductStock() - decreaseStockInput.getProductQuantity();
//            数量扣除异常
            if (i<0){
               throw new ProductException(ResultEnum.PRODUCT_STOCK_ERROR);
            }
            //更新商品库存
            productInfo.setProductStock(i);
            productInfoRepository.save(productInfo);
            productInfos.add(productInfo);
        }
        return productInfos;
    }
}
