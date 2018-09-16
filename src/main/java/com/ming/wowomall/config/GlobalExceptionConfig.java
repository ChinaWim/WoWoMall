package com.ming.wowomall.config;

import com.ming.wowomall.common.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.jws.WebResult;
import javax.servlet.http.HttpServletRequest;

/**
 * @author m969130721@163.com
 * @date 18-9-16 下午4:33
 */

@ControllerAdvice
@Slf4j
public class GlobalExceptionConfig {


    @ExceptionHandler(Exception.class)
    public ModelAndView resolveException (Exception exception, HttpServletRequest request){
        log.error("requestURI:{} Exception:",request.getRequestURI(),exception);
        ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());
        modelAndView.addObject("status",ResponseCode.ERROR.getCode());
        modelAndView.addObject("msg","接口异常,详情请查看服务端日志信息");
        modelAndView.addObject("data",exception.getMessage());
        return modelAndView;
    }

}
