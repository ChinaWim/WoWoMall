package com.ming.wowomall.controller.common;

import com.ming.wowomall.common.Const;
import com.ming.wowomall.pojo.User;
import com.ming.wowomall.util.CookieUtil;
import com.ming.wowomall.util.JsonUtil;
import com.ming.wowomall.util.RedisPoolUtil;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author m969130721@163.com
 * @date 18-9-15 下午5:21
 */
@WebFilter(filterName = "sessionExpireFilter",urlPatterns = "*.do")
public class SessionExpireFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest)servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse)servletResponse;
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if (!StringUtils.isBlank(loginToken)) {
            String userJsonStr = RedisPoolUtil.get(loginToken);
            User user = JsonUtil.string2Obj(userJsonStr,User.class);
            if (user != null) {
                RedisPoolUtil.expire(loginToken, Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
            }
        }
        doFilter(httpServletRequest,httpServletResponse,filterChain);
    }

    @Override
    public void destroy() {

    }
}
