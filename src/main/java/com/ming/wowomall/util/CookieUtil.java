package com.ming.wowomall.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author m969130721@163.com
 * @date 18-9-15 下午3:33
 */
@Slf4j
public class CookieUtil {

    private static final String COOKIE_DOMAIN = "wowomall.com";

    private static final String COOKIE_NAME = "wowomall_session_token";

    /**
     * 登录默认一年有效期
     */
    private static final int COOKIE_LOGIN_AGE = 60 * 60 * 24 * 365;


    public static void writeLoginToken(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie(COOKIE_NAME, token);
        cookie.setDomain(COOKIE_DOMAIN);
        //单位秒 -1永久 0清楚,不写默认写到内存
        cookie.setMaxAge(COOKIE_LOGIN_AGE);
        //不许通过脚本访问cookie
        cookie.setHttpOnly(true);
        //代表在根目录
        cookie.setPath("/");
        response.addCookie(cookie);
        log.info("writeLoginToken cookieName:{},value:{}", COOKIE_NAME, token);
    }

    public static void delLoginToken(HttpServletRequest request,HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie ck : cookies) {
                if (StringUtils.equals(COOKIE_NAME, ck.getName())) {
                    log.info("delLoginToken cookieName:{},value:{}", COOKIE_NAME, ck.getValue());
                    ck.setDomain(COOKIE_DOMAIN);
                    ck.setPath("/");
                    ck.setMaxAge(0);
                    response.addCookie(ck);
                    return;
                }
            }
        }
    }

    public static String readLoginToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (StringUtils.equals(COOKIE_NAME, cookie.getName())) {
                    log.info("readLoginToken cookieName:{},value:{}", COOKIE_NAME, cookie.getValue());
                    return cookie.getValue();
                }
            }
        }
        return null;
    }


}
