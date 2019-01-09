package com.ffb.service.impl;

import com.ffb.dataobject.UserInfo;
import com.ffb.repository.UserInfoRepostory;
import com.ffb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by 廖师兄
 * 2018-03-04 21:45
 */
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserInfoRepostory repostory;
	/**
	 * 通过openid来查询用户信息
	 *
	 * @param openid
	 * @return
	 */
	@Override
	public UserInfo findByOpenid(String openid) {
		return repostory.findByOpenid(openid);
	}
}
