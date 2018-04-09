package com.jzx.validate.core.core;

/**
 * 参数验证规则实体类
 * 
 * @author 杨杰
 * @version 2018年4月9日
 * @see ParamValidate
 * @since
 */
public class ParamValidate {
	/** 参数名称 **/
	private String name;
	/** 数据验证(int String ...) **/
	private String validate;
	/** 验证类型(mail phone ...) **/
	private String type;
	/** 是否可以为空(Y 是 N 否) **/
	private String isNull;
	/** 验证消息 **/
	private String msg;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValidate() {
		return validate;
	}

	public void setValidate(String validate) {
		this.validate = validate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIsNull() {
		return isNull;
	}

	public void setIsNull(String isNull) {
		this.isNull = isNull;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		return "ParamValidate [name=" + name + ", validate=" + validate + ", type=" + type + ", isNull=" + isNull + ", msg=" + msg + "]";
	}

}
