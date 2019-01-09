package com.ffb.apigateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpRequest;
import org.apache.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

/**
 * @author ffb
 * @create 2018-10-07 23:46
 */
@Component
public class LoginFilter extends ZuulFilter {
    @Override
    public String filterType() {
//      过滤器类型  pre，routing postRouting
        return PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        //过滤器执行顺序
        return 4;
    }
    //过滤器是否生效,z做拦截
    @Override
    public boolean shouldFilter() {
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest http= currentContext.getRequest();
        System.out.println(http.getRequestURI());
        System.out.println(http.getRequestURL());
/*        ACL
/apigateway/order/api/v1/order/save
        http://localhost:9000/apigateway/order/api/v1/order/save*/
        if (http.getRequestURI().contains("/apigateway/order"))
        return true;
        return false;
    }

    //url带token校验，cookie或者header
    @Override
    public Object run() throws ZuulException {
        //JWT
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request= currentContext.getRequest();
        String token=request.getHeader("token");
        //登录校验
        if(StringUtils.isBlank(token)){
            token = request.getParameter("token");
        }
        if(StringUtils.isBlank(token)){
            //校验不通过
            currentContext.setSendZuulResponse(false);
            currentContext.setResponseStatusCode(HttpStatus.SC_UNAUTHORIZED);
        }
       // System.out.println("拦截了！");
        return null;
    }
}
