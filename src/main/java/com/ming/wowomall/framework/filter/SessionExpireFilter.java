package com.ming.wowomall.framework.filter;

import com.ming.wowomall.common.Const;
import com.ming.wowomall.pojo.User;
import com.ming.wowomall.util.CookieUtil;
import com.ming.wowomall.util.JsonUtil;
import com.ming.wowomall.util.RedisShardedPoolUtil;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
            String userJsonStr = RedisShardedPoolUtil.get(loginToken);
            User user = JsonUtil.string2Obj(userJsonStr,User.class);
            if (user != null) {
                log.info("执行sessionExpireFilter,loginToken:{},extime:{}",loginToken,Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
                RedisShardedPoolUtil.expire(loginToken, Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
            }
        }
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }

    @Override
    public void destroy() {

    }
}
