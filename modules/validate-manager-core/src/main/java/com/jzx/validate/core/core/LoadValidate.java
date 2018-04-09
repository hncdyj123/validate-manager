package com.jzx.validate.core.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

/**
 * 解析xml中的验证规则类
 * 
 * @author 杨杰
 * @version 2018年4月9日
 * @see LoadValidate
 * @since
 */
public class LoadValidate {
	/** 存放所有的验证规则 **/
	private static Map<String, ActionValidate> validateMap = new LinkedHashMap<String, ActionValidate>();

	static {
		try {
			readConfiguration();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * 读取验证规则
	 * 
	 * @param xmlStr
	 * @throws DocumentException
	 */
	@SuppressWarnings("rawtypes")
	public static void loadValidate(String xmlStr) throws DocumentException {
		Document document = DocumentHelper.parseText(xmlStr);
		// 获取要解析的元素
		Element root = document.getRootElement();
		for (Iterator parent = root.elementIterator(); parent.hasNext();) {
			Element e = (Element) parent.next();
			ActionValidate actionValidate = new ActionValidate();
			actionValidate.setActionUrl(e.attributeValue("actionUrl"));
			List<ParamValidate> valdateList = new ArrayList<ParamValidate>();
			for (Iterator son = e.elementIterator(); son.hasNext();) {
				Element el = (Element) son.next();
				// 搜索Action引用的验证规则
				if (StringUtils.equalsIgnoreCase(el.getName(), "reference")) {
					if (StringUtils.isEmpty(actionValidate.getReferenceName())) {
						actionValidate.setReferenceName(el.attributeValue("name"));
					}
					continue;
				}
				// 验证规则
				ParamValidate validate = new ParamValidate();
				validate.setName(el.attributeValue("name"));
				validate.setValidate(el.attributeValue("validate"));
				validate.setType(el.attributeValue("type"));
				validate.setIsNull(el.attributeValue("isnull"));
				validate.setMsg(el.attributeValue("msg"));
				// 增加验证规则
				valdateList.add(validate);
			}
			// 增加当前actiion的验证规则
			actionValidate.setParamList(valdateList);
			validateMap.put(e.attributeValue("actionUrl"), actionValidate);
		}
	}

	/**
	 * 
	 * 获取指定目录下所有xml字符串动态数组
	 * 
	 * @return
	 * @throws IOException
	 */
	public static List<String> getValidateXmlStr() throws IOException {
		PathMatchingResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
		Resource[] resources = patternResolver.getResources("classpath:validate/*-validator.xml");
		List<String> xmlStrList = new ArrayList<String>();
		for (int i = 0; i < resources.length; i++) {
			InputStreamReader inputStreamReader = new InputStreamReader(resources[i].getInputStream());
			BufferedReader breader = new BufferedReader(inputStreamReader);
			String temp = breader.readLine();
			String xmlStr = "";
			while (temp != null) {
				xmlStr += temp + "\n";
				temp = breader.readLine();
			}
			xmlStrList.add(xmlStr);
		}
		return xmlStrList;
	}

	/**
	 * 
	 * 初始化配置
	 * 
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static void readConfiguration() throws IOException, DocumentException {
		List<String> xmlStrList = getValidateXmlStr();
		for (String xmlStr : xmlStrList) {
			loadValidate(xmlStr);
		}
	}

	public static Map<String, ActionValidate> getValidateMap() {
		return validateMap;
	}
}
