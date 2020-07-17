package com.jzx.validate.core.validate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSON;
import com.jzx.validate.core.core.ValidateCore;
import com.jzx.validate.core.message.Message;

/**
 * 参数验证拦截
 * 
 * @author 杨杰
 * @version 2018年4月9日
 * @see RestValidateInterceptor
 * @since
 */
public class RestValidateInterceptor extends HandlerInterceptorAdapter {
    private final Logger LOGGER = LoggerFactory.getLogger(RestValidateInterceptor.class);
    private ValidateCore validateCore = new ValidateCore();

    /**
     * 在业务处理器处理请求之前被调用 如果返回false <br/>
     * 从当前的拦截器往回执行所有拦截器的afterCompletion(),再退出拦截器链 <br/>
     * 如果返回true 执行下一个拦截器,直到所有的拦截器都执行完毕<br/>
     * 再执行被拦截的Controller <br/>
     * 然后进入拦截器链, 从最后一个拦截器往回执行所有的postHandle() <br/>
     * 接着再从最后一个拦截器往回执行所有的afterCompletion()<br/>
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws Exception {
        long startTime = System.currentTimeMillis();
        String requestUri = request.getRequestURI();
        String contextPath = request.getContextPath();
        String url = requestUri.substring(contextPath.length());
        // 存放所有需要验证的参数
        Map paramMap = new LinkedHashMap();
        String methodName = request.getMethod().toUpperCase();
        HttpServletRequest httpRequest = (HttpServletRequest)request;
        HttpServletRequest requestWrapper = new RequestWrapper(httpRequest);

        if (StringUtils.equalsIgnoreCase(methodName, HttpMethod.POST.name())
            || StringUtils.equalsIgnoreCase(methodName, HttpMethod.PUT.name())) {
            String content = getBody(requestWrapper);
            paramMap = JSON.parseObject(content, LinkedHashMap.class);
        }
        if (StringUtils.equalsIgnoreCase(methodName, HttpMethod.GET.name())
            || StringUtils.equalsIgnoreCase(methodName, HttpMethod.DELETE.name())) {
            // 获取所有的请求参数
            Map reqMap = request.getParameterMap();
            for (Iterator it = reqMap.entrySet().iterator(); it.hasNext();) {
                Map.Entry entry = (Entry)it.next();
                paramMap.put(entry.getKey(), ((String[])entry.getValue())[0]);
            }
        }
        // 验证参数
        Message message = validateCore.validate(url, paramMap);
        if (message != null) {
            String json = JSON.toJSONString(message);
            this.responseJson(response, json);
            return false;
        }
        LOGGER.info("requestURI : " + url + " exec success!");
        LOGGER.debug("params validate runtime is : [ " + (System.currentTimeMillis() - startTime) + " ] ms");
        return true;
    }

    /**
     * 在业务处理器处理请求执行完成后,生成视图之前执行的动作 可在modelAndView中加入数据，比如当前时间
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
        ModelAndView modelAndView) throws Exception {
        // log.info("==============执行顺序: 、postHandle================");
        // if (modelAndView != null) { // 加入当前时间
        // modelAndView.addObject("var", "测试postHandle");
        // }
    }

    /**
     * 在DispatcherServlet完全处理完请求后被调用,可用于清理资源等
     * 
     * 当有拦截器抛出异常时,会从当前拦截器往回执行所有的拦截器的afterCompletion()
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
        throws Exception {}

    private void responseJson(HttpServletResponse response, String json) throws Exception {
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");
        try {
            writer = response.getWriter();
            writer.print(json);
        } catch (IOException e) {
            LOGGER.error("response error:{}", e);
        } finally {
            if (writer != null)
                writer.close();
        }
    }

    public static String getBody(HttpServletRequest request) throws IOException {
        BufferedReader reader = request.getReader();
        char[] buf = new char[512];
        int len = 0;
        StringBuffer contentBuffer = new StringBuffer();
        while ((len = reader.read(buf)) != -1) {
            contentBuffer.append(buf, 0, len);
        }
        String content = contentBuffer.toString();
        if (content == null) {
            content = "";
        }
        return content;
    }
}
