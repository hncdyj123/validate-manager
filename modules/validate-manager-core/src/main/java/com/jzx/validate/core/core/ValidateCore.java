/*
 * 文件名：ValidateCore.java
 * 版权：Copyright by 杰之迅有限公司
 * 描述：
 * 修改人：杨杰
 * 修改时间：2018年4月9日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.jzx.validate.core.core;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jzx.validate.core.constants.Constants;
import com.jzx.validate.core.message.Message;

/**
 * 核心验证类
 * 
 * @author 杨杰
 * @version 2018年4月9日
 * @see ValidateCore
 * @since
 */
public class ValidateCore {
	private static Logger LOGGER = LoggerFactory.getLogger(ValidateCore.class);

	/**
	 * 
	 * 验证方法
	 * 
	 * @param requestURI
	 * @param paramMap
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Message validate(String requestURI, Map paramMap) {
		Message message = new Message();
		ActionValidate actionValidate = LoadValidate.getValidateMap().get(requestURI);
		// 没有找到验证规则
		if (actionValidate == null) {
			message.setMessage("没有找到服务的验证规则");
			message.setCode(Constants.SYSTEM_RETURNCODE_NOT_VALIDATE_ERROR);
			LOGGER.warn("Did not find the validation rules [ " + requestURI + " ]");
			return message;
		}
		List<ParamValidate> paramValidate = new ArrayList<ParamValidate>();
		// 获取当前接口的验证规则
		List<ParamValidate> current = actionValidate.getParamList();
		paramValidate.addAll(current);
		if (StringUtils.isNotEmpty(actionValidate.getReferenceName())) { // 获取当前接口引用的验证规则
			List<ParamValidate> reference = LoadValidate.getValidateMap().get(actionValidate.getReferenceName()).getParamList();
			paramValidate.addAll(reference);
		}
		// 循环验证规则
		for (ParamValidate p : paramValidate) {
			// 参数验证
			try {
				message = validateParam(p, paramMap);
				if (message != null) {
					break;
				}
			} catch (Exception e) {
				LOGGER.error("praram validate error:{}", e);
				message.setMessage("验证规则异常");
				message.setCode(Constants.SYSTEM_RETURNCODE_VALIDATE_ERROR);
				return message;
			}
		}
		return message;
	}

	/**
	 * 
	 * 参数验证
	 * 
	 * @param p
	 * @param paramMap
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private Message validateParam(ParamValidate p, Map paramMap) {
		if (StringUtils.equalsIgnoreCase(p.getIsNull(), "Y")) { // 验证非必填
			if (StringUtils.isNotEmpty(p.getType())) { // 非必填但是有验证规则
				String paramValue = String.valueOf(paramMap.get(p.getName()));
				if (StringUtils.isNotEmpty(paramValue)) { // 被验证的值不为空
					return regExpValidate(p, paramMap);
				}
			}
			return null;
		}
		if (paramMap.get(p.getName()) == null) { // 验证是否为空
			return this.validate(p);
		}
		String paramVal = paramMap.get(p.getName()).toString();
		if (StringUtils.isBlank(paramVal)) { // 验证是否为空字符串
			return this.validate(p);
		}
		if (StringUtils.equalsIgnoreCase(p.getValidate(), "int")) { // 验证是否整形
			try {
				Integer.parseInt(paramVal);
			} catch (Exception e) {
				LOGGER.error("params convert error :{}", e);
				return this.validate(p);
			}
		}

		if (!StringUtils.isEmpty(p.getType())) {
			// 正则验证
			return regExpValidate(p, paramMap);
		}
		return null;
	}

	/**
	 * 
	 * 正则表达式验证
	 * 
	 * @param p
	 * @param paramMap
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private Message regExpValidate(ParamValidate p, Map paramMap) {
		if (StringUtils.equalsIgnoreCase(p.getType(), Constants.REGEXP_PHONE_KEY)) { // 验证手机号
			Matcher m = getMatcher(Constants.REGEXP_PHONE_VALUE, String.valueOf(paramMap.get(p.getName())));
			if (!m.find()) {
				return this.validate(p);
			}
		} else if (StringUtils.equalsIgnoreCase(p.getType(), Constants.REGEXP_MAIL_KEY)) { // 验证邮箱
			Matcher m = getMatcher(Constants.REGEXP_MAIL_VALUE, String.valueOf(paramMap.get(p.getName())));
			if (!m.find()) {
				return this.validate(p);
			}
		} else if (StringUtils.equalsIgnoreCase(p.getType(), Constants.REGEXP_TIME_KEY)) { // 验证时分
			Matcher m = getMatcher(Constants.REGEXP_TIME_VALUE, String.valueOf((paramMap.get(p.getName()))));
			if (!m.find()) {
				return this.validate(p);
			}
		} else if (StringUtils.equalsIgnoreCase(p.getType(), Constants.REGEXP_ZEROORONE_KEY)) { // 验证0或1
			Matcher m = getMatcher(Constants.REGEXP_ZEROORONE_VALUE, String.valueOf(paramMap.get(p.getName())));
			if (!m.find()) {
				return this.validate(p);
			}
		} else if (StringUtils.equalsIgnoreCase(p.getType(), Constants.REGEXP_CHINESE_KEY)) {
			String value = String.valueOf(paramMap.get(p.getName()));
			Matcher m = null;
			try {
				m = getMatcher(Constants.REGEXP_CHINESE_VALUE, URLDecoder.decode(value, "utf-8"));
				if (!m.matches()) {
					return this.validate(p);
				}
			} catch (UnsupportedEncodingException e) {
				return this.validate(p);
			}
		} else { // 剩下以正则表达式解析
			try {
				Matcher m = getMatcher(p.getType(), String.valueOf(paramMap.get(p.getName())));
				if (!m.find()) {
					return this.validate(p);
				}
			} catch (Exception ex) { // 正则表达式错误
				LOGGER.error("convert data errror : {}", ex);
				return this.validate(p);
			}
		}
		return null;
	}

	/**
	 * 
	 * 创建正则表达式对象
	 * 
	 * @param regex 正则表达
	 * @param targetStr 目标字符串
	 * @return m {@link Matcher}
	 */
	private Matcher getMatcher(String regex, String targetStr) {
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(targetStr);
		return m;
	}

	/**
	 * 创建验证返回消息
	 * 
	 * @param p
	 * @return
	 * @see
	 */
	private Message validate(ParamValidate p) {
		Message message = new Message();
		if (StringUtils.isNotBlank(p.getMsg())) {
			message.setMessage(p.getMsg());
		} else {
			message.setMessage("paramName [ " + p.getName() + " ] format error!");
		}
		message.setCode(Constants.SYSTEM_RETURNCODE_PARMAS_ERROR);
		return message;
	}
}
