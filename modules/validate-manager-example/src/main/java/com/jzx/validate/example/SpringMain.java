package com.jzx.validate.example;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.jzx.validate.example.domain.User;
import com.jzx.validate.example.service.ExampleService;

/**
 * 切面参数验证demo<br/>
 * 适用于service层验证，例如：dubbo,motan等<br/>
 * 
 * @author 杨杰
 * @version 2018年4月9日
 * @see SpringMain
 * @since
 */
public class SpringMain {

	public static void main(String[] args) throws Exception {
		ApplicationContext cotext = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
		// ExampleService exampleService = SpringBeanUtils.getBean(ExampleService.class);
		ExampleService exampleService = cotext.getBean(ExampleService.class);
		while (true) {
			User user = new User();
			user.setUserName("jzx");
			user.setMobile("13888889999");
			exampleService.validateExample(user);
			Thread.sleep(5000);
		}
	}
}
