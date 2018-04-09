package com.jzx.validate.example.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * spring帮助类<br/>
 * <bean id="SpringHelper"class="com.jzx.validate.example.utils.SpringBeanUtils"/>
 * 
 * @author 杨杰
 * @version 2017年10月23日
 * @see SpringBeanUtils
 * @since
 */
public class SpringBeanUtils implements ApplicationContextAware, DisposableBean {

	private static ApplicationContext applicationContext = null;

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringBeanUtils.applicationContext = applicationContext;
	}

	public void destroy() throws Exception {
		applicationContext = null;
	}

	/**
	 * 获取applicationContext
	 * 
	 * @return applicationContext
	 */
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * 根据Bean名称获取实例
	 * 
	 * @param name Bean注册名称
	 * 
	 * @return bean实例
	 * 
	 * @throws BeansException
	 */
	public static Object getBean(String name) throws BeansException {
		return applicationContext.getBean(name);
	}

	/**
	 * 根据Bean名称获取实例
	 * 
	 * @param name Bean的Class描述
	 * 
	 * @return bean实例
	 * 
	 * @throws BeansException
	 */
	public static <T> T getBean(Class<T> clazz) throws BeansException {
		return applicationContext.getBean(clazz);
	}

}
