package com.ffb.apigateway.filter;

import com.google.common.util.concurrent.RateLimiter;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

/**
 * 限流google的guava框架RateLimiter令牌桶算法
 * github 上面的限流方式（marcosbarbero）
 * @author ffb
 * @create 2018-10-08 21:42
 */
public class RateLimitFilter extends ZuulFilter {

    //每秒创建多少个令牌，通行证
    private static final RateLimiter RATE_LIMITER = RateLimiter.create(100);
    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return -4;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest http= currentContext.getRequest();
        //只对订单接口限流
        if (http.getRequestURI().contains("/apigateway/order"))
            return true;
        return false;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext currentContext = RequestContext.getCurrentContext();
        //如果拿不到令牌，不能往下走,jmeter测试
        if(!RATE_LIMITER.tryAcquire()){
            currentContext.setSendZuulResponse(false);
            currentContext.setResponseStatusCode(HttpStatus.TOO_MANY_REQUESTS.value());
        };
        return null;
    }
}
