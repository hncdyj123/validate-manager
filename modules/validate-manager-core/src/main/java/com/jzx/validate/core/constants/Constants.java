package com.jzx.validate.core.constants;

/**
 * 系统常量类
 * 
 * @author 杨杰
 * @version 2018年4月9日
 * @see Constants
 * @since
 */
public class Constants {
	/** 参数验证失败 **/
	public static int SYSTEM_RETURNCODE_PARMAS_ERROR = 1005;
	/** 验证规则异常 **/
	public static int SYSTEM_RETURNCODE_VALIDATE_ERROR = 1006;
	/** 没有找到服务的验证规则 **/
	public static int SYSTEM_RETURNCODE_NOT_VALIDATE_ERROR = 1007;

	/** 手机号验证KEY **/
	public static String REGEXP_PHONE_KEY = "mobile";
	/** 手机号验证表达式 **/
	public static String REGEXP_PHONE_VALUE = "^\\d{11}$";

	/** 邮箱验证KEY **/
	public static String REGEXP_MAIL_KEY = "email";
	/** 邮箱号验证表达式 **/
	public static String REGEXP_MAIL_VALUE = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";

	/** 时分验证KEY **/
	public static String REGEXP_TIME_KEY = "time";
	/** 时分号验证表达式 **/
	public static String REGEXP_TIME_VALUE = "^([0-1]?[0-9]|2[0-3]):([0-5][0-9])$";

	/** 0或1验证KEY **/
	public static String REGEXP_ZEROORONE_KEY = "type";
	/** 0或1验证表达式 **/
	public static String REGEXP_ZEROORONE_VALUE = "(^0$|^1$)";

	/** 中文验证 **/
	public static String REGEXP_CHINESE_KEY = "chinese";
	/** 中文验证表达式 **/
	public static String REGEXP_CHINESE_VALUE = "[\u4E00-\uFA29]+";
}
