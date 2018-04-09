package com.jzx.validate.core.core;

import java.util.ArrayList;
import java.util.List;

/**
 * action验证规则实体类
 * 
 * @author 杨杰
 * @version 2018年4月9日
 * @see ActionValidate
 * @since
 */
public class ActionValidate {
	/** 要验证的url **/
	private String actionUrl;
	/** 当前Action的验证规则 **/
	private List<ParamValidate> paramList = new ArrayList<ParamValidate>();
	/** 引用的验证规则名称 **/
	private String referenceName;

	public String getActionUrl() {
		return actionUrl;
	}

	public void setActionUrl(String actionUrl) {
		this.actionUrl = actionUrl;
	}

	public List<ParamValidate> getParamList() {
		return paramList;
	}

	public void setParamList(List<ParamValidate> paramList) {
		this.paramList = paramList;
	}

	public String getReferenceName() {
		return referenceName;
	}

	public void setReferenceName(String referenceName) {
		this.referenceName = referenceName;
	}

}
