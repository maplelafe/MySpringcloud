package com.ffb.apigateway.filter;

import com.ffb.apigateway.Utils.CookieConstant;
import com.ffb.apigateway.Utils.CookieUtil;
import com.ffb.apigateway.Utils.RedisConstant;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

/**
 * 对订单进行过滤操作:create 买家,finish 卖家
 * @author ffb
 * @create 2018-10-07 23:46
 */
@Component
public class AuthFilter extends ZuulFilter {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        //过滤器执行顺序
        return 4;
    }
    @Override
    public boolean shouldFilter() {
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest http= currentContext.getRequest();
        System.out.println(http.getRequestURI());
        System.out.println(http.getRequestURL());
        if (http.getRequestURI().contains("/order/create")||http.getRequestURI().contains("/order/finish"))
        return true;
        return false;
    }
    @Override
    public Object run() throws ZuulException {
        //JWT
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request= currentContext.getRequest();
//        买家buyer  key=openid,value=xxxx url: order/create
//        买家 url order/finish key=token value=UUID ,redis存储
        if(request.getRequestURI().contains("/order/create")){
            final Cookie cookie = CookieUtil.get(request, "openid");
            if(cookie==null||StringUtils.isBlank(cookie.getValue())){
                //无法继续
                currentContext.setSendZuulResponse(false);
                currentContext.setResponseStatusCode(HttpStatus.SC_UNAUTHORIZED);
            }
        }
        if(request.getRequestURI().contains("/order/finish")){
            final Cookie cookie = CookieUtil.get(request, "token");
            if(cookie==null||StringUtils.isBlank(cookie.getValue())
                    ||StringUtils.isBlank(stringRedisTemplate.opsForValue().get(String.format(RedisConstant.TOKEN_TEMPLATE,cookie.getValue())))){
                //无法继续
                currentContext.setSendZuulResponse(false);
                currentContext.setResponseStatusCode(HttpStatus.SC_UNAUTHORIZED);
            }
        }
        return null;
    }
}
