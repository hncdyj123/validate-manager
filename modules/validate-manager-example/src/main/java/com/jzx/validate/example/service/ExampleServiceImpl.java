package com.jzx.validate.example.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jzx.validate.core.message.Message;
import com.jzx.validate.example.domain.User;

public class ExampleServiceImpl implements ExampleService {
	private static Logger LOGGER = LoggerFactory.getLogger(ExampleServiceImpl.class);

	public Message validateExample(User user) throws Exception {
		LOGGER.warn("pass validate!");
		return new Message();
	}
}
