package com.ming.wowomall.framework.interceptor;

import com.ming.wowomall.common.Const;
import com.ming.wowomall.common.ResponseCode;
import com.ming.wowomall.common.ServerResponse;
import com.ming.wowomall.pojo.User;
import com.ming.wowomall.util.CookieUtil;
import com.ming.wowomall.util.JsonUtil;
import com.ming.wowomall.util.RedisShardedPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author m969130721@163.com
 * @date 18-9-16 下午8:19
 */
@Slf4j
public class AuthorityInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //请求中Controller中的方法名
        HandlerMethod handlerMethod = (HandlerMethod)handler;
        Method method = handlerMethod.getMethod();
        String methodName = method.getName();
        String className = method.getClass().getSimpleName();

        User user = null;
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isNoneBlank(loginToken)){
            String userJsonStr = RedisShardedPoolUtil.get(loginToken);
            user = JsonUtil.string2Obj(userJsonStr,User.class);
        }
        if (user == null || user.getRole().intValue() != Const.Role.ROLE_ADMIN){
            //这里要添加reset，否则报异常 getWriter() has already been called for this response.
            response.reset();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter writer = response.getWriter();
            if (user == null) {
                //上传由于富文本特殊要求返回
                if (request.getRequestURI().equals("/manage/product/richtext_img_upload.do")) {
                    Map resultMap = new HashMap();
                    resultMap.put("success",false);
                    resultMap.put("msg","用户未登录");
                    writer.write(JsonUtil.obj2String(resultMap));
                } else {
                    writer.write(JsonUtil.obj2String(ServerResponse.createByErrorMessage("用户未登录")));
                }
                log.info("用户未登录");
            } else {
                //上传由于富文本特殊要求返回
                if (request.getRequestURI().equals("/manage/product/richtext_img_upload.do")) {
                    Map resultMap = new HashMap();
                    resultMap.put("success",false);
                    resultMap.put("msg","无权访问");
                    writer.write(JsonUtil.obj2String(resultMap));
                }else{
                    writer.write(JsonUtil.obj2String(ServerResponse.createByErrorMessage("无权访问")));
                }

                log.info("无权访问");
            }
            writer.flush();
            writer.close();
            return false;
        }
        return  true;
    }

}
