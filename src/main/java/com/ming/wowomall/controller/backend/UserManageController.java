package com.ming.wowomall.controller.backend;

import com.ming.wowomall.common.Const;
import com.ming.wowomall.common.ServerResponse;
import com.ming.wowomall.pojo.User;
import com.ming.wowomall.service.UserService;
import com.ming.wowomall.util.CookieUtil;
import com.ming.wowomall.util.JsonUtil;
import com.ming.wowomall.util.RedisShardedPoolUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author m969130721@163.com
 * @date 18-8-28 下午11:25
 */
@RestController
@RequestMapping("/manage/user")
public class UserManageController {

    @Autowired
    private UserService userService;


    @PostMapping("/login.do")
    public Object login(String username, String password, HttpSession session, HttpServletResponse httpServletResponse){
        ServerResponse<User> response = userService.login(username, password);
        if (response.isSuccess()) {
            User user = response.getData();
            if (Const.Role.ROLE_ADMIN == user.getRole()) {
                CookieUtil.writeLoginToken(httpServletResponse,session.getId());
                RedisShardedPoolUtil.setEx(session.getId(),JsonUtil.obj2String(user),Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
            }else {
                return ServerResponse.createByErrorMessage("不是管理员,无法登录");
            }
        }
        return response;
    }
}
