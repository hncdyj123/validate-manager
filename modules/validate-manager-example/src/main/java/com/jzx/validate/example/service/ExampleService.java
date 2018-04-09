package com.jzx.validate.example.service;

import com.jzx.validate.core.message.Message;
import com.jzx.validate.example.domain.User;

public interface ExampleService {
	/**
	 * 验证方法
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 * @see
	 */
	public Message validateExample(User user) throws Exception;
}
