package com.dm.platform.controller;

import com.dm.platform.util.RequestUtil;
import com.dm.platform.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 工程：os-app
 * 创建人 : ChenGJ
 * 创建时间： 2015/9/6
 * 说明：
 */
@ControllerAdvice
public class ControllerExceptionHandler extends DefaultController{
    private static Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ModelAndView handleAllException(HttpServletResponse response, HttpServletRequest request, Exception e) {
        logger.info("捕获到异常");
        e.printStackTrace();
        if (RequestUtil.isAjax(request)) {
            logger.info("异常来源请求为：{}", "ajax请求");
            ResponseUtil.error();
        } else {
            logger.info("异常来源请求为：{}", "传统页面请求");
            return Error(e);
        }
        return null;
    }
}
