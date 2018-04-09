/*
 * 文件名：UserController.java
 * 版权：Copyright by 杰之迅有限公司
 * 描述：
 * 修改人：杨杰
 * 修改时间：2018年4月9日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.jzx.validate.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jzx.validate.core.message.Message;
import com.jzx.validate.example.domain.User;

@Controller
@RequestMapping("/user")
public class UserController {

	@RequestMapping("/validate")
	@ResponseBody
	public Object validateExample(User user) {
		Message message = new Message();
		return message;
	}
}
