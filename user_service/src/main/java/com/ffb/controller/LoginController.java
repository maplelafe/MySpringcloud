package com.ffb.controller;

import com.ffb.VO.ResultVO;
import com.ffb.constant.CookieConstant;
import com.ffb.constant.RedisConstant;
import com.ffb.dataobject.UserInfo;
import com.ffb.enums.ResultEnum;
import com.ffb.enums.RoleEnum;
import com.ffb.service.UserService;
import com.ffb.utils.CookieUtil;
import com.ffb.utils.ResultVOUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created by 廖师兄
 * 2018-03-04 23:12
 */
@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private UserService userService;

    private Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 买家登录
     *
     * @param openid
     * @param response
     * @return
     */
    @GetMapping("/buyer")
    public ResultVO buyer(@RequestParam("openid") String openid,
                          HttpServletResponse response) {
        //1. openid和数据库里的数据是否匹配
        UserInfo userInfo = userService.findByOpenid(openid);
        if (userInfo == null) {
            return ResultVOUtil.error(ResultEnum.LOGIN_FAIL);
        }

        //2. 判断角色
        if (RoleEnum.BUYER.getCode() != userInfo.getRole()) {
            return ResultVOUtil.error(ResultEnum.ROLE_ERROR);
        }

        //3. cookie里设置openid=abc
        //https://blog.csdn.net/tanyunlong_nice/article/details/47188659    cookies与session区别
        CookieUtil.set(response, CookieConstant.OPENID, openid, CookieConstant.expire);
        log.info("buyer设置cookie={}", openid);
        return ResultVOUtil.success();
    }

    @GetMapping("/seller")
    public ResultVO seller(@RequestParam("openid") String openid,
                           HttpServletRequest request,
                           HttpServletResponse response) {
        //判断是否已登录
        Cookie cookie = CookieUtil.get(request, CookieConstant.TOKEN);
        if (cookie != null) {
            final String s = stringRedisTemplate.opsForValue().get(String.format(RedisConstant.TOKEN_TEMPLATE, cookie.getValue()));
            if (!StringUtils.isEmpty(s))
                return ResultVOUtil.success();
        }

        //1. openid和数据库里的数据是否匹配
        UserInfo userInfo = userService.findByOpenid(openid);
        if (userInfo == null) {
            return ResultVOUtil.error(ResultEnum.LOGIN_FAIL);
        }

        //2. 判断角色
        if (RoleEnum.SELLER.getCode() != userInfo.getRole()) {
            return ResultVOUtil.error(ResultEnum.ROLE_ERROR);
        }

        //3. redis设置key=UUID, value=xyz
        String token = UUID.randomUUID().toString();
        Integer expire = CookieConstant.expire;
        stringRedisTemplate.opsForValue().set(String.format(RedisConstant.TOKEN_TEMPLATE, token),
                openid,
                expire,
                TimeUnit.SECONDS);

        //4. cookie里设置token=UUID
        CookieUtil.set(response, CookieConstant.TOKEN, token, CookieConstant.expire);
        log.info("seller设置cookie:{}", token);
        return ResultVOUtil.success();
    }


}
