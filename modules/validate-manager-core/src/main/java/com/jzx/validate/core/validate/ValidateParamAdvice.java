package com.jzx.validate.core.validate;

import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jzx.validate.core.core.ValidateCore;
import com.jzx.validate.core.message.Message;

/**
 * 参数验证切面
 * 
 * @author 杨杰
 * @version 2018年4月9日
 * @see ValidateParamAdvice
 * @since
 */
public class ValidateParamAdvice {
	private static Logger LOGGER = LoggerFactory.getLogger(ValidateParamAdvice.class);

	private ValidateCore validateCore = new ValidateCore();

	@SuppressWarnings("rawtypes")
	public Object doAround(ProceedingJoinPoint jp) throws Throwable {
		long startTime = System.currentTimeMillis();
		// 获取类执行的方法
		String classname = jp.getTarget().getClass().getName();
		String method = jp.getSignature().getName();
		Object[] object = jp.getArgs();
		String requestURI = classname + "." + method;
		LOGGER.debug("requestURI is : " + requestURI);
		// 获取所有的请求参数
		Map paramMap = BeanUtils.describe(object[0]);
		// 验证参数
		Message message = validateCore.validate(requestURI, paramMap);

		// Map 返回消息
		if (message != null) {
			return message;
		}
		LOGGER.info("requestURI : " + requestURI + " exec success!");
		LOGGER.debug("params validate runtime is : [ " + (System.currentTimeMillis() - startTime) + " ] ms");
		return jp.proceed();
	}

}
