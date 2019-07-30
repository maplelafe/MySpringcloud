package com.ffb.order_service.controller;

import com.ffb.order_service.domain.ProductOrder;
import com.ffb.order_service.service.ProductOrderService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.apache.commons.lang.StringUtils;
import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("api/v1/order")
@RefreshScope
public class OrderTestController {


    @Autowired
    private ProductOrderService productOrderService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @RequestMapping("save")
    @HystrixCommand(fallbackMethod = "saveOrderFail") //
    public Object save(@RequestParam("user_id")int userId, @RequestParam("product_id") int productId, HttpServletRequest request){
        String token = request.getHeader("token");
        String cookie = request.getHeader("cookie");
        System.out.println("token:"+token);
        System.out.println("cookie:"+cookie);
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("code",0);
        map.put("msg","Hystrix:order服务成功！");
        map.put("data",productOrderService.save(userId, productId));
        return map;
    }

    public Object saveOrderFail(int userid,int productid,HttpServletRequest request){
//       监控报警
        String saveOrderKey ="save-order";
        String sendValue = redisTemplate.opsForValue().get(saveOrderKey);
        final String ip = request.getRemoteAddr();
        new Thread(
                ()-> {
                if(StringUtils.isBlank(sendValue)){
                    System.out.println("紧急短信，用户下单失败，请离开查找原因,ip地址是="+ip);
                    //发送一个http请求，调用短信服务 TODO
                    redisTemplate.opsForValue().set(saveOrderKey, "save-order-fail", 20, TimeUnit.SECONDS);
                }else{
                    System.out.println("已经发送过短信，20秒内不重复发送");
                }
        }
        ).start();
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("code",-1);
        map.put("msg","Hystrix:order服务失败！");
        //map.put("data",userid+","+productid);
        return  map;
    }

}
