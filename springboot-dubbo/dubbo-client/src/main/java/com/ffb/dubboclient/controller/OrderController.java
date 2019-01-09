package com.ffb.dubboclient.controller;

import java.util.List;

import com.ffb.bean.UserAddress;
import com.ffb.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class OrderController {
	
	@Autowired
	OrderService orderService;
	
	@ResponseBody
	@RequestMapping(value = "/initOrder")
	public List<UserAddress> initOrder(@RequestParam("uid")String userId) {
		List<UserAddress> list=orderService.initOrder(userId);
		return list;
	}

}
